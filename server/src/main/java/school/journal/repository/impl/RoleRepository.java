package school.journal.repository.impl;

import org.hibernate.Session;
import school.journal.entity.Role;
import school.journal.persistence.HibernateUtil;
import school.journal.repository.IRepository;
import school.journal.repository.RepositoryAbstractClass;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.HibernateSpecification;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class RoleRepository extends RepositoryAbstractClass<Role> {
    private static final RoleRepository instance = new RoleRepository();

    public static RoleRepository getInstance(){
        return instance;
    }


    public List<Role> read() throws RepositoryException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Role> roles = (List<Role>)session.createCriteria(Role.class).list();
        session.getTransaction().commit();
        session.close();
        return roles;
    }

    @Override
    public Role create(Role role, Session session) throws RepositoryException {
        return null;
    }

    @Override
    public Role update(Role role, Session session) throws RepositoryException {
        return null;
    }

    @Override
    public Role delete(Role role, Session session) throws RepositoryException {
        return null;
    }

    @Override
    public List<Role> query(HibernateSpecification specification, Session session) throws RepositoryException {
        return null;
    }
}
