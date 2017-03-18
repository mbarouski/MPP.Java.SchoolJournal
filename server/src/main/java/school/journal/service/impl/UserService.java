package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school.journal.entity.Token;
import school.journal.entity.User;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.impl.TokenRepository;
import school.journal.repository.impl.UserRepository;
import school.journal.repository.specification.user.UserSpecificationByUserId;
import school.journal.service.CRUDService;
import school.journal.service.IUserService;
import school.journal.service.exception.ServiceException;
import school.journal.utils.MD5Generator;
import school.journal.utils.exception.ValidationException;

import java.util.List;

import static school.journal.utils.ValidateServiceUtils.*;

@Component("UserService")
public class UserService extends CRUDService<User> implements IUserService {

    @Autowired
    public UserService(@Qualifier("UserRepository") IRepository<User> repository, @Qualifier("TokenRepository") IRepository<Token> tokenRepository) {
        LOGGER = Logger.getLogger(UserService.class);
        this.repository = repository;
    }

    private void checkPassword(User user) throws ServiceException {
        String password = user.getPassword();
        try {
            validateString(password, "Password");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        if (password.length() < 6) {
            throw new ServiceException("Password is small.");
        }
        user.setPassHash(MD5Generator.generate(password));
    }

    @Override
    public User create(User user) throws ServiceException {
        try {
            validateString(user.getUsername(), "Username");
            validateEmail(user.getEmail());
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        user.setLocked((byte) 0);
        user.setPassHash(MD5Generator.generate(generateNewPassword()));
        return super.create(user);
    }

    private String generateNewPassword() {
        return MD5Generator.generate(((Long)(System.currentTimeMillis() % 1000)).toString());
    }

    @Override
    public User update(User newUser) throws ServiceException {
        User user;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<User> users = repository.query(new UserSpecificationByUserId(newUser.getUserId()), session);
            if(users.size() == 0) {
                throw new ServiceException("No such user");
            }
            user = users.get(0);


            repository.update(user, session);
        } catch (RepositoryException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
//        catch (ValidationException exc) {
//            LOGGER.error(exc);
//            throw new ServiceException(exc);
//        }
        checkPassword(newUser);
        return user;
    }

    @Override
    public void delete(int id) throws ServiceException {
        try {
            validateId(id, "User");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        User user = new User();
        user.setUserId(id);
        super.delete(user);
    }

    @Override
    public List<User> read() throws ServiceException {
        return super.read();
    }
}
