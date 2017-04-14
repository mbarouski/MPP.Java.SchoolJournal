package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import school.journal.entity.*;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.user.UserSpecificationByUserId;
import school.journal.service.CRUDService;
import school.journal.service.IUserService;
import school.journal.service.exception.ServiceException;
import school.journal.utils.MD5Generator;
import school.journal.utils.exception.ValidationException;

import java.text.MessageFormat;
import java.util.List;

import static school.journal.utils.ValidateServiceUtils.*;

@Service("UserService")
public class UserService extends CRUDService<User> implements IUserService {
    private static final int USER_ROLE = 1;
    private static final int PUPIL_ROLE = 2;
    private static final int DIRECTOR_ROLE = 5;
    private static final int TEACHER_ROLE = 3;
    private static final int DIRECTOR_OF_STUDIES_ROLE = 4;
    private static final int ADMIN_ROLE = 6;

    @Autowired
    @Qualifier("MailService")
    private MailService mailService;

    @Autowired
    public UserService(@Qualifier("UserRepository") IRepository<User> repository, @Qualifier("TokenRepository") IRepository<Token> tokenRepository) {
        LOGGER = Logger.getLogger(UserService.class);
        this.repository = repository;
    }

    private void checkPassword(User newUser, User user) throws ServiceException {
        String password = newUser.getPassword();
        if(password == null) return;
        if (password.length() < 6) {
            throw new ServiceException("Password is small.");
        }
        user.setPassHash(MD5Generator.generate(password));
    }

    @Override
    public User create(User user) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            validateString(user.getUsername(), "Username");
            validateEmail(user.getEmail());
            user.setRole((Role)session.get(Role.class, USER_ROLE));
            String password = generateNewPassword();
            user.setPassHash(MD5Generator.generate(password));
            repository.create(user, session);
            transaction.commit();
            LOGGER.info(MessageFormat.format("Password: {0}", password));
            mailService.sendMail(user.getEmail(), password);
        } catch (RepositoryException | ValidationException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public User changeRole(int userId, int roleId) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        User user = changeRole(userId, roleId, session);
        try {
            repository.update(user, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public User changePassword(int userId, String password) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        User user = (User) session.get(User.class, userId);
        if(user == null) throw new ServiceException("User not found");
        user.setPassHash(MD5Generator.generate(password));
        try {
            repository.update(user, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return user;
    }

    private void deleteTeacherIfExist(int teacherId, Session session) {
        Teacher teacher = (Teacher) session.get(Teacher.class, teacherId);
        if(teacher != null) {
            session.delete(teacher);
        }
    }

    private void deletePupilIfExist(int pupilId, Session session) {
        Pupil pupil = (Pupil) session.get(Pupil.class, pupilId);
        if(pupil != null) {
            session.delete(pupil);
        }
    }

    @Override
    public User changeRole(int userId, int roleId, Session session) throws ServiceException {
        User user = (User) session.get(User.class, userId);
        if(user == null) throw new ServiceException("User not found");
        Role role = (Role) session.get(Role.class, roleId);
        if(role == null) throw new ServiceException("Role not found");
        if(roleId == PUPIL_ROLE) {
            deleteTeacherIfExist(userId, session);
        } else if((roleId == TEACHER_ROLE) || (roleId == DIRECTOR_OF_STUDIES_ROLE) || (roleId == DIRECTOR_ROLE)) {
            deletePupilIfExist(userId, session);
        } else {
            deleteTeacherIfExist(userId, session);
            deletePupilIfExist(userId, session);
        }
        user.setRole(role);
        return user;
    }

    private String generateNewPassword() {
        return MD5Generator.generate(((Long)(System.currentTimeMillis() % 1000)).toString());
    }

    private void checkNewRole(User newUser, User user, Session session) throws ServiceException {
        Integer roleId = newUser.getRoleId();
        if(roleId == null) return;
        try {
            Role role = (Role) session.load(Role.class, roleId);
            user.setRole(role);
        } catch (ObjectNotFoundException exc) {
            throw new ServiceException("Role is not found");
        }
    }

    private int checkId(Integer id) throws ServiceException {
        if(id == null) throw new ServiceException("Incorrect user id");
        return id.intValue();
    }

    @Override
    public User update(User newUser) throws ServiceException {
        User user;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            user = repository.get(checkId(newUser.getUserId()), session);
            checkPassword(newUser, user);
            checkNewRole(newUser, user, session);
            repository.update(user, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public void delete(int id) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = (User)session.load(User.class, id);
            repository.delete(user, session);
            transaction.commit();
        } catch (ObjectNotFoundException | RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> read() throws ServiceException {
        return super.read();
    }
}
