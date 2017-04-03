package school.journal.service;

import school.journal.entity.Term;
import school.journal.service.exception.ServiceException;

public interface ITermService {
    Term getCurrentTerm() throws ServiceException;
}
