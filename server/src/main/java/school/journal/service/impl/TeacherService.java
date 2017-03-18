package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school.journal.entity.Teacher;
import school.journal.repository.IRepository;
import school.journal.repository.impl.TeacherRepository;
import school.journal.service.CRUDService;
import school.journal.service.ITeacherService;
import school.journal.service.exception.ServiceException;
import school.journal.utils.exception.ValidationException;

import java.util.List;
import static school.journal.utils.ValidateServiceUtils.*;

@Component("TeacherService")
public class TeacherService extends CRUDService<Teacher> implements ITeacherService {

    @Autowired
    public TeacherService(@Qualifier("TeacherRepository") IRepository<Teacher> repository) {
        LOGGER = Logger.getLogger(TeacherService.class);
        this.repository = repository;
    }

    @Override

    public Teacher create(Teacher teacher) throws ServiceException {
        try {
            validateString(teacher.getLastName(), "Last Name");
            validateString(teacher.getFirstName(), "First Name");
            validateNullableString(teacher.getPathronymic(), "Patronymic");
            validateNullableId(teacher.getClassId(), "Class");
            validateNullableString(teacher.getDescription(), "Description");
            validatePhone(teacher.getPhoneNumber());
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        return super.create(teacher);
    }

    @Override
    public Teacher update(Teacher teacher) throws ServiceException {
        try {
            validateId(teacher.getTeacherId(),"Teacher");
            validateString(teacher.getLastName(), "Last Name");
            validateString(teacher.getFirstName(), "First Name");
            validateNullableString(teacher.getPathronymic(), "Patronymic");
            validateNullableId(teacher.getClassId(), "Class");
            validateNullableString(teacher.getDescription(), "Description");
            validatePhone(teacher.getPhoneNumber());
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        return super.update(teacher);
    }

    @Override
    public void delete(int id) throws ServiceException {
        try {
            validateId(id,"Teacher");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        Teacher teacher = new Teacher();
        teacher.setTeacherId(id);
        super.delete(teacher);
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
