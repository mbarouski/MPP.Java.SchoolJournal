package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school.journal.entity.Role;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.role.RoleSpecificationByRoleId;
import school.journal.service.CRUDService;
import school.journal.service.IRoleService;
import school.journal.service.exception.ServiceException;
import school.journal.utils.exception.ValidationException;

import java.util.List;

import static school.journal.utils.ValidateServiceUtils.*;

@Component("RoleService")
public class RoleService extends CRUDService<Role> implements IRoleService {

    @Autowired
    public RoleService(@Qualifier("RoleRepository")IRepository<Role> repository) {
        LOGGER = Logger.getLogger(RoleService.class);
        this.repository = repository;
    }

    @Override
    public Role getOne(int roleId) throws ServiceException {
        try {
            validateId(roleId, "Role");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Role role = null;
        try {
            List list = repository.query(
                    new RoleSpecificationByRoleId(roleId), session);
            session.getTransaction().commit();
            if (list.size() > 0) {
                role = (Role) list.get(0);
            }
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
        } finally {
            session.close();
        }
        return role;
    }

    @Override
    public Role create(Role obj) throws ServiceException {
        try {
            validateString(obj.getName(),"Name");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        validateLevel(obj.getLevel());
        return super.create(obj);
    }

    @Override
    public Role update(Role obj) throws ServiceException {
        try {
            validateId(obj.getRoleId(), "Role");
            validateString(obj.getName(),"Name");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
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
            throw new ServiceException(exc);
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
        if (level <= 0) throw new ServiceException("Invalid level");
    }
}
