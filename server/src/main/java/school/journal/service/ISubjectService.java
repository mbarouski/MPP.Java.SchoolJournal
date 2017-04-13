package school.journal.service;

import school.journal.entity.Subject;
import school.journal.service.exception.ServiceException;

public interface ISubjectService extends IService<Subject> {
    Subject getOne(int subjectId) throws ServiceException;
}
