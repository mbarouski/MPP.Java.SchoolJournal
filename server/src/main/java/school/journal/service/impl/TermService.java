package school.journal.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import school.journal.entity.Term;
import school.journal.service.CRUDService;
import school.journal.service.ITermService;
import school.journal.service.exception.ServiceException;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service("TermService")
public class TermService extends CRUDService<Term> implements ITermService {

    private static final int MIN_VACATION_DAY_COUNT = 30;

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
//        checkTerms(term.getStart(), term.getEnd());
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
        return terms.get(0);
    }

    private void checkYearStart(Date date) throws ServiceException {
        Calendar calendar = Calendar.getInstance();
        Calendar firstSeptemberStart = Calendar.getInstance();
        Calendar firstSeptemberEnd = Calendar.getInstance();
        firstSeptemberStart.set(calendar.get(Calendar.YEAR),Calendar.SEPTEMBER,1,0,0,0);
        firstSeptemberEnd.set(calendar.get(Calendar.YEAR),Calendar.SEPTEMBER,1,23,59,59);
        if (calendar.get(Calendar.MONTH)<Calendar.SEPTEMBER){
            firstSeptemberEnd.set(Calendar.YEAR,firstSeptemberEnd.get(Calendar.YEAR)-1);
            firstSeptemberStart.set(Calendar.YEAR,firstSeptemberEnd.get(Calendar.YEAR));
        }
        if (date.before(firstSeptemberStart.getTime())||date.after(firstSeptemberEnd.getTime())) {
            throw new ServiceException("First term start is not valid");
        }
    }

    private void checkYearEnd(Date date) throws ServiceException {
        Calendar calendar = Calendar.getInstance();
        Calendar lastMayStart = Calendar.getInstance();
        Calendar lastMayEnd = Calendar.getInstance();
        lastMayStart.set(calendar.get(Calendar.YEAR),Calendar.MAY,31,0,0,0);
        lastMayEnd.set(calendar.get(Calendar.YEAR),Calendar.MAY,31,23,59,59);
        if (calendar.get(Calendar.MONTH)>Calendar.SEPTEMBER){
            lastMayEnd.set(Calendar.YEAR,lastMayEnd.get(Calendar.YEAR)+1);
            lastMayStart.set(Calendar.YEAR,lastMayEnd.get(Calendar.YEAR));
        }
        if (date.before(lastMayStart.getTime())||date.after(lastMayEnd.getTime())) {
            throw new ServiceException("First term start is not valid");
        }
    }

    private void checkVacationsDayLength(List<Term> terms) throws ServiceException {
        long dayCount = 0;
        final long MILLISECONDS_IN_DAY = 86_400_000;
        Date date = null;
        for (Term term : terms) {
            if (date != null) {
                dayCount += (getDayStartDate(term.getStart().getTime()) - getDayStartDate(date.getTime())) / MILLISECONDS_IN_DAY;
            }
            date = term.getEnd();
        }
        if (dayCount < MIN_VACATION_DAY_COUNT) {
            throw new ServiceException("Vacation count is not valid");
        }
    }

    private long getDayStartDate(long millis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    private void checkOverlapping(Term term, Session session) throws ServiceException {
        List<Term> terms = (List<Term>) session.createCriteria(Term.class).list();
        try {
            if (term.getNumber() == 1) {
                Term afterTerm = getTermByNumber(terms, 2);
                if (term.getEnd().after(afterTerm.getStart())) {
                    throw new ServiceException("Time is overlapped");
                }
                checkYearStart(term.getStart());
            } else if (term.getNumber() == 4) {
                Term beforeTerm = getTermByNumber(terms, 3);
                if (term.getEnd().before(beforeTerm.getStart())) {
                    throw new ServiceException("Time is overlapped");
                }
                checkYearEnd(term.getEnd());
            } else {
                int termNumber = term.getNumber();
                Term afterTerm = getTermByNumber(terms, termNumber + 1);
                Term beforeTerm = getTermByNumber(terms, termNumber - 1);
                if (term.getEnd().after(afterTerm.getStart()))
                    throw new ServiceException("Time is overlapped");
                if (term.getStart().before(beforeTerm.getEnd()))
                    throw new ServiceException("Time is overlapped");
            }
            terms.set(term.getNumber()-1,term);
            checkVacationsDayLength(terms);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            session.getTransaction().rollback();
            throw exc;
        }
    }

    /*private void checkTerms(Date start, Date end) throws ServiceException {
        if(start.after(end)) throw new ServiceException("Start is after end");
        long diff = end.getTime() - start.getTime();
        if((new Date(diff)).before(Date.valueOf("1970-02-01"))) throw new ServiceException("Incorrect term duration");
        if((new Date(diff)).after(Date.valueOf("1971-01-01"))) throw new ServiceException("Incorrect term duration");
    }*/
}
