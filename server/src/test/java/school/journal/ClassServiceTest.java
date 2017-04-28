package school.journal;

import org.hibernate.Session;
import org.junit.*;
import org.junit.rules.ExpectedException;
import school.journal.entity.Clazz;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.impl.ClazzRepository;
import school.journal.service.exception.ServiceException;
import school.journal.service.impl.ClassService;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClassServiceTest {

    ClassService service;
    ClazzRepository repository;

    @Before
    public void initRepositoryAndService(){
        repository = mock(ClazzRepository.class);
        service = new ClassService(repository);
    }

    @After
    public void destroyElements(){
        repository = null;
        service = null;
    }

    void initRepositoryWithException(IRepository repository) throws RepositoryException{
        doThrow(RepositoryException.class).when(repository).query(any(),any());
        doThrow(RepositoryException.class).when(repository).create(any(),any());
        doThrow(RepositoryException.class).when(repository).delete(any(),any());
        doThrow(RepositoryException.class).when(repository).update(any(),any());
        doThrow(RepositoryException.class).when(repository).get(anyInt(),any(Session.class));
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getAll_default_returnObjects()throws  Exception{
        ArrayList<Clazz> expectedObjects = new ArrayList<>();
        expectedObjects.add(new Clazz());
        expectedObjects.add(new Clazz());

        doReturn(expectedObjects).when(repository).query(any(),any());

        assertEquals(service.read(),expectedObjects);
    }

    @Test
    public void getAll_errorInGettingObjects_returnServiceException()throws  Exception{
        initRepositoryWithException(repository);

        exception.expect(ServiceException.class);
        service.read();
    }

    @Test
    public void create_validClazz_returnClazz()throws  Exception{
        Clazz clazz = new Clazz();
        clazz.setLetterMark("A");
        clazz.setNumber(2);

        when(repository.create(any(),any())).thenReturn(clazz);
        Clazz returnObject = service.create(clazz);
        assertEquals(returnObject,clazz);
        verify(repository).create(any(),any(Session.class));
    }

    @Test
    public void create_nullLetterMark_returnServiceExceprion()throws  Exception{
        Clazz clazz = new Clazz();

        when(repository.create(any(),any())).thenReturn(clazz);
        exception.expect(ServiceException.class);
        exception.expectMessage("Invalid (null)Letter Mark");
        service.create(clazz);
    }

    @Test
    public void create_emptyLetterMark_returnServiceExceprion()throws  Exception{
        Clazz clazz = new Clazz();
        clazz.setLetterMark("");

        when(repository.create(any(),any())).thenReturn(clazz);
        exception.expect(ServiceException.class);
        exception.expectMessage("Invalid Letter Mark");
        service.create(clazz);
    }

    @Test
    public void create_wrongClassNumber_returnServiceExceprion()throws  Exception{
        Clazz clazz = new Clazz();
        clazz.setLetterMark("A");
        clazz.setNumber(0);

        when(repository.create(any(),any())).thenReturn(clazz);
        exception.expect(ServiceException.class);
        exception.expectMessage("Invalid class number");
        service.create(clazz);
    }

    @Test
    public void create_error_returnServiceException()throws  Exception{
        Clazz clazz = new Clazz();
        clazz.setLetterMark("A");
        clazz.setNumber(2);
        initRepositoryWithException(repository);

        exception.expect(ServiceException.class);
        service.create(clazz);
    }

    @Test
    public void update_validClazz_returnClazz()throws  Exception{
        Clazz clazz = new Clazz();
        clazz.setClassId(1);
        clazz.setLetterMark("A");
        clazz.setNumber(2);

        when(repository.update(any(),any())).thenReturn(clazz);
        when(repository.get(anyInt(),any(Session.class))).thenReturn(clazz);

        Clazz returnObject = service.update(clazz);
        assertEquals(returnObject,clazz);
        verify(repository).update(any(),any(Session.class));
    }

    @Test
    public void update_noSuchElementInDataBase_returnServiceException()throws  Exception{
        Clazz clazz = new Clazz();
        clazz.setClassId(1);
        clazz.setLetterMark("A");
        clazz.setNumber(2);

        when(repository.update(any(),any())).thenReturn(clazz);
        when(repository.get(anyInt(),any(Session.class))).thenReturn(null);

        exception.expect(ServiceException.class);
        exception.expectMessage("Class is not exists");
        service.update(clazz);
    }

    @Test
    public void update_WrongNumber_doNothing()throws  Exception{
        Clazz clazz = new Clazz();
        clazz.setClassId(1);
        clazz.setLetterMark("A");
        clazz.setNumber(0);
        Clazz dbClazz = new Clazz();
        dbClazz.setClassId(1);
        dbClazz.setLetterMark("A");
        dbClazz.setNumber(1);

        when(repository.update(any(),any())).thenReturn(clazz);
        when(repository.get(anyInt(),any(Session.class))).thenReturn(dbClazz);

        service.update(clazz);
        verify(repository).update(any(),any(Session.class));
    }

    @Test
    public void update_emptyLetterMark_doNothing()throws  Exception{
        Clazz clazz = new Clazz();
        clazz.setClassId(1);
        clazz.setLetterMark("");
        clazz.setNumber(0);

        when(repository.update(any(),any())).thenReturn(clazz);
        when(repository.get(anyInt(),any(Session.class))).thenReturn(clazz);

        service.update(clazz);
        verify(repository).update(any(),any(Session.class));
    }

    @Test
    public void update_wrongClassNumber_doNothing()throws  Exception{
        Clazz clazz = new Clazz();
        clazz.setClassId(1);
        clazz.setLetterMark("A");
        clazz.setNumber(0);

        when(repository.update(any(),any())).thenReturn(clazz);
        when(repository.get(anyInt(),any(Session.class))).thenReturn(clazz);

        service.update(clazz);
        verify(repository).update(any(),any(Session.class));
    }

    @Test
    public void update_error_returnServiceException()throws  Exception{
        Clazz clazz = new Clazz();
        clazz.setClassId(1);
        clazz.setLetterMark("A");
        clazz.setNumber(0);
        initRepositoryWithException(repository);

        exception.expect(ServiceException.class);
        service.update(clazz);
    }

    @Test
    public void delete_validId_deleteObject()throws  Exception{
        service.delete(1);
        verify(repository).delete(any(),any(Session.class));
    }

    @Test
    public void delete_wrongId_throwsServiceException()throws  Exception{
        exception.expect(ServiceException.class);
        service.delete(-1);
    }

    @Test
    public void getOne_validId_getObject()throws  Exception{
        service.getOne(1);
        when(repository.get(anyInt(),any(Session.class))).thenReturn(new Clazz());
        verify(repository).get(anyInt(),any(Session.class));
    }

    @Test
    public void getOne_wrongId_throwsServiceException()throws  Exception{
        exception.expect(ServiceException.class);
        service.getOne(-1);
    }
}
