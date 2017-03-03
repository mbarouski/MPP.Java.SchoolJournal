package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import school.journal.entity.Teacher;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.service.ITeacherService;
import school.journal.service.ServiceAbstractClass;
import school.journal.service.exception.ServiceException;
import school.journal.utils.MobilePhoneValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TeacherService extends ServiceAbstractClass implements ITeacherService {
    private static final Logger LOGGER = Logger.getLogger(TeacherService.class);

    private Pattern LFP_PATTERN = Pattern.compile("[А-Я][а-я]+");

    @Autowired
    private IRepository<Teacher> teacherRepository;

    private void checkTeacherClass(Teacher teacher) throws ServiceException {
        int classId = teacher.getClassId();
        if(classId < 0) {
            throw new ServiceException("Class ID isn't correct.");
        }
    }

    private void checkMobilePhone(Teacher teacher) throws ServiceException {
        String phone = teacher.getPhoneNumber();
        if((phone != null) && (!MobilePhoneValidator.validate(phone))) {
            throw new ServiceException("Phone is not correct.");
        }
    }

    private void checkFirstName(Teacher teacher) throws ServiceException {
        String firstName = teacher.getFirstName();
        if(firstName != null) {
            if(firstName.isEmpty()) {
                throw new ServiceException("First name is empty");
            }
            if(!LFP_PATTERN.matcher(firstName).matches()) {
                throw new ServiceException("First name is not correct");
            }
        }
    }

    private void checkLastName(Teacher teacher) throws ServiceException {
        String lastName = teacher.getLastName();
        if(lastName != null) {
            if(lastName.isEmpty()) {
                throw new ServiceException("Last name is empty");
            }
            if(!LFP_PATTERN.matcher(lastName).matches()) {
                throw new ServiceException("Last name is not correct");
            }
        }
    }

    private void checkPatronymic(Teacher teacher) throws ServiceException {
        String patronymic = teacher.getPathronymic();
        if(patronymic != null) {
            if(patronymic.isEmpty()) {
                throw new ServiceException("Patronymic is empty");
            }
            if(!LFP_PATTERN.matcher(patronymic).matches()) {
                throw new ServiceException("Patronymic is not correct");
            }
        }
    }

    /**
     * Check last name, first name and patronymic
     */
    private void checkLFP(Teacher teacher) throws ServiceException {
        checkFirstName(teacher);
        checkLastName(teacher);
        checkPatronymic(teacher);
    }

    private void checkTeacherId(Teacher teacher) throws ServiceException {
        int teacherId = teacher.getTeacherId();
        if(teacherId <= 0) {
            throw new ServiceException("ID in not valid");
        }
    }

    @Override
    public Teacher create(Teacher teacher) throws ServiceException {

        checkTeacherClass(teacher);
        checkMobilePhone(teacher);
        checkLFP(teacher);

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            teacherRepository.create(teacher, session);
            session.getTransaction().commit();
        } catch (RepositoryException exc) {
            session.getTransaction().rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return teacher;
    }

    @Override
    public Teacher update(Teacher teacher) throws ServiceException {
        checkTeacherId(teacher);
        checkTeacherClass(teacher);
        checkMobilePhone(teacher);
        checkLFP(teacher);

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            teacherRepository.update(teacher, session);
            session.getTransaction().commit();
        } catch (RepositoryException exc) {
            session.getTransaction().rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return teacher;
    }

    @Override
    public void delete(int id) throws ServiceException {
        if(id <= 0) {
            throw new ServiceException("ID isn't valid");
        }

        Teacher teacher = new Teacher();
        teacher.setTeacherId(id);

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            teacherRepository.delete(teacher, session);
            session.getTransaction().commit();
        } catch (RepositoryException exc) {
            session.getTransaction().rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Teacher> read() throws ServiceException {
        List<Teacher> teachers;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            teachers = teacherRepository.query(null, session);
            session.getTransaction().commit();
        } catch (RepositoryException exc) {
            teachers = new ArrayList<>();
            session.getTransaction().rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return teachers;
    }
}
