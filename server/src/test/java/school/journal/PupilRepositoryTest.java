package school.journal;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import school.journal.Factories.TestsFactory;
import school.journal.entity.Pupil;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.impl.PupilRepository;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class PupilRepositoryTest
{
        private final TestsFactory factory = new TestsFactory(Pupil.class);

        @Rule
        public final ExpectedException exception = ExpectedException.none();

        @Test
        public void getAll_default_returnObject() throws Exception{
            ArrayList<Pupil> expectedObjects = new ArrayList<>();
            expectedObjects.add(new Pupil());
            expectedObjects.add(new Pupil());

            PupilRepository testedRepository = factory.createPupilRepository();
            Session mockSession = factory.createMockSessionWithGetAll(expectedObjects);

            assertEquals(testedRepository.query(null,mockSession),expectedObjects);
        }

        @Test
        public void getAll_errorInTransaction_throwHibernateException() throws Exception{
            PupilRepository testedRepository = factory.createPupilRepository();
            Session sessionWithException = factory.createMockSessionWithException();
            exception.expect(HibernateException.class);
            testedRepository.query(null,sessionWithException);
        }

        @Test
        public void getById_idIsGreaterThanZero_returnObject()throws Exception{
            Pupil expectedObject = new Pupil();
            PupilRepository testedRepository = factory.createPupilRepository();
            Session mockSession = factory.createMockSessionWithGet(expectedObject);

            assertEquals(testedRepository.get(1,mockSession),expectedObject);
        }

        @Test
        public void getById_errorInTransaction_throwHibernateException() throws Exception{
            PupilRepository testedRepository = factory.createPupilRepository();
            Session sessionWithException = factory.createMockSessionWithException();
            exception.expect(HibernateException.class);

            testedRepository.get(1,sessionWithException);
        }

        @Test
        public void getById_noSuchElement_throwRepositoryException() throws Exception{
            PupilRepository testedRepository = factory.createPupilRepository();
            Session mockSession = factory.createMockSession();

            exception.expect(RepositoryException.class);

            testedRepository.get(1,mockSession);
        }



        @Test
        public void create_errorInTransaction_throwHibernateException() throws  Exception{
            PupilRepository testedRepository = factory.createPupilRepository();
            Session mockSession = factory.createMockSessionWithException();

            exception.expect(HibernateException.class);
            testedRepository.create(new Pupil(),mockSession);
        }

        @Test
        public void create_CreatedEntityNotNull_AddObject() throws Exception{
            Pupil createdObject = new Pupil();
            Session mockSession = factory.createMockSessionWithSave();
            PupilRepository testedRepository = factory.createPupilRepository();

            testedRepository.create(createdObject,mockSession);

            verify(mockSession).save(createdObject);
        }

        @Test
        public void create_CreatedEntityNotNull_ReturnEntity() throws Exception{
            Pupil expectedPupil = new Pupil();
            Session mockSession = factory.createMockSessionWithSave();
            PupilRepository testedRepository = factory.createPupilRepository();

            Pupil returnedPupil = testedRepository.create(expectedPupil,mockSession);

            assertEquals(expectedPupil, returnedPupil);
        }

        @Test
        public void update_errorInTransaction_throwHibernateException()throws Exception{
            PupilRepository testedRepository = factory.createPupilRepository();
            Session mockSession = factory.createMockSessionWithException();

            exception.expect(HibernateException.class);
            testedRepository.update(new Pupil(),mockSession);
        }

        @Test
        public void update_UpdatedEntityNotNull_UpdateObject() throws Exception{
            Pupil updatedObject = new Pupil();
            Session mockSession = factory.createMockSession();
            PupilRepository testedRepository = factory.createPupilRepository();

            testedRepository.update(updatedObject,mockSession);

            verify(mockSession).merge(updatedObject);
        }

        @Test
        public void delete_errorInTransaction_throwHibernateException() throws Exception{
            PupilRepository testedRepository = factory.createPupilRepository();
            Session sessionWithException = factory.createMockSessionWithException();

            exception.expect(HibernateException.class);
            testedRepository.delete(new Pupil(),sessionWithException);
        }

        @Test
        public void delete_validId_deleteObject() throws Exception{
            Pupil deletedObject = new Pupil();
            PupilRepository testedRepository = factory.createPupilRepository();
            Session mockSession = factory.createMockSessionWithGet(deletedObject);

            testedRepository.delete(deletedObject,mockSession);

            verify(mockSession).delete(deletedObject);
        }
}