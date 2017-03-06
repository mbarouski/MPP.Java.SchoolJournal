package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import school.journal.entity.User;
import school.journal.service.CRUDService;
import school.journal.service.IUserService;
import school.journal.service.exception.ServiceException;
import school.journal.utils.MD5Generator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static school.journal.utils.ValidateServiceUtils.*;

@Component
public class UserService extends CRUDService<User> implements IUserService {

    public UserService() {
        LOGGER = Logger.getLogger(UserService.class);
    }

    private static Pattern EMAIL_PATTERN = Pattern.compile("[0-9a-zA-Z]+@[0-9a-zA-Z]");

    private void checkEmail(User user) throws ServiceException {
        validateString(user.getEmail(), "Email");
        Matcher m = EMAIL_PATTERN.matcher(user.getEmail());
        if (!m.matches()) {
            throw new ServiceException("Email isn't correct.");
        }
    }

    private void checkPassword(User user) throws ServiceException {
        String password = user.getPassword();
        validateString(password, "Password");
        if (password.length() < 6) {
            throw new ServiceException("Password is small.");
        }
        user.setPassHash(MD5Generator.generate(password));
    }

    @Override
    public User create(User user) throws ServiceException {
        validateString(user.getUsername(), "Username");
        checkEmail(user);
        user.setLocked((byte) 0);
        user.setPassHash(MD5Generator.generate(generateNewPassword()));
        return super.create(user);
    }

    private String generateNewPassword() {
        return MD5Generator.generate(((Long)(System.currentTimeMillis() % 1000)).toString());
    }

    @Override
    public User update(User user) throws ServiceException {
        validateString(user.getUsername(), "Username");
        checkEmail(user);
        checkPassword(user);
        return super.update(user);
    }

    @Override
    public void delete(int id) throws ServiceException {
        validateId(id, "User");
        User user = new User();
        user.setUserId(id);
        super.delete(user);
    }

    @Override
    public List<User> read() throws ServiceException {
        return super.read();
    }
}
