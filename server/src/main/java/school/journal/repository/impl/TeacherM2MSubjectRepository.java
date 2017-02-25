package school.journal.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Component;
import school.journal.entity.Role;
import school.journal.entity.TeacherM2MSubject;
import school.journal.entity.User;
import school.journal.repository.IRepository;
import school.journal.repository.RepositoryAbstractClass;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.HibernateSpecification;

import java.util.List;
@Component
public class TeacherM2MSubjectRepository extends RepositoryAbstractClass<TeacherM2MSubject> {
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
        Criteria criteria =  session.createCriteria(TeacherM2MSubject.class);
        Criterion criterion;
        if((specification != null) && ((criterion = specification.toCriteria()) != null)){
            criteria.add(criterion);
        }
        return criteria.list();
    }
}
