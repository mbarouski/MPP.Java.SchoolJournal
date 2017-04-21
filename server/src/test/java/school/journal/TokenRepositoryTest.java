package school.journal;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import school.journal.Factories.TestsFactory;
import school.journal.entity.Token;
import school.journal.repository.impl.TokenRepository;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class TokenRepositoryTest {
    private final TestsFactory factory = new TestsFactory(Token.class);

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getAll_default_returnObject() throws Exception{
        ArrayList<Token> expectedObjects = new ArrayList<>();
        expectedObjects.add(new Token());
        expectedObjects.add(new Token());

        TokenRepository testedRepository = factory.createTokenRepository();
        Session mockSession = factory.createMockSessionWithGetAll(expectedObjects);

        assertEquals(testedRepository.query(null,mockSession),expectedObjects);
    }

    @Test
    public void getAll_errorInTransaction_throwHibernateException() throws Exception{
        TokenRepository testedRepository = factory.createTokenRepository();
        Session sessionWithException = factory.createMockSessionWithException();
        exception.expect(HibernateException.class);
        testedRepository.query(null,sessionWithException);
    }

    @Test
    public void getById_anyValue_returnNull()throws Exception{
        Token token = new Token();
        TokenRepository testedRepository = factory.createTokenRepository();
        Session mockSession = factory.createMockSessionWithGet(token);

        assertEquals(testedRepository.get(1,mockSession),null);
    }

    @Test
    public void create_errorInTransaction_throwHibernateException() throws  Exception{
        TokenRepository testedRepository = factory.createTokenRepository();
        Session mockSession = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.create(new Token(),mockSession);
    }

    @Test
    public void create_CreatedEntityNotNull_AddObject() throws Exception{
        Token createdObject = new Token();
        Session mockSession = factory.createMockSessionWithSave();
        TokenRepository testedRepository = factory.createTokenRepository();

        testedRepository.create(createdObject,mockSession);

        verify(mockSession).save(createdObject);
    }

    @Test
    public void create_CreatedEntityNotNull_ReturnEntity() throws Exception{
        Token expectedToken = new Token();
        Session mockSession = factory.createMockSessionWithSave();
        TokenRepository testedRepository = factory.createTokenRepository();

        Token returnedToken = testedRepository.create(expectedToken,mockSession);

        assertEquals(expectedToken, returnedToken);
    }

    @Test
    public void update_errorInTransaction_throwHibernateException()throws Exception{
        TokenRepository testedRepository = factory.createTokenRepository();
        Session mockSession = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.update(new Token(),mockSession);
    }

    @Test
    public void update_UpdatedEntityNotNull_UpdateObject() throws Exception{
        Token updatedObject = new Token();
        Session mockSession = factory.createMockSession();
        TokenRepository testedRepository = factory.createTokenRepository();

        testedRepository.update(updatedObject,mockSession);

        verify(mockSession).update(updatedObject);
    }

    @Test
    public void delete_errorInTransaction_throwHibernateException() throws Exception{
        TokenRepository testedRepository = factory.createTokenRepository();
        Session sessionWithException = factory.createMockSessionWithException();

        exception.expect(HibernateException.class);
        testedRepository.delete(new Token(),sessionWithException);
    }

    @Test
    public void delete_validId_deleteObject() throws Exception{
        Token deletedObject = new Token();
        TokenRepository testedRepository = factory.createTokenRepository();
        Session mockSession = factory.createMockSessionWithGet(deletedObject);

        testedRepository.delete(deletedObject,mockSession);

        verify(mockSession).delete(deletedObject);
    }
}
