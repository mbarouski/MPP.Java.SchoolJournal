package school.journal.service;

import org.hibernate.Session;
import school.journal.entity.User;
import school.journal.service.exception.ServiceException;

public interface IUserService extends IService<User> {
    User changeRole(int userId, int roleId) throws ServiceException;

    User changePassword(int userId, String password) throws ServiceException;

    User changeRole(int userId, int roleId, Session session) throws ServiceException;

    User getOne(int userId) throws ServiceException;
}
