package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import school.journal.entity.Teacher;
import school.journal.service.CRUDService;
import school.journal.service.ITeacherService;
import school.journal.service.exception.ServiceException;

import java.util.List;
import static school.journal.utils.ValidateServiceUtils.*;

@Component
public class TeacherService extends CRUDService<Teacher> implements ITeacherService {

    public TeacherService() {
        LOGGER = Logger.getLogger(TeacherService.class);
    }

    @Override

    public Teacher create(Teacher teacher) throws ServiceException {
        validateString(teacher.getLastName(), "Last Name");
        validateString(teacher.getFirstName(), "First Name");
        validateNullableString(teacher.getPathronymic(), "Patronymic");
        validateNullableId(teacher.getClassId(), "Class");
        validateNullableString(teacher.getDescription(), "Description");
        validate(teacher.getPhoneNumber());
        return super.create(teacher);
    }

    @Override
    public Teacher update(Teacher teacher) throws ServiceException {
        validateId(teacher.getTeacherId(),"Teacher");
        validateString(teacher.getLastName(), "Last Name");
        validateString(teacher.getFirstName(), "First Name");
        validateNullableString(teacher.getPathronymic(), "Patronymic");
        validateNullableId(teacher.getClassId(), "Class");
        validateNullableString(teacher.getDescription(), "Description");
        validate(teacher.getPhoneNumber());
        return super.update(teacher);
    }

    @Override
    public void delete(int id) throws ServiceException {
        validateId(id,"Teacher");
        Teacher teacher = new Teacher();
        teacher.setTeacherId(id);
        super.delete(teacher);
    }

    @Override
    public List<Teacher> read() throws ServiceException {
        return super.read();
    }
}
