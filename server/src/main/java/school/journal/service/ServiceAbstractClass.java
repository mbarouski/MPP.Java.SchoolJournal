package school.journal.service;

import org.hibernate.SessionFactory;
import school.journal.persistence.HibernateUtil;

public abstract class ServiceAbstractClass {
    protected SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
}
