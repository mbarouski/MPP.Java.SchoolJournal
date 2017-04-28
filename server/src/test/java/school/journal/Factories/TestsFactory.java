package school.journal.Factories;

import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.mockito.Mockito;
import school.journal.repository.IRepository;

import java.util.List;

import static org.mockito.Mockito.*;

public class TestsFactory<T> extends BaseFactory {

    final Class<T> typeParameterClass;

    public  TestsFactory(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }
    @Override
    public SessionFactory createMockSessionFactory(Session session) {
        SessionFactory result = createSessionFactory();
        when(result.openSession()).thenReturn((org.hibernate.classic.Session) session);
        return result;
    }

    @Override
    public Session createMockSession(){
        Session result = Mockito.mock(Session.class);
        when(result.beginTransaction()).thenReturn(Mockito.mock(Transaction.class));
        return result;
    }

    @Override
    public Session createMockSessionWithException() {
        Session session = createMockSession();
        doThrow(HibernateException.class).when(session).get(any(Class.class),  anyInt());
        doThrow(HibernateException.class).when(session).save(any(Object.class));
        doThrow(HibernateException.class).when(session).update(any(Object.class));
        doThrow(HibernateException.class).when(session).delete(any(Object.class));
        doThrow(HibernateException.class).when(session).createCriteria(any(Class.class));
        doThrow(HibernateException.class).when(session).merge(any(Object.class));
        return session;
    }

    @Override
    public Session createMockSessionWithGetAll(List expectedObjects) {
        Session session = createMockSession();
        Criteria mockCriteria = Mockito.mock(Criteria.class);
        when(mockCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)).thenReturn(mockCriteria);
        when(mockCriteria.list()).thenReturn(expectedObjects);
        when(session.createCriteria(any(Class.class))).thenReturn(mockCriteria);
        when(mockCriteria.addOrder(any(Order.class))).thenReturn(mockCriteria);
        return session;
    }

    @Override
    public Session createMockSessionWithGet(Object returnedObject) {
        Session session = createMockSession();
        when(session.get(any(Class.class),  anyInt())).thenReturn(returnedObject);
        return session;
    }

    @Override
    public Session createMockSessionWithSave() {
        Session session = createMockSession();
        when(session.save(typeParameterClass)).thenReturn(anyInt());
        return session;
    }

    @Override
    public Session createMockSessionWithUpdate() {
        Session session = createMockSession();
        //when(session.update(typeParameterClass)).thenReturn();
        return session;
    }

    @Override
    public Session createMockSessionWithGetByName(Object deletedObject, String name) {
        Session session = createMockSession();
        Criteria mockCriteria = Mockito.mock(Criteria.class);
        when(mockCriteria.add(Restrictions.eq(name, anyString()))).thenReturn(mockCriteria);
        when(mockCriteria.uniqueResult()).thenReturn(deletedObject);
        when(session.createCriteria(typeParameterClass)).thenReturn(mockCriteria);
        return session;
    }

    @Override
    public IRepository createMockContactDataRepository() {
        return Mockito.mock(IRepository.class);
    }

    private SessionFactory createSessionFactory(){
        return Mockito.mock(SessionFactory.class);
    }
}
