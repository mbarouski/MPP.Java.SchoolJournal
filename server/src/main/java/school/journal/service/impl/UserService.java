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

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService extends ServiceAbstractClass implements IUserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class);

    @Autowired
    private IRepository<User> userRepository;

    private Pattern EMAIL_PATTERN = Pattern.compile("[0-9a-zA-Z]+@[0-9a-zA-Z]");

    @Override
    public User create(User user) throws ServiceException {
        if((user.getUsername() == null) || (user.getUsername().isEmpty())){
            throw new ServiceException("Username is empty.");
        }

        if((user.getEmail() == null) || (user.getEmail().isEmpty())) {
            throw new ServiceException("Email is empty.");
        }
        Matcher m = EMAIL_PATTERN.matcher(user.getEmail());
        if(!m.matches()) {
            throw new ServiceException("Email isn't correct.");
        }
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
        return null;
    }

    @Override
    public User delete(int id) throws ServiceException {
        return null;
    }

    @Override
    public List<User> read() throws ServiceException {
        return null;
    }
}
