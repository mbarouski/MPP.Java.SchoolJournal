package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import school.journal.entity.Subject;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.service.CRUDService;
import school.journal.service.ISubjectService;
import school.journal.service.ServiceAbstractClass;
import school.journal.service.exception.ServiceException;
import static school.journal.utils.ValidateServiceUtils.*;

import java.util.List;

@Component
public class SubjectService extends CRUDService<Subject> implements ISubjectService {

    public SubjectService() {
        LOGGER = Logger.getLogger(SubjectService.class);
    }

    @Override
    public Subject create(Subject subject) throws ServiceException {
        validateString(subject.getName(), "Invalid name");
        validateString(subject.getDescription(), "Empty description of subject");
        return super.create(subject);
    }

    @Override
    public Subject update(Subject subject) throws ServiceException {
        validateId(subject.getSubjectId(), "Subject");
        validateString(subject.getName(), "Invalid name");
        validateString(subject.getDescription(), "Empty description of subject");
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
