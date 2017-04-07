package school.journal.repository.specification.mark;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.Mark;
import school.journal.entity.Term;

import java.sql.Date;

public class MarkSpecificationByTerm extends MarkSpecification {

    private Date dateFrom;
    private Date dateTo;

    public MarkSpecificationByTerm(Term term) {
        this.dateFrom = term.getStart();
        this.dateTo = term.getEnd();
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.between("date",dateFrom,dateTo);
    }

    @Override
    public boolean specified(Mark mark) {
        Date date = mark.getDate();
        return date.equals(dateFrom) || date.equals(dateTo) || date.after(dateFrom) && date.before(dateTo);
    }
}
