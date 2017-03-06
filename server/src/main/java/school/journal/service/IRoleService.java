package school.journal.service;

import school.journal.entity.Role;
import school.journal.service.exception.ServiceException;

import java.util.List;

public interface IRoleService extends IService<Role>{

    Role getOne(int roleId) throws ServiceException;
}
