package school.journal.service;

import school.journal.service.exception.ServiceException;

public interface IDocumentGenerationService {

    String createPupilClassListWithFormerTeacherDocument(int classId) throws ServiceException;
    String createTeacherScheduleDocument(int teacherId) throws ServiceException;
    String createClassScheduleDocument(int classId) throws ServiceException;
    String createFullScheduleDocument() throws ServiceException;
    String createMarksDocument() throws ServiceException;

}
