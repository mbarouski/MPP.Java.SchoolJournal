package school.journal.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import school.journal.entity.Role;
import school.journal.entity.SubjectInSchedule;
import school.journal.entity.User;
import school.journal.repository.IRepository;
import school.journal.repository.RepositoryAbstractClass;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.HibernateSpecification;

import java.util.List;

public class SubjectInScheduleRepository extends RepositoryAbstractClass<SubjectInSchedule> {
    private static final SubjectInScheduleRepository instance = new SubjectInScheduleRepository();

    public static SubjectInScheduleRepository getInstance() {
        return instance;
    }

    private SubjectInScheduleRepository() {
    }

    @Override
    public SubjectInSchedule create(SubjectInSchedule subjectInSchedule, Session session) throws RepositoryException {
        session.save(subjectInSchedule);
        return subjectInSchedule;
    }

    @Override
    public SubjectInSchedule update(SubjectInSchedule subjectInSchedule, Session session) throws RepositoryException {
        session.update(subjectInSchedule);
        return subjectInSchedule;
    }

    @Override
    public SubjectInSchedule delete(SubjectInSchedule subjectInSchedule, Session session) throws RepositoryException {
        session.delete(subjectInSchedule);
        return subjectInSchedule;
    }

    @Override
    public List<SubjectInSchedule> query(HibernateSpecification specification, Session session) throws RepositoryException {
        Criteria criteria = session.createCriteria(SubjectInSchedule.class);
        Criterion criterion = specification.toCriteria();
        if (criterion!=null){
            criteria.add(criterion);
        }
        return criteria.list();
    }
}
