package school.journal.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import school.journal.entity.Clazz;
import school.journal.entity.Mark;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.impl.MarkRepository;
import school.journal.repository.specification.Specification;
import school.journal.repository.specification.mark.MarkSpecificationByMarkId;
import school.journal.service.IMarkService;
import school.journal.service.ServiceAbstractClass;
import school.journal.service.exception.ServiceException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MarkService extends ServiceAbstractClass implements IMarkService {
    @Autowired
    private IRepository<Mark> markIRepository;

    @Override
    public List<Mark> getMarks() throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Mark> markList = markIRepository.query(null, session);
            transaction.commit();
            return markList;
        } catch (RepositoryException e) {
            transaction.rollback();
            throw new ServiceException("Repository query exception", e);
        } finally {
            session.close();
        }
    }

    @Override
    public Mark createMark(Mark mark) throws ServiceException {
        if (mark.getValue() <= 0) {
            throw new ServiceException("Invalid value");
        } else {
            Calendar c = new GregorianCalendar();
            c.add(Calendar.DAY_OF_YEAR, 1);
            if (mark.getDate().after(c.getTime())) {
                throw new ServiceException("Invalid date");
            }
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            mark = markIRepository.create(mark, session);
            transaction.commit();
        } catch (RepositoryException e) {
            transaction.rollback();
            throw new ServiceException("Repository create exception", e);
        } finally {
            session.close();
        }
        return mark;
    }

    @Override
    public Mark updateMark(Mark mark) throws ServiceException {
        if (mark.getMarkId() <= 0) {
            throw new ServiceException("Invalid id");
        } else if (mark.getValue() <= 0) {
            throw new ServiceException("Invalid value");
        } else {
            Calendar c = new GregorianCalendar();
            c.add(Calendar.DAY_OF_YEAR, 1);
            if (mark.getDate().after(c.getTime())) {
                throw new ServiceException("Invalid date");
            }
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            mark = markIRepository.update(mark, session);
            transaction.commit();
        } catch (RepositoryException e) {
            transaction.rollback();
            throw new ServiceException("Repository update exception", e);
        } finally {
            session.close();
        }
        return mark;
    }

    @Override
    public Mark deleteMark(int markId) throws ServiceException {
        if (markId <= 0) {
            throw new ServiceException("Invalid id");
        }
        Mark mark = new Mark();
        mark.setMarkId(markId);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            mark = markIRepository.delete(mark, session);
            transaction.commit();
        } catch (RepositoryException e) {
            transaction.rollback();
            throw new ServiceException("Repository delete exception", e);
        } finally {
            session.close();
        }
        return mark;
    }

    @Override
    public Mark getOne(int markId) throws ServiceException {
        if (markId <= 0) {
            throw new ServiceException("Invalid id");
        }
        Mark mark = null;
        Specification<Mark> markSpecification = new MarkSpecificationByMarkId(markId);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Mark> markList = markIRepository.query(markSpecification, session);
            transaction.commit();
            if (markList.size() > 0) {
                mark = markList.get(0);
            }
            return mark;
        } catch (RepositoryException e) {
            transaction.rollback();
            throw new ServiceException("Repository query exception", e);
        } finally {
            session.close();
        }
    }
}
