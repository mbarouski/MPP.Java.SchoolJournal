package school.journal.service;

import school.journal.service.exception.ServiceException;

public interface ISVCService {

    String getClassWithFormerTeacher(int classId) throws ServiceException;
    String getTeacherSchedule(int teacherId) throws ServiceException;
    String getClassSchedule(int classId) throws ServiceException;
    String getFullSchedule() throws ServiceException;
    String getMarks() throws ServiceException;

}
