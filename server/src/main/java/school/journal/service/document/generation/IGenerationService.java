package school.journal.service.document.generation;

import school.journal.service.exception.ServiceException;

import java.io.OutputStream;

public interface IGenerationService {
    OutputStream generateClassPupilListDocument(DocumentType documentType, int classId) throws ServiceException;
    OutputStream generateTeacherScheduleDocument(DocumentType documentType, int teacherId) throws ServiceException;
    OutputStream generateClassScheduleDocument(DocumentType documentType, int classId) throws ServiceException;
    OutputStream generateFullScheduleDocument(DocumentType documentType) throws ServiceException;
    OutputStream generateMarksDocument(DocumentType documentType, int subjectId, int classId) throws ServiceException;
}
