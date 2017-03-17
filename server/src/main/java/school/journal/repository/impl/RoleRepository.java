package school.journal.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school.journal.entity.Role;
import school.journal.repository.RepositoryAbstractClass;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.HibernateSpecification;

import java.util.List;

@Component("RoleRepository")
public class RoleRepository extends RepositoryAbstractClass<Role> {
    @Override
    public Role create(Role role, Session session) throws RepositoryException {
        session.save(role);
        return role;
    }

    @Override
    public Role update(Role role, Session session) throws RepositoryException {
        session.update(role);
        return role;
    }

    @Override
    public Role delete(Role role, Session session) throws RepositoryException {
        session.delete(role);
        return role;
    }

    @Override
    public List<Role> query(HibernateSpecification specification, Session session) throws RepositoryException {
        Criteria criteria =  session.createCriteria(Role.class);
        Criterion criterion;
        if((specification != null) && ((criterion = specification.toCriteria()) != null)){
            criteria.add(criterion);
        }
        return criteria.list();
    }
}
