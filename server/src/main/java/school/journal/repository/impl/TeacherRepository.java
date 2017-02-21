package school.journal.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import school.journal.entity.Role;
import school.journal.entity.Teacher;
import school.journal.entity.User;
import school.journal.repository.IRepository;
import school.journal.repository.RepositoryAbstractClass;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.HibernateSpecification;

import java.util.List;

public class TeacherRepository extends RepositoryAbstractClass<Teacher> {
    private static final TeacherRepository instance = new TeacherRepository();

    private TeacherRepository() {
    }

    public static TeacherRepository getInstance() {
        return instance;
    }

    public List<Teacher> read() throws RepositoryException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Teacher> teachers = (List<Teacher>) session.createCriteria(Role.class).list();
        session.getTransaction().commit();
        session.close();
        return teachers;
    }

    @Override
    public Teacher create(Teacher teacher, Session session) throws RepositoryException {
        session.save(teacher);
        return teacher;
    }

    @Override
    public Teacher update(Teacher teacher, Session session) throws RepositoryException {
        session.update(teacher);
        return teacher;
    }

    @Override
    public Teacher delete(Teacher teacher, Session session) throws RepositoryException {
        session.delete(teacher);
        return teacher;
    }

    @Override
    public List<Teacher> query(HibernateSpecification specification, Session session) throws RepositoryException {
        Criteria criteria = session.createCriteria(Teacher.class);
        Criterion criterion = specification.toCriteria();
        if (criterion != null) {
            criteria.add(criterion);
        }
        return criteria.list();
    }
}
