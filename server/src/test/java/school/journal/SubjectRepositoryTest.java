package school.journal;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import school.journal.Factories.TestsFactory;
import school.journal.entity.Subject;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.impl.SubjectRepository;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class SubjectRepositoryTest {
    private final TestsFactory factory = new TestsFactory(Subject.class);

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getAll_default_returnObject() throws Exception{
        ArrayList<Subject> expectedObjects = new ArrayList<>();
        expectedObjects.add(new Subject());
        expectedObjects.add(new Subject());

        SubjectRepository testedRepository = factory.createSubjectRepository();
        Session mockSession = factory.createMockSessionWithGetAll(expectedObjects);

        assertEquals(testedRepository.query(null,mockSession),expectedObjects);
    }

    @Test
    public void getAll_errorInTransaction_throwHibernateException() throws Exception{
        SubjectRepository testedRepository = factory.createSubjectRepository();
        Session sessionWithException = factory.createMockSessionWithException();
        exception.expect(HibernateException.class);
        testedRepository.query(null,sessionWithException);
    }

    @Test
    public void getById_idIsGreaterThanZero_returnObject()throws Exception{
        Subject expectedObject = new Subject();
        SubjectRepository testedRepository = factory.createSubjectRepository();
        Session mockSession = factory.createMockSessionWithGet(expectedObject);

        assertEquals(testedRepository.get(1,mockSession),expectedObject);
    }

    @Test
    public void getById_errorInTransaction_throwHibernateException() throws Exception{
        SubjectRepository testedRepository = factory.createSubjectRepository();
        Session sessionWithException = factory.createMockSessionWithException();
        exception.expect(HibernateException.class);

        testedRepository.get(1,sessionWithException);
    }

    @Test
    public void getById_noSuchElement_throwRepositoryException() throws Exception{
        SubjectRepository testedRepository = factory.createSubjectRepository();
        Session mockSession = factory.createMockSession();

        exception.expect(RepositoryException.class);

        testedRepository.get(1,mockSession);
    }



    @Test
    public void create_errorInTransaction_throwHibernateException() throws  Exception{
        SubjectRepository testedRepository = factory.createSubjectRepository();
        Session mockSession = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.create(new Subject(),mockSession);
    }

    @Test
    public void create_CreatedEntityNotNull_AddObject() throws Exception{
        Subject createdObject = new Subject();
        Session mockSession = factory.createMockSessionWithSave();
        SubjectRepository testedRepository = factory.createSubjectRepository();

        testedRepository.create(createdObject,mockSession);

        verify(mockSession).save(createdObject);
    }

    @Test
    public void create_CreatedEntityNotNull_ReturnEntity() throws Exception{
        Subject expectedSubject = new Subject();
        Session mockSession = factory.createMockSessionWithSave();
        SubjectRepository testedRepository = factory.createSubjectRepository();

        Subject returnedSubject = testedRepository.create(expectedSubject,mockSession);

        assertEquals(expectedSubject, returnedSubject);
    }

    @Test
    public void update_errorInTransaction_throwHibernateException()throws Exception{
        SubjectRepository testedRepository = factory.createSubjectRepository();
        Session mockSession = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.update(new Subject(),mockSession);
    }

    @Test
    public void update_UpdatedEntityNotNull_UpdateObject() throws Exception{
        Subject updatedObject = new Subject();
        Session mockSession = factory.createMockSession();
        SubjectRepository testedRepository = factory.createSubjectRepository();

        testedRepository.update(updatedObject,mockSession);

        verify(mockSession).update(updatedObject);
    }

    @Test
    public void delete_errorInTransaction_throwHibernateException() throws Exception{
        SubjectRepository testedRepository = factory.createSubjectRepository();
        Session sessionWithException = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.delete(new Subject(),sessionWithException);
    }

    @Test
    public void delete_validId_deleteObject() throws Exception{
        Subject deletedObject = new Subject();
        SubjectRepository testedRepository = factory.createSubjectRepository();
        Session mockSession = factory.createMockSessionWithGet(deletedObject);

        testedRepository.delete(deletedObject,mockSession);

        verify(mockSession).delete(deletedObject);
    }
}
