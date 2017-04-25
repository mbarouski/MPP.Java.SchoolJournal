package school.journal.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import school.journal.controller.util.ExceptionEnum;
import school.journal.entity.Term;
import school.journal.service.CRUDService;
import school.journal.service.ITermService;
import school.journal.service.exception.ClassifiedServiceException;
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
        checkTermExists(t);
        t.setEnd(term.getEnd());
        t.setStart(term.getStart());
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

    private void checkTermExists(Term term) throws ServiceException {
        if (term == null) {
            throw new ClassifiedServiceException(ExceptionEnum.term_not_found);
        }
    }

    private Term getTermByNumber(List<Term> terms, int number) throws ServiceException{
        for(Term term : terms) {
            if(term.getNumber() == number) return term;
        }
        throw new ClassifiedServiceException(ExceptionEnum.term_not_found);
    }

    private void checkYearStart(Date date) throws ServiceException {
        Calendar calendar = Calendar.getInstance();
        Calendar firstSeptemberStart = Calendar.getInstance();
        firstSeptemberStart.set(calendar.get(Calendar.YEAR), Calendar.SEPTEMBER, 1, 0, 0, 0);
        shiftYearForSeptember(calendar, firstSeptemberStart);
        if (date.before(firstSeptemberStart.getTime())) {
            throw new ClassifiedServiceException(ExceptionEnum.first_term_start_is_before_september_1st);
        }
        firstSeptemberStart.set(calendar.get(Calendar.YEAR), Calendar.SEPTEMBER, 1, 23, 59, 59);
        if (date.after(firstSeptemberStart.getTime())) {
            throw new ClassifiedServiceException(ExceptionEnum.first_term_start_is_after_september_1st);
        }
    }

    private void shiftYearForSeptember(Calendar calendar,Calendar firstSeptember) {
        if (calendar.get(Calendar.MONTH) < Calendar.SEPTEMBER) {
            firstSeptember.set(Calendar.YEAR, firstSeptember.get(Calendar.YEAR) - 1);
        }
    }

    private void checkYearEnd(Date date) throws ServiceException {
        Calendar calendar = Calendar.getInstance();
        Calendar lastMayStart = Calendar.getInstance();
        lastMayStart.set(calendar.get(Calendar.YEAR),Calendar.MAY,31,0,0,0);
        shiftYearForMay(calendar,lastMayStart);
        if (date.before(lastMayStart.getTime())){
            throw new ClassifiedServiceException(ExceptionEnum.last_term_end_is_before_May_31st);
        }
        lastMayStart.set(calendar.get(Calendar.YEAR),Calendar.MAY,31,23,59,59);
        if (date.after(lastMayStart.getTime())) {
            throw new ClassifiedServiceException(ExceptionEnum.last_term_end_is_after_May_31st);
        }
    }

    private void shiftYearForMay(Calendar calendar,Calendar lastMay) {
        if (calendar.get(Calendar.MONTH) > Calendar.SEPTEMBER) {
            lastMay.set(Calendar.YEAR, lastMay.get(Calendar.YEAR) + 1);
        }
    }

    private void checkVacationsDayLength(List<Term> terms) throws ServiceException {
        long dayCount = 0;
        if (terms.get(0).getStart().getYear()-terms.get(3).getEnd().getYear()>1) {
            throw new ClassifiedServiceException(ExceptionEnum.too_long_year);
        }
        final long MILLISECONDS_IN_DAY = 86_400_000;
        Date date = null;
        for (Term term : terms) {
            if (date != null) {
                dayCount += (getDayStartDate(term.getStart().getTime()) - getDayStartDate(date.getTime())) / MILLISECONDS_IN_DAY;
            }
            date = term.getEnd();
        }
        if (dayCount < MIN_VACATION_DAY_COUNT) {
            throw new ClassifiedServiceException(ExceptionEnum.vacations_are_too_small);
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
            if (getDayStartDate(term.getStart().getTime())>=getDayStartDate(term.getEnd().getTime())){
                throw new ClassifiedServiceException(ExceptionEnum.term_start_is_after_its_end);
            }
            if (term.getNumber() == 1) {
                Term afterTerm = getTermByNumber(terms, 2);
                if (term.getEnd().after(afterTerm.getStart())) {
                    throw new ClassifiedServiceException(ExceptionEnum.term_is_overlapped_by_next_term);
                }
                checkYearStart(term.getStart());
            } else if (term.getNumber() == 4) {
                Term beforeTerm = getTermByNumber(terms, 3);
                if (term.getEnd().before(beforeTerm.getStart())) {
                    throw new ClassifiedServiceException(ExceptionEnum.term_is_overlapped_by_previous_term);
                }
                checkYearEnd(term.getEnd());
            } else {
                int termNumber = term.getNumber();
                Term afterTerm = getTermByNumber(terms, termNumber + 1);
                Term beforeTerm = getTermByNumber(terms, termNumber - 1);
                if (term.getEnd().after(afterTerm.getStart()))
                    throw new ClassifiedServiceException(ExceptionEnum.term_is_overlapped_by_next_term);
                if (term.getStart().before(beforeTerm.getEnd()))
                    throw new ClassifiedServiceException(ExceptionEnum.term_is_overlapped_by_previous_term);
            }
            terms.set(term.getNumber()-1,term);
            checkVacationsDayLength(terms);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            session.getTransaction().rollback();
            throw exc;
        }
    }

}
