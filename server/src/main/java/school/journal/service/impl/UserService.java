package school.journal.service.impl;

import school.journal.entity.User;
import school.journal.service.CRUDService;
import school.journal.service.IUserService;
import school.journal.service.exception.ServiceException;
import school.journal.utils.MD5Generator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService extends CRUDService<User> implements IUserService {

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
        return super.create(user);
    }

    private String generateNewPassword() {
        return MD5Generator.generate(((Long)(System.currentTimeMillis() % 1000)).toString());
    }

    @Override
    public User update(User user) throws ServiceException {
        checkUsername(user);
        checkEmail(user);
        checkPassword(user);
        return super.update(user);
    }

    @Override
    public void delete(int id) throws ServiceException {
        validateId(id);
        User user = new User();
        user.setUserId(id);
        super.delete(user);
    }

    @Override
    public List<User> read() throws ServiceException {
        return super.read();
    }

    private void validateId(int id) throws ServiceException {
        if(id <= 0) throw new ServiceException("ID is not correct.");
    }
}
