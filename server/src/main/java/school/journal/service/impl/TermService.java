package school.journal.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import school.journal.entity.Teacher;
import school.journal.entity.Term;
import school.journal.service.CRUDService;
import school.journal.service.ITermService;
import school.journal.service.exception.ServiceException;
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
}
m