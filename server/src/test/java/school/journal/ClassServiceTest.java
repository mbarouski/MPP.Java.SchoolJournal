package school.journal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import school.journal.Factories.TestsFactory;
import school.journal.entity.Clazz;
import school.journal.service.impl.ClassService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class ClassServiceTest {

    private final TestsFactory factory = new TestsFactory(Clazz.class);

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getAll_default_returnObject()throws  Exception{

        Collection<Clazz> brandEntities = new ArrayList<>();
        brandEntities.add(new Clazz());
        brandEntities.add(new Clazz());

        Collection<Clazz> expectedObjects = brandEntities.stream().collect(Collectors.toList());
        ClassService testedService = Mockito.mock(ClassService.class);
        doReturn(expectedObjects).when(testedService).read();

        Collection<Clazz> actualObjects = testedService.read();
        assertEquals(expectedObjects.size(), actualObjects.size());
        verify(testedService).read();
    }
}
