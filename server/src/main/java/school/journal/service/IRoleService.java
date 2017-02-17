package school.journal.service;

import school.journal.entity.Role;
import school.journal.service.exception.ServiceException;

import java.util.List;

public interface IRoleService {
    List<Role> getRoles() throws ServiceException;
}
