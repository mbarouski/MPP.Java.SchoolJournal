package school.journal.service;

import school.journal.entity.Term;
import school.journal.service.exception.ServiceException;

import java.util.List;

public interface ITermService {
    Term getCurrentTerm() throws ServiceException;

    Term update(Term term) throws ServiceException;

    List<Term> read() throws ServiceException;
}
