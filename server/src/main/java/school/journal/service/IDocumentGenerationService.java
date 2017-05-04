package school.journal.service;

import school.journal.service.exception.ServiceException;

import java.io.OutputStream;

public interface IDocumentGenerationService {

    OutputStream createPupilClassListWithFormerTeacherDocument(int classId) throws ServiceException;
    OutputStream createTeacherScheduleDocument(int teacherId) throws ServiceException;
    OutputStream createClassScheduleDocument(int classId) throws ServiceException;
    OutputStream createFullScheduleDocument() throws ServiceException;
    OutputStream createMarksDocument() throws ServiceException;

}
