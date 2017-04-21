package school.journal;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import school.journal.Factories.TestsFactory;
import school.journal.entity.Mark;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.impl.MarkRepository;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class MarkRepositoryTest {

    private final TestsFactory factory = new TestsFactory(Mark.class);

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getAll_default_returnObject() throws Exception {
        ArrayList<Mark> expectedObjects = new ArrayList<>();
        expectedObjects.add(new Mark());
        expectedObjects.add(new Mark());

        MarkRepository testedRepository = factory.createMarkRepository();
        Session mockSession = factory.createMockSessionWithGetAll(expectedObjects);

        assertEquals(testedRepository.query(null, mockSession), expectedObjects);
    }

    @Test
    public void getAll_errorInTransaction_throwHibernateException() throws Exception {
        MarkRepository testedRepository = factory.createMarkRepository();
        Session sessionWithException = factory.createMockSessionWithException();
        exception.expect(HibernateException.class);
        testedRepository.query(null, sessionWithException);
    }

    @Test
    public void getById_idIsGreaterThanZero_returnObject() throws Exception {
        Mark expectedObject = new Mark();
        MarkRepository testedRepository = factory.createMarkRepository();
        Session mockSession = factory.createMockSessionWithGet(expectedObject);

        assertEquals(testedRepository.get(1, mockSession), expectedObject);
    }

    @Test
    public void getById_errorInTransaction_throwHibernateException() throws Exception {
        MarkRepository testedRepository = factory.createMarkRepository();
        Session sessionWithException = factory.createMockSessionWithException();
        exception.expect(HibernateException.class);

        testedRepository.get(1, sessionWithException);
    }

    @Test
    public void getById_noSuchElement_throwRepositoryException() throws Exception {
        MarkRepository testedRepository = factory.createMarkRepository();
        Session mockSession = factory.createMockSession();

        exception.expect(RepositoryException.class);

        testedRepository.get(1, mockSession);
    }


    @Test
    public void create_errorInTransaction_throwHibernateException() throws Exception {
        MarkRepository testedRepository = factory.createMarkRepository();
        Session mockSession = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.create(new Mark(), mockSession);
    }

    @Test
    public void create_CreatedEntityNotNull_AddObject() throws Exception {
        Mark createdObject = new Mark();
        Session mockSession = factory.createMockSessionWithSave();
        MarkRepository testedRepository = factory.createMarkRepository();

        testedRepository.create(createdObject, mockSession);

        verify(mockSession).save(createdObject);
    }

    @Test
    public void create_CreatedEntityNotNull_ReturnEntity() throws Exception {
        Mark expectedMark = new Mark();
        Session mockSession = factory.createMockSessionWithSave();
        MarkRepository testedRepository = factory.createMarkRepository();

        testedRepository.create(expectedMark, mockSession);

        verify(mockSession).save(expectedMark);
    }

    @Test
    public void update_errorInTransaction_throwHibernateException() throws Exception {
        MarkRepository testedRepository = factory.createMarkRepository();
        Session mockSession = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.update(new Mark(), mockSession);
    }

    @Test
    public void update_UpdatedEntityNotNull_UpdateObject() throws Exception {
        Mark updatedObject = new Mark();
        Session mockSession = factory.createMockSession();
        MarkRepository testedRepository = factory.createMarkRepository();

        testedRepository.update(updatedObject, mockSession);

        verify(mockSession).merge(updatedObject);
    }

    @Test
    public void delete_errorInTransaction_throwHibernateException() throws Exception {
        MarkRepository testedRepository = factory.createMarkRepository();
        Session sessionWithException = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.delete(new Mark(), sessionWithException);
    }

    @Test
    public void delete_validId_deleteObject() throws Exception {
        Mark deletedObject = new Mark();
        MarkRepository testedRepository = factory.createMarkRepository();
        Session mockSession = factory.createMockSessionWithGet(deletedObject);

        testedRepository.delete(deletedObject, mockSession);

        verify(mockSession).delete(deletedObject);
    }
}