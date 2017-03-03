package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import school.journal.entity.Subject;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.service.ISubjectService;
import school.journal.service.ServiceAbstractClass;
import school.journal.service.exception.ServiceException;
import static school.journal.utils.ValidateServiceUtils.*;

import java.util.List;

@Component
public class SubjectService extends ServiceAbstractClass implements ISubjectService {

    private static final Logger LOGGER = Logger.getLogger(SubjectService.class);

    @Autowired
    private IRepository<Subject> subjectRepository;

    @Override
    public Subject create(Subject subject) throws ServiceException {

        validateString(subject.getName(),"Invalid name");
        validateString(subject.getDescription(),"Empty description of subject");

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            subjectRepository.create(subject, session);
            session.getTransaction().commit();
        } catch (RepositoryException exc) {
            session.getTransaction().rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }

        return subject;
    }

    @Override
    public Subject update(Subject subject) throws ServiceException {

        validateId(subject.getSubjectId(),"Subject");
        validateString(subject.getName(),"Invalid name");
        validateString(subject.getDescription(),"Empty description of subject");

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            subjectRepository.update(subject, session);session.getTransaction().commit();
        } catch (RepositoryException exc) {
            session.getTransaction().rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }

        return subject;
    }

    @Override
    public void delete(int subjectId) throws ServiceException {

        validateId(subjectId,"Subject");

        Subject subject = new Subject();
        subject.setSubjectId(subjectId);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            subjectRepository.delete(subject, session);
            session.getTransaction().commit();
        } catch (RepositoryException exc) {
            session.getTransaction().rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Subject> read() throws ServiceException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            return subjectRepository.query(null, session);
        } catch (RepositoryException exc){
            LOGGER.error(exc);
            throw new ServiceException();
        } finally {
            session.close();
        }
    }
}
