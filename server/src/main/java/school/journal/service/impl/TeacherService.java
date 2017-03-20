package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school.journal.entity.Clazz;
import school.journal.entity.Teacher;
import school.journal.entity.User;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.service.CRUDService;
import school.journal.service.ITeacherService;
import school.journal.service.exception.ServiceException;
import school.journal.utils.exception.ValidationException;

import java.util.List;
import static school.journal.utils.ValidateServiceUtils.*;

@Component("TeacherService")
public class TeacherService extends CRUDService<Teacher> implements ITeacherService {

    private IRepository<User> userRepository;
    private IRepository<Clazz> classRepository;

    @Autowired
    public TeacherService(@Qualifier("TeacherRepository") IRepository<Teacher> repository,
                          @Qualifier("UserRepository") IRepository<User> userRepository,
                          @Qualifier("ClassRepository") IRepository<Clazz> classRepository) {
        LOGGER = Logger.getLogger(TeacherService.class);
        this.repository = repository;
        this.userRepository = userRepository;
        this.classRepository = classRepository;
    }

    private User checkUser(Integer id, Session session) throws ServiceException{
        if(id == null) throw new ServiceException("User id is incorrect");
        try {
            return userRepository.get(id, session);
        } catch (RepositoryException exc ) {
            throw new ServiceException("User is not found");
        }
    }

    @Override
    public Teacher create(Teacher teacher) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            teacher.setUser(checkUser(teacher.getUserId(), session));
            validateString(teacher.getLastName(), "Last Name");
            validateString(teacher.getFirstName(), "First Name");
            validateNullableString(teacher.getPathronymic(), "Patronymic");
            validateNullableId(teacher.getClassId(), "Class");
            validateNullableString(teacher.getDescription(), "Description");
            validatePhone(teacher.getPhoneNumber());
            repository.create(teacher, session);
            transaction.commit();
        } catch (ValidationException | ObjectNotFoundException | RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            if(session != null) {
                session.close();
            }
        }
        return teacher;
    }

    private int checkId(Integer id) throws ServiceException {
        if(id == null) throw new ServiceException("Incorrect user id");
        return id.intValue();
    }

    private void checkLastName(Teacher newTeacher, Teacher teacher) throws ValidationException {
        String lastName = newTeacher.getLastName();
        if(lastName == null) return;
        validateString(lastName, "Last Name");
        teacher.setLastName(lastName);
    }

    private void checkFirstName(Teacher newTeacher, Teacher teacher) throws ValidationException {
        String firstName = newTeacher.getFirstName();
        if(firstName == null) return;
        validateString(firstName, "First Name");
        teacher.setFirstName(firstName);
    }

    private void checkPatronymic(Teacher newTeacher, Teacher teacher) throws ValidationException {
        String patronymic = newTeacher.getFirstName();
        if(patronymic == null) return;
        validateString(patronymic, "Patronymic");
        teacher.setPathronymic(patronymic);
    }

    private void checkDescription(Teacher newTeacher, Teacher teacher) {
        String description = newTeacher.getDescription();
        if(description == null) return;
        teacher.setDescription(description);
    }

    private void checkClassId(Teacher newTeacher, Teacher teacher, Session session) throws ValidationException {
        Integer classId = newTeacher.getClassId();
        if(classId == null) return;
        try {
            classRepository.get(classId, session);
            teacher.setClassId(classId);
        } catch (RepositoryException exc) {
            throw new ValidationException(exc);
        }
    }

    private void checkPhone(Teacher newTeacher, Teacher teacher) throws ValidationException {
        String phone = newTeacher.getPhoneNumber();
        if(phone == null) return;
        validatePhone(phone);
        teacher.setPhoneNumber(phone);
    }

    @Override
    public Teacher update(Teacher newTeacher) throws ServiceException {
        Teacher teacher;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            teacher = repository.get(checkId(newTeacher.getUserId()), session);
            checkLastName(newTeacher, teacher);
            checkFirstName(newTeacher, teacher);
            checkPatronymic(newTeacher, teacher);
            checkClassId(newTeacher, teacher, session);
            checkDescription(newTeacher, teacher);
            checkPhone(newTeacher, teacher);
            repository.update(teacher, session);
            transaction.commit();
        } catch (ValidationException | RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            if(session != null) {
                session.close();
            }
        }
        return teacher;
    }

    @Override
    public void delete(int id) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Teacher teacher = (Teacher)session.load(Teacher.class, id);
            repository.delete(teacher, session);
            transaction.commit();
        } catch (ObjectNotFoundException | RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            if(session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Teacher> read() throws ServiceException {
        return super.read();
    }

    @Override
    public List<Teacher> getListOfTeachersForClass(int classId) throws ServiceException {
        return null;
    }

    @Override
    public List<Teacher> getListOfAllTeachers() throws ServiceException {
        return null;
    }

    @Override
    public Teacher attachTeacherToClass(int teacherId, int classId) throws ServiceException {
        return null;
    }

    @Override
    public Teacher detachTeacherFromClass(int teacherId) throws ServiceException {
        return null;
    }

    @Override
    public Teacher makeDirectorOfStudies(int teacherId) throws ServiceException {
        return null;
    }

    @Override
    public Teacher unmakeDirectorOfStudies(int teacherId) throws ServiceException {
        return null;
    }
}
