package school.journal.service;

import school.journal.entity.Teacher;
import school.journal.service.exception.ServiceException;

import java.util.List;

public interface ITeacherService extends IService<Teacher> {
    List<Teacher> getListOfTeachersForClass(int classId) throws ServiceException;
    Teacher changeClassOfTeacher(int teacherId, int classId) throws ServiceException;
    Teacher changeDirectorOfStudies(int teacherId, boolean isDirector) throws ServiceException;
    Teacher getOne(int teacherId) throws ServiceException;
    Teacher getFormTeacher(int classId) throws ServiceException;
}
