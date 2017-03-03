package school.journal.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import school.journal.entity.User;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.user.UserSpecificationByUsername;
import school.journal.service.IAuthService;
import school.journal.service.ServiceAbstractClass;
import school.journal.service.exception.AuthException;
import school.journal.service.exception.ServiceException;
import school.journal.utils.MD5Generator;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
public class AuthService extends ServiceAbstractClass implements IAuthService {
    private final String SECRET = "simple_secret_string";

    @Autowired
    private IRepository<User> userRepository;


    /**
     * Returns token string value.
     */
    @Override
    public String login(String username, String password) throws AuthException, ServiceException {
        List<User> users;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            users = userRepository.query(new UserSpecificationByUsername(username), session);
            if(users.size() > 0){
                User user = users.get(0);
                if(verifyPasswords(user.getPassHash(), password)) {
                    try {
                        return JWT.create().withSubject(username).sign(Algorithm.HMAC256(SECRET));
                    } catch (UnsupportedEncodingException exc) {

                    }
                }
            }
        } catch (RepositoryException exc) {

        } finally {
            session.getTransaction().commit();
            session.close();
        }
        return null;
    }

    private boolean verifyPasswords(String hash, String pass) {
        return hash.equals(MD5Generator.generate(pass));
    }

    @Override
    public void logout() throws AuthException, ServiceException {

    }

    /**
     * If token is valid returns user object, in another case returns null.
     */
    @Override
    public User checkToken(String token) throws AuthException, ServiceException {
        JWT jwt = JWT.decode(token);
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
                        return user;
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
