package school.journal;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import school.journal.Factories.TestsFactory;
import school.journal.entity.Clazz;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.impl.ClazzRepository;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class ClazzRepositoryTest {

    private final TestsFactory factory = new TestsFactory(Clazz.class);

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getAll_default_returnObject() throws Exception{
        ArrayList<Clazz> expectedObjects = new ArrayList<>();
        expectedObjects.add(new Clazz());
        expectedObjects.add(new Clazz());

        ClazzRepository testedRepository = factory.createClazzRepository();
        Session mockSession = factory.createMockSessionWithGetAll(expectedObjects);

        assertEquals(testedRepository.query(null,mockSession),expectedObjects);
    }

    @Test
    public void getAll_errorInTransaction_throwHibernateException() throws Exception{
        ClazzRepository testedRepository = factory.createClazzRepository();
        Session sessionWithException = factory.createMockSessionWithException();
        exception.expect(HibernateException.class);
        testedRepository.query(null,sessionWithException);
    }

    @Test
    public void getById_idIsGreaterThanZero_returnObject()throws Exception{
        Clazz expectedObject = new Clazz();
        ClazzRepository testedRepository = factory.createClazzRepository();
        Session mockSession = factory.createMockSessionWithGet(expectedObject);

        assertEquals(testedRepository.get(1,mockSession),expectedObject);
    }

    @Test
    public void getById_errorInTransaction_throwHibernateException() throws Exception{
        ClazzRepository testedRepository = factory.createClazzRepository();
        Session sessionWithException = factory.createMockSessionWithException();
        exception.expect(HibernateException.class);

        testedRepository.get(1,sessionWithException);
    }

    @Test
    public void getById_noSuchElement_throwRepositoryException() throws Exception{
        ClazzRepository testedRepository = factory.createClazzRepository();
        Session mockSession = factory.createMockSession();

        exception.expect(RepositoryException.class);

        testedRepository.get(1,mockSession);
    }



    @Test
    public void create_errorInTransaction_throwHibernateException() throws  Exception{
        ClazzRepository testedRepository = factory.createClazzRepository();
        Session mockSession = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.create(new Clazz(),mockSession);
    }

    @Test
    public void create_CreatedEntityNotNull_AddObject() throws Exception{
        Clazz createdObject = new Clazz();
        Session mockSession = factory.createMockSessionWithSave();
        ClazzRepository testedRepository = factory.createClazzRepository();

        testedRepository.create(createdObject,mockSession);

        verify(mockSession).save(createdObject);
    }

    @Test
    public void create_CreatedEntityNotNull_ReturnEntity() throws Exception{
        Clazz expectedClazz = new Clazz();
        Session mockSession = factory.createMockSessionWithSave();
        ClazzRepository testedRepository = factory.createClazzRepository();

        Clazz returnedClazz = testedRepository.create(expectedClazz,mockSession);

        assertEquals(expectedClazz, returnedClazz);
    }

    @Test
    public void update_errorInTransaction_throwHibernateException()throws Exception{
        ClazzRepository testedRepository = factory.createClazzRepository();
        Session mockSession = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.update(new Clazz(),mockSession);
    }

    @Test
    public void update_UpdatedEntityNotNull_UpdateObject() throws Exception{
        Clazz updatedObject = new Clazz();
        Session mockSession = factory.createMockSession();
        ClazzRepository testedRepository = factory.createClazzRepository();

        testedRepository.update(updatedObject,mockSession);

        verify(mockSession).merge(updatedObject);
    }

    @Test
    public void delete_errorInTransaction_throwHibernateException() throws Exception{
        ClazzRepository testedRepository = factory.createClazzRepository();
        Session sessionWithException = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.delete(new Clazz(),sessionWithException);
    }

    @Test
    public void delete_validObject_deleteObject() throws Exception{
        Clazz deletedObject = new Clazz();
        ClazzRepository testedRepository = factory.createClazzRepository();
        Session mockSession = factory.createMockSessionWithGet(deletedObject);

        testedRepository.delete(deletedObject,mockSession);

        verify(mockSession).delete(deletedObject);
    }
}
