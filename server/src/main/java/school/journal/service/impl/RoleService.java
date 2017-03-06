package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import school.journal.entity.Role;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.role.RoleSpecificationByRoleId;
import school.journal.service.CRUDService;
import school.journal.service.IRoleService;
import school.journal.service.exception.ServiceException;
import java.util.List;

import static school.journal.utils.ValidateServiceUtils.*;

@Component
public class RoleService extends CRUDService<Role> implements IRoleService {

    public RoleService() {
        LOGGER = Logger.getLogger(RoleService.class);
    }

    @Override
    public Role getOne(int roleId) throws ServiceException {
        validateId(roleId, "Role");
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
        validateString(obj.getName(),"Name");
        validateLevel(obj.getLevel());
        return super.create(obj);
    }

    @Override
    public Role update(Role obj) throws ServiceException {
        validateId(obj.getRoleId(), "Role");
        validateString(obj.getName(),"Name");
        validateLevel(obj.getLevel());
        return super.update(obj);
    }

    @Override
    public void delete(int id) throws ServiceException {
        validateId(id, "Role");
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
