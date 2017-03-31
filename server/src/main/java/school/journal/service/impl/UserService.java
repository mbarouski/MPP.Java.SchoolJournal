package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import school.journal.entity.Role;
import school.journal.entity.Token;
import school.journal.entity.User;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.user.UserSpecificationByUserId;
import school.journal.service.CRUDService;
import school.journal.service.IUserService;
import school.journal.service.exception.ServiceException;
import school.journal.utils.MD5Generator;
import school.journal.utils.exception.ValidationException;

import java.util.List;

import static school.journal.utils.ValidateServiceUtils.*;

@Service("UserService")
public class UserService extends CRUDService<User> implements IUserService {
    private static final int USER_ROLE = 2;

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
            user.setLocked((byte) 0);
            user.setRole((Role)session.load(Role.class, USER_ROLE));
            user.setPassHash(MD5Generator.generate(generateNewPassword()));
            repository.create(user, session);
            transaction.commit();
        } catch (RepositoryException | ValidationException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            if(session != null) {
                session.close();
            }
        }
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

    private void checkLockedStatus(User newUser, User user) {
        Byte lockedStatus = newUser.getLocked();
        if(lockedStatus == null) return;
        if(lockedStatus >= 0) {
            user.setLocked((byte)1);
        } else {
            user.setLocked((byte)0);
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
            checkLockedStatus(newUser, user);
            repository.update(user, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            if(session != null) {
                session.close();
            }
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
            if(session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<User> read() throws ServiceException {
        return super.read();
    }
}
