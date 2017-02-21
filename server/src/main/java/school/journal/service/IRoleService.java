package school.journal.service;

import school.journal.entity.Role;
import school.journal.service.exception.ServiceException;

import java.util.List;

public interface IRoleService {
    List<Role> getRoles() throws ServiceException;

    Role createRole(Role role) throws ServiceException;
    Role updateRole(Role role) throws ServiceException;
    Role deleteRole(int roleId) throws ServiceException;
    Role getOne(int roleId) throws ServiceException;
}
