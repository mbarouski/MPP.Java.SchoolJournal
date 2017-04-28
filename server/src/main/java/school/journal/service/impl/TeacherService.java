package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import school.journal.controller.util.ExceptionEnum;
import school.journal.entity.Clazz;
import school.journal.entity.Role;
import school.journal.entity.Teacher;
import school.journal.entity.User;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.service.CRUDService;
import school.journal.service.ITeacherService;
import school.journal.service.exception.ClassifiedServiceException;
import school.journal.service.exception.ServiceException;
import school.journal.utils.exception.ValidationException;

import java.text.MessageFormat;
import java.util.List;

import static school.journal.entity.enums.RoleEnum.DIRECTOR_OF_STUDIES;
import static school.journal.entity.enums.RoleEnum.TEACHER;
import static school.journal.utils.ValidateServiceUtils.*;

@Service("TeacherService")
public class TeacherService extends CRUDService<Teacher> implements ITeacherService {
    private static final String SQL_QUERY_FOR_GET_TEACHERS_FOR_CLASS = "SELECT `teacher`.`teacher_id`, `teacher`.`phone_number`, " +
            "`teacher`.`class_id`, `teacher`.`first_name`, `teacher`.`last_name`, `teacher`.`pathronymic`, `teacher`.`description` " +
            "FROM `teacher` " +
            "JOIN `subject_in_schedule` " +
            "ON `teacher`.`teacher_id` = `subject_in_schedule`.`teacher_id` " +
            "WHERE `subject_in_schedule`.`class_id` = {0};";

    private static final String SQL_QUERY_FOR_GET_HEAD_CLASS_TEACHER = "SELECT `teacher`.`teacher_id`, `teacher`.`phone_number`, " +
            "`teacher`.`class_id`, `teacher`.`first_name`, `teacher`.`last_name`, `teacher`.`pathronymic`, `teacher`.`description` " +
            "FROM `teacher` " +
            "WHERE `teacher`.`class_id` = {0}";

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

    private User checkUser(Integer id, Session session) throws ServiceException {
        if (id == null) throw new ServiceException("User id is incorrect");
        try {
            return userRepository.get(id, session);
        } catch (RepositoryException exc) {
            throw new ClassifiedServiceException(ExceptionEnum.teacher_not_found);
        }
    }

    @Override
    public Teacher create(Teacher teacher) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        teacher.setUser(checkUser(teacher.getUserId(), session));
        try {
            validateString(teacher.getLastName(), "Last Name");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ClassifiedServiceException(ExceptionEnum.teacher_has_wrong_last_name);
        }
        try {
            validateString(teacher.getFirstName(), "First Name");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ClassifiedServiceException(ExceptionEnum.teacher_has_wrong_first_name);
        }
        try {
            validateNullableString(teacher.getPathronymic(), "Patronymic");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ClassifiedServiceException(ExceptionEnum.teacher_has_wrong_patronymic);
        }
        try {
            validateNullableId(teacher.getClassId(), "Class");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ClassifiedServiceException(ExceptionEnum.teacher_has_wrong_class);
        }
        try {
            validateNullableString(teacher.getDescription(), "Description");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ClassifiedServiceException(ExceptionEnum.teacher_has_wrong_characteristic);
        }
        try {
            validatePhone(teacher.getPhoneNumber());
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ClassifiedServiceException(ExceptionEnum.teacher_has_wrong_phone);
        }
        try {
            repository.create(teacher, session);
            transaction.commit();
        } catch (ObjectNotFoundException | RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return teacher;
    }

    private int checkId(Integer id) throws ServiceException {
        if (id == null) {
            throw new ClassifiedServiceException(ExceptionEnum.wrong_id_number);
        }
        return id.intValue();
    }

    private void checkLastName(Teacher newTeacher, Teacher teacher) throws ValidationException {
        String lastName = newTeacher.getLastName();
        if (lastName == null) return;
        validateString(lastName, "Last Name");
        teacher.setLastName(lastName);
    }

    private void checkFirstName(Teacher newTeacher, Teacher teacher) throws ValidationException {
        String firstName = newTeacher.getFirstName();
        if (firstName == null) return;
        validateString(firstName, "First Name");
        teacher.setFirstName(firstName);
    }

    private void checkPatronymic(Teacher newTeacher, Teacher teacher) throws ValidationException {
        String patronymic = newTeacher.getPathronymic();
        if (patronymic == null) return;
        validateString(patronymic, "Patronymic");
        teacher.setPathronymic(patronymic);
    }

    private void checkDescription(Teacher newTeacher, Teacher teacher) {
        String description = newTeacher.getDescription();
        if (description == null) return;
        teacher.setDescription(description);
    }

    private void checkClassId(Teacher newTeacher, Teacher teacher, Session session) throws ValidationException {
        Integer classId = newTeacher.getClassId();
        if (classId == null) return;
        try {
            classRepository.get(classId, session);
            teacher.setClassId(classId);
        } catch (RepositoryException exc) {
            throw new ValidationException(exc);
        }
    }

    private void checkPhone(Teacher newTeacher, Teacher teacher) throws ValidationException {
        String phone = newTeacher.getPhoneNumber();
        if (phone == null) return;
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
            session.close();
        }
        return teacher;
    }

    @Override
    public void delete(int id) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Teacher teacher = (Teacher) session.load(Teacher.class, id);
            repository.delete(teacher, session);
            transaction.commit();
        } catch (ObjectNotFoundException | RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Teacher getOne(int teacherId) throws ServiceException {
        Teacher teacher;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            teacher = repository.get(teacherId, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return teacher;
    }

    @Override
    public List<Teacher> read() throws ServiceException {
        return super.read();
    }

    @Override
    public List<Teacher> getListOfTeachersForClass(int classId) throws ServiceException {
        Session session = sessionFactory.openSession();
        List<Teacher> teachers;
        teachers = session.createSQLQuery(MessageFormat.format(SQL_QUERY_FOR_GET_TEACHERS_FOR_CLASS, classId)).addEntity(Teacher.class).list();
        return teachers;
    }

    @Override
    public Teacher changeClassOfTeacher(int teacherId, int classId) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Teacher teacher;
        try {
            teacher = (Teacher) session.get(Teacher.class, teacherId);
            if (teacher == null) {
                throw new ClassifiedServiceException(ExceptionEnum.teacher_not_found);
            }
            if (classId == 0) {
                teacher.setClassId(null);
            } else {
                if (session.get(Clazz.class, classId) == null) {
                    throw new ClassifiedServiceException(ExceptionEnum.teacher_has_wrong_class);
                }
            }
            teacher.setClassId(classId);
            List l = session.createSQLQuery(MessageFormat.format(SQL_QUERY_FOR_GET_HEAD_CLASS_TEACHER, classId)).addEntity(Teacher.class).list();
            if (l != null && l.size() != 0) {
                Teacher oldClassTeacher = (Teacher)l.get(0);
                oldClassTeacher.setClassId(null);
                repository.update(oldClassTeacher,session);
            }
            repository.update(teacher, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return teacher;
    }

    @Override
    public Teacher changeDirectorOfStudies(int teacherId, boolean isDirector) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Teacher teacher;
        try {
            teacher = (Teacher) session.get(Teacher.class, teacherId);
            if (teacher == null) {
                throw new ClassifiedServiceException(ExceptionEnum.teacher_not_found);
            }
            Role role = (Role) session.get(Role.class, (isDirector ? DIRECTOR_OF_STUDIES : TEACHER).getValue());
            if (role == null) {
                throw new ClassifiedServiceException(ExceptionEnum.role_not_found);
            }
            teacher.getUser().setRole(role);
            repository.update(teacher, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return teacher;
    }
}
