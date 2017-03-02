package school.journal.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import school.journal.entity.Clazz;
import school.journal.entity.Pupil;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.pupil.PupilSpecification;
import school.journal.repository.specification.pupil.PupilSpecificationByClassId;
import school.journal.repository.specification.pupil.PupilSpecificationByPupilId;
import school.journal.service.IPupilService;
import school.journal.service.ServiceAbstractClass;
import school.journal.service.exception.ServiceException;

import java.util.List;

@Component
public class PupilService extends ServiceAbstractClass implements IPupilService {
    @Autowired
    private IRepository<Pupil> pupilIRepository;

    @Override
    public List<Pupil> getPupils() throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Pupil> pupilList = pupilIRepository.query(null, session);
            transaction.commit();
            return pupilList;
        } catch (RepositoryException exc) {
            transaction.rollback();
            throw new ServiceException("Repository query exception", exc);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Pupil> getListOfPupils(int clazzId) throws ServiceException {
        if (clazzId <= 0) {
            throw new ServiceException("Invalid id");
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Pupil> pupilList = pupilIRepository.query(
                    new PupilSpecificationByClassId(clazzId), session);
            transaction.commit();
            return pupilList;
        } catch (RepositoryException e) {
            transaction.rollback();
            throw new ServiceException("Repository query exception", e);
        } finally {
            session.close();
        }
    }

    @Override
    public Pupil createPupil(Pupil pupil) throws ServiceException {
        if (pupil.getLastName().isEmpty()) {
            throw new ServiceException("Invalid Last Name");
        } else if (pupil.getFirstName().isEmpty()) {
            throw new ServiceException("Invalid First Name");
        } else if (pupil.getPathronymic().isEmpty()) {
            throw new ServiceException("Invalid Patronymic");
        } else if (pupil.getClazzByClassId() == null) {
            throw new ServiceException("Class For Pupil Was Not Set");
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            pupil = pupilIRepository.create(pupil, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            throw new ServiceException("Repository create exception", exc);
        } finally {
            session.close();
        }
        return pupil;
    }

    @Override
    public Pupil updatePupil(Pupil pupil) throws ServiceException {
        if (pupil.getLastName().isEmpty()) {
            throw new ServiceException("Invalid Last Name");
        } else if (pupil.getFirstName().isEmpty()) {
            throw new ServiceException("Invalid First Name");
        } else if (pupil.getPathronymic().isEmpty()) {
            throw new ServiceException("Invalid Patronymic");
        } else if (pupil.getClazzByClassId() == null) {
            throw new ServiceException("Class For Pupil Was Not Set");
        } else if (pupil.getPupilId() <= 0) {
            throw new ServiceException("Invalid id");
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            pupil = pupilIRepository.update(pupil, session);
            transaction.commit();
            return pupil;
        } catch (RepositoryException exc) {
            transaction.rollback();
            throw new ServiceException("Repository update exception", exc);
        } finally {
            session.close();
        }
    }

    @Override
    public Pupil movePupulToAnotherClass(Pupil pupil, Clazz clazz) throws ServiceException {
        if (clazz.getClassId() <= 0) {
            throw new ServiceException("Invalid class id");
        }
        pupil.setClazzByClassId(clazz);
        return updatePupil(pupil);
    }

    @Override
    public Pupil deletePupil(int pupilId) throws ServiceException {
        if (pupilId <= 0) {
            throw new ServiceException("Invalid id");
        }
        Pupil pupil = new Pupil();
        pupil.setPupilId(pupilId);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            pupil = pupilIRepository.delete(pupil, session);
        } catch (RepositoryException exc) {
            transaction.rollback();
            throw new ServiceException("Repository delete exception", exc);
        } finally {
            session.close();
        }
        return pupil;
    }

    @Override
    public Pupil getOne(int pupilId) throws ServiceException {
        if (pupilId <= 0) {
            throw new ServiceException("Invalid id");
        }
        PupilSpecification specification = new PupilSpecificationByPupilId(pupilId);
        Pupil pupil = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Pupil> pupilList = pupilIRepository.query(specification, session);
            if (pupilList.size() > 0) {
                pupil = pupilList.get(0);
            }
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            throw new ServiceException("Repository query exception", exc);
        } finally {
            session.close();
        }
        return pupil;
    }

}
