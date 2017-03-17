package school.journal.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Component;
import school.journal.entity.Subject;
import school.journal.repository.RepositoryAbstractClass;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.HibernateSpecification;

import java.util.List;

@Component("SubjectRepository")
public class SubjectRepository extends RepositoryAbstractClass<Subject> {
    @Override
    public Subject create(Subject subject, Session session) throws RepositoryException {
        session.save(subject);
        return subject;
    }

    @Override
    public Subject update(Subject subject, Session session) throws RepositoryException {
        session.update(subject);
        return subject;
    }

    @Override
    public Subject delete(Subject subject, Session session) throws RepositoryException {
        session.delete(subject);
        return subject;
    }

    @Override
    public List<Subject> query(HibernateSpecification specification, Session session) throws RepositoryException {
        Criteria criteria =  session.createCriteria(Subject.class);
        Criterion criterion;
        if((specification != null) && ((criterion = specification.toCriteria()) != null)){
            criteria.add(criterion);
        }
        return criteria.list();
    }
}

