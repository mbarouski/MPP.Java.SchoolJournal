package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import school.journal.entity.Subject;
import school.journal.service.CRUDService;
import school.journal.service.ISubjectService;
import school.journal.service.exception.ServiceException;

import java.util.List;

import static school.journal.utils.ValidateServiceUtils.*;

@Component
public class SubjectService extends CRUDService<Subject> implements ISubjectService {

    public SubjectService() {
        LOGGER = Logger.getLogger(SubjectService.class);
    }

    @Override
    public Subject create(Subject subject) throws ServiceException {
        validateString(subject.getName(), "Name");
        validateString(subject.getDescription(), "Description");
        return super.create(subject);
    }

    @Override
    public Subject update(Subject subject) throws ServiceException {
        validateId(subject.getSubjectId(), "Subject");
        validateString(subject.getName(), "Name");
        validateString(subject.getDescription(), "Description");
        return super.update(subject);
    }

    @Override
    public void delete(int subjectId) throws ServiceException {
        validateId(subjectId, "Subject");
        Subject subject = new Subject();
        subject.setSubjectId(subjectId);
        super.delete(subject);
    }

    @Override
    public List<Subject> read() throws ServiceException {
        return super.read();
    }
}
