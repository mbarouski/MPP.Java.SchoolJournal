package school.journal.service;

import org.hibernate.SessionFactory;
import school.journal.persistence.HibernateUtil;

public abstract class ServiceAbstractClass {
    protected static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
}
