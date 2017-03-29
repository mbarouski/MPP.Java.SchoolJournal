package school.journal.service;

import school.journal.entity.Token;
import school.journal.entity.User;
import school.journal.service.exception.AuthException;
import school.journal.service.exception.ServiceException;

public interface IAuthService {
    Token login(String username, String password) throws AuthException, ServiceException;

    void logout(User user) throws AuthException, ServiceException;

    User checkToken(String token) throws AuthException, ServiceException;
}
