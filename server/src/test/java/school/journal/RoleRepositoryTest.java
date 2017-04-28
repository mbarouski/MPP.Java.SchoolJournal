package school.journal;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import school.journal.Factories.TestsFactory;
import school.journal.entity.Role;
import school.journal.repository.impl.RoleRepository;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class RoleRepositoryTest {
    private final TestsFactory factory = new TestsFactory(Role.class);

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getAll_default_returnObject() throws Exception{
        ArrayList<Role> expectedObjects = new ArrayList<>();
        expectedObjects.add(new Role());
        expectedObjects.add(new Role());

        RoleRepository testedRepository = factory.createRoleRepository();
        Session mockSession = factory.createMockSessionWithGetAll(expectedObjects);

        assertEquals(testedRepository.query(null,mockSession),expectedObjects);
    }

    @Test
    public void getAll_errorInTransaction_throwHibernateException() throws Exception{
        RoleRepository testedRepository = factory.createRoleRepository();
        Session sessionWithException = factory.createMockSessionWithException();
        exception.expect(HibernateException.class);
        testedRepository.query(null,sessionWithException);
    }

    @Test
    public void getById_alwaysReturnNull()throws Exception{
        Role expectedObject = new Role();
        RoleRepository testedRepository = factory.createRoleRepository();
        Session mockSession = factory.createMockSessionWithGet(expectedObject);

        assertEquals(testedRepository.get(1,mockSession),null);
    }



    @Test
    public void create_errorInTransaction_throwHibernateException() throws  Exception{
        RoleRepository testedRepository = factory.createRoleRepository();
        Session mockSession = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.create(new Role(),mockSession);
    }

    @Test
    public void create_CreatedEntityNotNull_AddObject() throws Exception{
        Role createdObject = new Role();
        Session mockSession = factory.createMockSessionWithSave();
        RoleRepository testedRepository = factory.createRoleRepository();

        testedRepository.create(createdObject,mockSession);

        verify(mockSession).save(createdObject);
    }

    @Test
    public void create_CreatedEntityNotNull_ReturnEntity() throws Exception{
        Role expectedRole = new Role();
        Session mockSession = factory.createMockSessionWithSave();
        RoleRepository testedRepository = factory.createRoleRepository();

        Role returnedRole = testedRepository.create(expectedRole,mockSession);

        assertEquals(expectedRole, returnedRole);
    }

    @Test
    public void update_errorInTransaction_throwHibernateException()throws Exception{
        RoleRepository testedRepository = factory.createRoleRepository();
        Session mockSession = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.update(new Role(),mockSession);
    }

    @Test
    public void update_UpdatedEntityNotNull_UpdateObject() throws Exception{
        Role updatedObject = new Role();
        Session mockSession = factory.createMockSession();
        RoleRepository testedRepository = factory.createRoleRepository();

        testedRepository.update(updatedObject,mockSession);

        verify(mockSession).update(updatedObject);
    }

    @Test
    public void delete_errorInTransaction_throwHibernateException() throws Exception{
        RoleRepository testedRepository = factory.createRoleRepository();
        Session sessionWithException = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.delete(new Role(),sessionWithException);
    }

    @Test
    public void delete_validObject_deleteObject() throws Exception{
        Role deletedObject = new Role();
        RoleRepository testedRepository = factory.createRoleRepository();
        Session mockSession = factory.createMockSessionWithGet(deletedObject);

        testedRepository.delete(deletedObject,mockSession);

        verify(mockSession).delete(deletedObject);
    }
}
