package school.journal;
import org.junit.Test;
import school.journal.entity.Term;
import school.journal.service.ITermService;
import school.journal.service.exception.ServiceException;
import school.journal.service.impl.TermService;

import static org.junit.Assert.assertTrue;

public class TermServiceTest {

    private ITermService termService = new TermService();

    @Test
    public void getCurrentTermTest() throws ServiceException {
        Term term = termService.getCurrentTerm();
        assertTrue(term != null);
    }
}
