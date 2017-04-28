package school.journal;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import school.journal.Factories.TestsFactory;
import school.journal.entity.SubjectInSchedule;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.impl.SubjectInScheduleRepository;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class SubjectInScheduleRepositoryTest {
    private final TestsFactory factory = new TestsFactory(SubjectInSchedule.class);

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getAll_default_returnObject() throws Exception{
        ArrayList<SubjectInSchedule> expectedObjects = new ArrayList<>();
        expectedObjects.add(new SubjectInSchedule());
        expectedObjects.add(new SubjectInSchedule());

        SubjectInScheduleRepository testedRepository = factory.createSubjectInScheduleRepository();
        Session mockSession = factory.createMockSessionWithGetAll(expectedObjects);

        assertEquals(testedRepository.query(null,mockSession),expectedObjects);
    }

    @Test
    public void getAll_errorInTransaction_throwHibernateException() throws Exception{
        SubjectInScheduleRepository testedRepository = factory.createSubjectInScheduleRepository();
        Session sessionWithException = factory.createMockSessionWithException();
        exception.expect(HibernateException.class);
        testedRepository.query(null,sessionWithException);
    }

    @Test
    public void getById_idIsGreaterThanZero_returnObject()throws Exception{
        SubjectInSchedule expectedObject = new SubjectInSchedule();
        SubjectInScheduleRepository testedRepository = factory.createSubjectInScheduleRepository();
        Session mockSession = factory.createMockSessionWithGet(expectedObject);

        assertEquals(testedRepository.get(1,mockSession),expectedObject);
    }

    @Test
    public void getById_errorInTransaction_throwHibernateException() throws Exception{
        SubjectInScheduleRepository testedRepository = factory.createSubjectInScheduleRepository();
        Session sessionWithException = factory.createMockSessionWithException();
        exception.expect(HibernateException.class);

        testedRepository.get(1,sessionWithException);
    }

    @Test
    public void getById_noSuchElement_throwRepositoryException() throws Exception{
        SubjectInScheduleRepository testedRepository = factory.createSubjectInScheduleRepository();
        Session mockSession = factory.createMockSession();

        exception.expect(RepositoryException.class);

        testedRepository.get(1,mockSession);
    }



    @Test
    public void create_errorInTransaction_throwHibernateException() throws  Exception{
        SubjectInScheduleRepository testedRepository = factory.createSubjectInScheduleRepository();
        Session mockSession = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.create(new SubjectInSchedule(),mockSession);
    }

    @Test
    public void create_CreatedEntityNotNull_AddObject() throws Exception{
        SubjectInSchedule createdObject = new SubjectInSchedule();
        Session mockSession = factory.createMockSessionWithSave();
        SubjectInScheduleRepository testedRepository = factory.createSubjectInScheduleRepository();

        testedRepository.create(createdObject,mockSession);

        verify(mockSession).save(createdObject);
    }

    @Test
    public void create_CreatedEntityNotNull_ReturnEntity() throws Exception{
        SubjectInSchedule expectedSubjectInSchedule = new SubjectInSchedule();
        Session mockSession = factory.createMockSessionWithSave();
        SubjectInScheduleRepository testedRepository = factory.createSubjectInScheduleRepository();

        SubjectInSchedule returnedSubjectInSchedule = testedRepository.create(expectedSubjectInSchedule,mockSession);

        assertEquals(expectedSubjectInSchedule, returnedSubjectInSchedule);
    }

    @Test
    public void update_errorInTransaction_throwHibernateException()throws Exception{
        SubjectInScheduleRepository testedRepository = factory.createSubjectInScheduleRepository();
        Session mockSession = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.update(new SubjectInSchedule(),mockSession);
    }

    @Test
    public void update_UpdatedEntityNotNull_UpdateObject() throws Exception{
        SubjectInSchedule updatedObject = new SubjectInSchedule();
        Session mockSession = factory.createMockSession();
        SubjectInScheduleRepository testedRepository = factory.createSubjectInScheduleRepository();

        testedRepository.update(updatedObject,mockSession);

        verify(mockSession).update(updatedObject);
    }

    @Test
    public void delete_errorInTransaction_throwHibernateException() throws Exception{
        SubjectInScheduleRepository testedRepository = factory.createSubjectInScheduleRepository();
        Session sessionWithException = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.delete(new SubjectInSchedule(),sessionWithException);
    }

    @Test
    public void delete_validObject_deleteObject() throws Exception{
        SubjectInSchedule deletedObject = new SubjectInSchedule();
        SubjectInScheduleRepository testedRepository = factory.createSubjectInScheduleRepository();
        Session mockSession = factory.createMockSessionWithGet(deletedObject);

        testedRepository.delete(deletedObject,mockSession);

        verify(mockSession).delete(deletedObject);
    }
}
