package school.journal.service.impl;

import school.journal.entity.Teacher;
import school.journal.service.CRUDService;
import school.journal.service.ITeacherService;
import school.journal.service.exception.ServiceException;

import java.util.List;

public class TeacherService extends CRUDService<Teacher> implements ITeacherService {
    //TODO CHECK Correctness of fields

    @Override
    public Teacher create(Teacher obj) throws ServiceException {
        return super.create(obj);
    }

    @Override
    public Teacher update(Teacher obj) throws ServiceException {
        return super.update(obj);
    }

    @Override
    public void delete(int id) throws ServiceException {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(id);
        super.delete(teacher);
    }

    @Override
    public List<Teacher> read() throws ServiceException {
        return super.read();
    }
}
