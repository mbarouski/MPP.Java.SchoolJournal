package school.journal.service;

import school.journal.entity.Teacher;
import school.journal.service.exception.ServiceException;

import java.util.List;

public interface ITeacherService extends IService<Teacher> {
    List<Teacher> getListOfTeachersForClass(int classId) throws ServiceException;
    List<Teacher> getListOfAllTeachers() throws ServiceException;
    Teacher attachTeacherToClass(int teacherId, int classId) throws ServiceException;
    Teacher detachTeacherFromClass(int teacherId) throws ServiceException;
    Teacher makeDirectorOfStudies(int teacherId) throws ServiceException;
    Teacher unmakeDirectorOfStudies(int teacherId) throws ServiceException;
}
