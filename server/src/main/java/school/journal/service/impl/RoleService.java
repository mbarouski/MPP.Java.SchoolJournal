package school.journal.service.impl;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import school.journal.entity.Role;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.role.RoleSpecification;
import school.journal.repository.specification.role.RoleSpecificationByRoleId;
import school.journal.service.IRoleService;
import school.journal.service.ServiceAbstractClass;
import school.journal.service.exception.ServiceException;

import java.util.List;

@Component
public class RoleService extends ServiceAbstractClass implements IRoleService {
    @Autowired
    private IRepository<Role> roleRepository;

    @Override
    public List<Role> getRoles() throws ServiceException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            return roleRepository.query(null, session);
        } catch (RepositoryException exc){
            throw new ServiceException();
        }
    }

    @Override
    public Role createRole(Role role) throws ServiceException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        if(role.getName().isEmpty()){
            throw new ServiceException("Invalid name");
        }
        if(role.getLevel() <= 0){
            throw new ServiceException("Invalid level");
        }
        try {
            roleRepository.create(role, session);
        } catch (RepositoryException exc) {
            return role;
        }
        session.getTransaction().commit();
        return role;
    }

    @Override
    public Role updateRole(Role role) throws ServiceException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        if(role.getName().isEmpty()){
            throw new ServiceException("Invalid name");
        }
        if(role.getLevel() <= 0){
            throw new ServiceException("Invalid level");
        }
        if(role.getRoleId() <= 0){
            throw new ServiceException("Invalid id");
        }
        try {
            roleRepository.update(role, session);
        } catch (RepositoryException exc) {
            return role;
        }
        session.getTransaction().commit();
        return role;
    }

    @Override
    public Role deleteRole(int roleId) throws ServiceException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        if(roleId <= 0){
            throw new ServiceException("Invalid id");
        }
        Role role = new Role();
        role.setRoleId(roleId);
        try {
            roleRepository.delete(role, session);
        } catch (RepositoryException exc) {
            return role;
        }
        session.getTransaction().commit();
        return role;
    }

    @Override
    public Role getOne(int roleId) throws ServiceException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        if(roleId <= 0){
            throw new ServiceException("Invalid id");
        }
        RoleSpecification specification = new RoleSpecificationByRoleId(roleId);
        Role role = null;
        try {
            List list = roleRepository.query(specification, session);
            if(list.size() > 0){
                role = (Role)list.get(0);
            }
        } catch (RepositoryException exc) {
        }
        session.getTransaction().commit();
        return role;
    }
}
