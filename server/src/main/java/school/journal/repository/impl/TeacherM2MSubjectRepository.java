package school.journal.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import school.journal.entity.TeacherM2MSubject;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.HibernateSpecification;

import java.util.List;

public class TeacherM2MSubjectRepository implements IRepository<TeacherM2MSubject> {
    @Override
    public TeacherM2MSubject create(TeacherM2MSubject teacherM2MSubject, Session session) throws RepositoryException {
        session.save(teacherM2MSubject);
        return teacherM2MSubject;
    }

    @Override
    public TeacherM2MSubject update(TeacherM2MSubject teacherM2MSubject, Session session) throws RepositoryException {
        session.update(teacherM2MSubject);
        return teacherM2MSubject;
    }

    @Override
    public TeacherM2MSubject delete(TeacherM2MSubject teacherM2MSubject, Session session) throws RepositoryException {
        session.delete(teacherM2MSubject);
        return teacherM2MSubject;
    }

    @Override
    public List<TeacherM2MSubject> query(HibernateSpecification specification, Session session) throws RepositoryException {
        Criteria criteria = session.createCriteria(TeacherM2MSubject.class);
        Criterion criterion = specification.toCriteria();
        if (criteria!=null){
            criteria.add(criterion);
        }
        return criteria.list();
    }
}