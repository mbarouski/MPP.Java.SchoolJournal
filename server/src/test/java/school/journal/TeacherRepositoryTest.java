package school.journal;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import school.journal.Factories.TestsFactory;
import school.journal.entity.Teacher;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.impl.TeacherRepository;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class TeacherRepositoryTest {
    private final TestsFactory factory = new TestsFactory(Teacher.class);

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getAll_default_returnObject() throws Exception{
        ArrayList<Teacher> expectedObjects = new ArrayList<>();
        expectedObjects.add(new Teacher());
        expectedObjects.add(new Teacher());

        TeacherRepository testedRepository = factory.createTeacherRepository();
        Session mockSession = factory.createMockSessionWithGetAll(expectedObjects);

        assertEquals(testedRepository.query(null,mockSession),expectedObjects);
    }

    @Test
    public void getAll_errorInTransaction_throwHibernateException() throws Exception{
        TeacherRepository testedRepository = factory.createTeacherRepository();
        Session sessionWithException = factory.createMockSessionWithException();
        exception.expect(HibernateException.class);
        testedRepository.query(null,sessionWithException);
    }

    @Test
    public void getById_idIsGreaterThanZero_returnObject()throws Exception{
        Teacher expectedObject = new Teacher();
        TeacherRepository testedRepository = factory.createTeacherRepository();
        Session mockSession = factory.createMockSessionWithGet(expectedObject);

        assertEquals(testedRepository.get(1,mockSession),expectedObject);
    }

    @Test
    public void getById_errorInTransaction_throwHibernateException() throws Exception{
        TeacherRepository testedRepository = factory.createTeacherRepository();
        Session sessionWithException = factory.createMockSessionWithException();
        exception.expect(HibernateException.class);

        testedRepository.get(1,sessionWithException);
    }

    @Test
    public void getById_noSuchElement_throwRepositoryException() throws Exception{
        TeacherRepository testedRepository = factory.createTeacherRepository();
        Session mockSession = factory.createMockSession();

        exception.expect(RepositoryException.class);

        testedRepository.get(1,mockSession);
    }



    @Test
    public void create_errorInTransaction_throwHibernateException() throws  Exception{
        TeacherRepository testedRepository = factory.createTeacherRepository();
        Session mockSession = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.create(new Teacher(),mockSession);
    }

    @Test
    public void create_CreatedEntityNotNull_AddObject() throws Exception{
        Teacher createdObject = new Teacher();
        Session mockSession = factory.createMockSessionWithSave();
        TeacherRepository testedRepository = factory.createTeacherRepository();

        testedRepository.create(createdObject,mockSession);

        verify(mockSession).save(createdObject);
    }

    @Test
    public void create_CreatedEntityNotNull_ReturnEntity() throws Exception{
        Teacher expectedTeacher = new Teacher();
        Session mockSession = factory.createMockSessionWithSave();
        TeacherRepository testedRepository = factory.createTeacherRepository();

        Teacher returnedTeacher = testedRepository.create(expectedTeacher,mockSession);

        assertEquals(expectedTeacher, returnedTeacher);
    }

    @Test
    public void update_errorInTransaction_throwHibernateException()throws Exception{
        TeacherRepository testedRepository = factory.createTeacherRepository();
        Session mockSession = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.update(new Teacher(),mockSession);
    }

    @Test
    public void update_UpdatedEntityNotNull_UpdateObject() throws Exception{
        Teacher updatedObject = new Teacher();
        Session mockSession = factory.createMockSession();
        TeacherRepository testedRepository = factory.createTeacherRepository();

        testedRepository.update(updatedObject,mockSession);

        verify(mockSession).update(updatedObject);
    }

    @Test
    public void delete_errorInTransaction_throwHibernateException() throws Exception{
        TeacherRepository testedRepository = factory.createTeacherRepository();
        Session sessionWithException = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.delete(new Teacher(),sessionWithException);
    }

    @Test
    public void delete_validId_deleteObject() throws Exception{
        Teacher deletedObject = new Teacher();
        TeacherRepository testedRepository = factory.createTeacherRepository();
        Session mockSession = factory.createMockSessionWithGet(deletedObject);

        testedRepository.delete(deletedObject,mockSession);

        verify(mockSession).delete(deletedObject);
    }
}
