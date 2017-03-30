package school.journal.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Component;
import school.journal.entity.Mark;
import school.journal.repository.RepositoryAbstractClass;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.HibernateSpecification;

import java.util.List;
@Component("MarkRepository")
public class MarkRepository extends RepositoryAbstractClass<Mark> {
    @Override
    public Mark create(Mark mark, Session session) throws RepositoryException {
        return (Mark)session.save(mark);
    }

    @Override
    public Mark update(Mark mark, Session session) throws RepositoryException {
//        session.update(mark);
        return (Mark)session.merge(mark);
    }

    @Override
    public Mark delete(Mark mark, Session session) throws RepositoryException {
        session.delete(mark);
        return null;
    }

    @Override
    public List<Mark> query(HibernateSpecification specification, Session session) throws RepositoryException {
        Criteria criteria =  session.createCriteria(Mark.class);
        Criterion criterion;
        if((specification != null) && ((criterion = specification.toCriteria()) != null)){
            criteria.add(criterion);
        }
        return criteria.list();
    }

    @Override
    public Mark get(int id, Session session) throws RepositoryException {
        Mark mark = (Mark) session.get(Mark.class, id);
        if (mark == null) throw new RepositoryException("Mark not found");
        return mark;
    }
}
