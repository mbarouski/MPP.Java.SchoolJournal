package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import school.journal.entity.User;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.service.IUserService;
import school.journal.service.ServiceAbstractClass;
import school.journal.service.exception.ServiceException;
import school.journal.utils.MD5Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService extends ServiceAbstractClass implements IUserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class);

    @Autowired
    private IRepository<User> userRepository;

    private Pattern EMAIL_PATTERN = Pattern.compile("[0-9a-zA-Z]+@[0-9a-zA-Z]");

    private void checkUsername(User user) throws ServiceException {
        if((user.getUsername() == null) || (user.getUsername().isEmpty())){
            throw new ServiceException("Username is empty.");
        }
    }

    private void checkEmail(User user) throws ServiceException {
        if((user.getEmail() == null) || (user.getEmail().isEmpty())) {
            throw new ServiceException("Email is empty.");
        }
        Matcher m = EMAIL_PATTERN.matcher(user.getEmail());
        if(!m.matches()) {
            throw new ServiceException("Email isn't correct.");
        }
    }

    private void checkPassword(User user) throws ServiceException {
        String password = user.getPassword();
        if((password == null) || (password.isEmpty())){
            throw new ServiceException("Password is empty.");
        }
        if(password.length() < 6) {
            throw new ServiceException("Password is small.");
        }
        user.setPassHash(MD5Generator.generate(password));
    }

    @Override
    public User create(User user) throws ServiceException {
        checkUsername(user);
        checkEmail(user);
        user.setLocked((byte)0);
        user.setPassHash(MD5Generator.generate(generateNewPassword()));
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            user = userRepository.create(user, session);
            session.getTransaction().commit();
        } catch (RepositoryException exc) {
            session.getTransaction().rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return user;
    }

    private String generateNewPassword() {
        return MD5Generator.generate(((Long)(System.currentTimeMillis() % 1000)).toString());
    }

    @Override
    public User update(User user) throws ServiceException {
        checkUsername(user);
        checkEmail(user);
        checkPassword(user);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            user = userRepository.update(user, session);
            session.getTransaction().commit();
        } catch (RepositoryException exc) {
            session.getTransaction().rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public void delete(int id) throws ServiceException {
        if(id <= 0) {
            throw new ServiceException("ID is not correct.");
        }
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user = new User();
        user.setUserId(id);
        try {
            userRepository.delete(user, session);
            session.getTransaction().commit();
        } catch (RepositoryException exc) {
            session.getTransaction().rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> read() throws ServiceException {
        List<User> users;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            users = userRepository.query(null, session);
            session.getTransaction().commit();
        } catch (RepositoryException exc) {
            users = new ArrayList<>();
            session.getTransaction().rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return users;
    }
}
