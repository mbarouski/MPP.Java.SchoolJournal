package school.journal.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import school.journal.entity.Role;
import school.journal.entity.Token;
import school.journal.entity.User;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.role.RoleSpecificationByRoleId;
import school.journal.repository.specification.token.TokenSpecificationByTokenId;
import school.journal.repository.specification.user.UserSpecificationByUsername;
import school.journal.service.IAuthService;
import school.journal.service.ServiceAbstractClass;
import school.journal.service.exception.AuthException;
import school.journal.service.exception.ServiceException;
import school.journal.utils.MD5Generator;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service(value = "AuthService")
public class AuthService extends ServiceAbstractClass implements IAuthService {
    private final String SECRET = "simple_secret_string";

    @Autowired
    @Qualifier("UserRepository")
    private IRepository<User> userRepository;

    @Autowired
    @Qualifier("TokenRepository")
    private IRepository<Token> tokenRepository;

    @Autowired
    @Qualifier("RoleRepository")
    private IRepository<Role> roleRepository;

    private void updateToken(Token token) throws ServiceException{
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            tokenRepository.update(token, session);
            session.getTransaction().commit();
        } catch (RepositoryException exc) {
            session.getTransaction().rollback();
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
    }

    /**
     * Returns token string value.
     */
    @Override
    public Token login(String username, String password) throws AuthException, ServiceException {
        List<User> users;
        Token token = null;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            users = userRepository.query(new UserSpecificationByUsername(username), session);
            if(users.size() > 0){
                User user = users.get(0);
                if(user.getLocked() != 0) {
                    throw new AuthException("User is blocked");
                }
                if(verifyPasswords(user.getPassHash(), password)) {
                    try {
                        String tokenValue = JWT.create().withSubject(username).sign(Algorithm.HMAC256(SECRET));
                        boolean isNew = false;
                        token = (Token)session.get(Token.class, user.getUserId());
                        if (token == null) {
                            isNew = true;
                            token = new Token();
                            token.setUser((User)session.get(User.class, user.getUserId()));
                        }
                        token.setUserId(user.getUserId());
                        token.setValue(tokenValue);
                        token.setActive((byte)1);
                        token = isNew ? tokenRepository.create(token, session) : tokenRepository.update(token, session);
                        session.getTransaction().commit();
                        token.getUser().setPassHash(null);
                    } catch (UnsupportedEncodingException exc) { }
                } else {
                    throw new AuthException("Password is incorrect");
                }
            } else {
                throw new AuthException("User is not found");
            }
        } catch (RepositoryException exc) {
            session.getTransaction().rollback();
            token = null;
        } finally {
            session.close();
        }
        return token;
    }

    private boolean verifyPasswords(String hash, String pass) {
        return hash.equals(MD5Generator.generate(pass));
    }

    @Override
    public void logout(User user) throws AuthException, ServiceException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            Token token = new Token();
            token.setUserId(user.getUserId());
            token.setActive((byte)0);
            token.setValue("");
            tokenRepository.update(token, session);
            session.getTransaction().commit();
        } catch (RepositoryException exc) {
            session.getTransaction().rollback();
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
    }

    /**
     * If token is valid returns user object, in another case returns null.
     */
    @Override
    public User checkToken(String token) throws AuthException, ServiceException {
        if((token == null) || (token.isEmpty())) {
            throw new AuthException("Token is incorrect");
        }
        JWT jwt = null;
        try {
            jwt = JWT.decode(token);
        } catch (JWTDecodeException exc) {
            throw new AuthException("Token is incorrect");
        }
        try {
            String username = jwt.getSubject();
            if (username != null) {
                User user;
                Session session = null;
                try {
                    session = sessionFactory.openSession();
                    session.beginTransaction();
                    List<User> users = userRepository.query(new UserSpecificationByUsername(username), session);
                    if (users.size() > 0) {
                        user = users.get(0);
                        Token tokenObj = (Token) session.get(Token.class, user.getUserId());
                        if(tokenObj.getActive() == 0) {
                            throw new AuthException("Token is not active");
                        }
//                        if(user.getLocked() != 0) {
//                            throw new AuthException("User is blocked");
//                        }
                        return user;
                    } else {
                        throw new AuthException("Logout error");
                    }
                } catch (RepositoryException exc) {

                } finally {
                    session.getTransaction().commit();
                    session.close();
                }
            }
        } catch (JWTVerificationException exc){

        }
        return null;
    }
}
