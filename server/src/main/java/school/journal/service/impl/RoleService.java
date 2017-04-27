package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import school.journal.controller.util.ExceptionEnum;
import school.journal.entity.Role;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.role.RoleSpecificationByRoleId;
import school.journal.service.CRUDService;
import school.journal.service.IRoleService;
import school.journal.service.exception.ClassifiedServiceException;
import school.journal.service.exception.ServiceException;
import school.journal.utils.exception.ValidationException;

import java.util.List;

import static school.journal.utils.ValidateServiceUtils.*;

@Service("RoleService")
public class RoleService extends CRUDService<Role> implements IRoleService {

    @Autowired
    public RoleService(@Qualifier("RoleRepository")IRepository<Role> repository) {
        LOGGER = Logger.getLogger(RoleService.class);
        this.repository = repository;
    }

    @Override
    public Role create(Role obj) throws ServiceException {
        try {
            validateString(obj.getName(),"Name");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ClassifiedServiceException(ExceptionEnum.role_not_found);
        }
        validateLevel(obj.getLevel());
        return super.create(obj);
    }

    @Override
    public Role update(Role obj) throws ServiceException {
        try {
            validateId(obj.getRoleId(), "Role");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ClassifiedServiceException(ExceptionEnum.role_not_found);
        }
        try {
            validateString(obj.getName(), "Name");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ClassifiedServiceException(ExceptionEnum.role_has_wrong_name);
        }
        validateLevel(obj.getLevel());
        return super.update(obj);
    }

    @Override
    public void delete(int id) throws ServiceException {
        try {
            validateId(id, "Role");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ClassifiedServiceException(ExceptionEnum.role_not_found);
        }
        Role role = new Role();
        role.setRoleId(id);
        super.delete(role);
    }

    @Override
    public List<Role> read() throws ServiceException {
        return super.read();

    }

    private void validateLevel(int level) throws ServiceException {
        if (level <= 0) {
            throw new ClassifiedServiceException(ExceptionEnum.role_has_wrong_level);
        }
    }
}
