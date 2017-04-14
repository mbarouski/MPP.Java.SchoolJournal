package school.journal.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import school.journal.entity.LessonTime;
import school.journal.entity.Teacher;
import school.journal.entity.Term;
import school.journal.service.CRUDService;
import school.journal.service.ITermService;
import school.journal.service.exception.ServiceException;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;

@Service("TermService")
public class TermService extends CRUDService<Term> implements ITermService {
    @Override
    public Term getCurrentTerm() throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Term> terms = session.createCriteria(Term.class).add(
                Restrictions.and(Restrictions.ge("end", Calendar.getInstance().getTime()),
                        Restrictions.le("start", Calendar.getInstance().getTime()))).list();
        return terms.size() > 0 ? terms.get(0) : null;
    }

    @Override
    public Term update(Term term) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Term t = (Term) session.get(Term.class, term.getTermId());
        if(t == null) throw new ServiceException("Term not found");
        t.setEnd(term.getEnd());
        t.setStart(term.getStart());
        checkTerms(term.getStart(), term.getEnd());
        checkOverlapping(term, session);
        session.update(t);
        transaction.commit();
        return term;
    }

    @Override
    public List<Term> read() throws ServiceException {
        Session session = sessionFactory.openSession();
        List<Term> terms = session.createCriteria(Term.class).list();
        return terms;
    }

    private Term getTermByNumber(List<Term> terms, int number) {
        for(Term term : terms) {
            if(term.getNumber() == number) return term;
        }
        return null;
    }

    private void checkOverlapping(Term term, Session session) throws ServiceException {
        List<Term> terms = (List<Term>) session.createCriteria(Term.class).list();
        try {
            if (term.getNumber() == 1) {
                Term afterTerm = getTermByNumber(terms, 2);
                if (term.getEnd().after(afterTerm.getStart()))
                    throw new ServiceException("Time is overlapped");
            } else if (term.getNumber() == 4) {
                Term beforeTerm = getTermByNumber(terms, 3);
                if (term.getEnd().before(beforeTerm.getStart()))
                    throw new ServiceException("Time is overlapped");
            } else {
                int termNumber = term.getNumber();
                Term afterTerm = getTermByNumber(terms, termNumber + 1);
                Term beforeTerm = getTermByNumber(terms, termNumber - 1);
                if (term.getEnd().after(afterTerm.getStart()))
                    throw new ServiceException("Time is overlapped");
                if (term.getStart().before(beforeTerm.getEnd()))
                    throw new ServiceException("Time is overlapped");
            }
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            throw exc;
        }
    }

    private void checkTerms(Date start, Date end) throws ServiceException {
        if(start.after(end)) throw new ServiceException("Start is after end");
        long diff = end.getTime() - start.getTime();
        if((new Date(diff)).before(Date.valueOf("1970-02-01"))) throw new ServiceException("Incorrect term duration");
        if((new Date(diff)).after(Date.valueOf("1971-01-01"))) throw new ServiceException("Incorrect term duration");
    }
}
