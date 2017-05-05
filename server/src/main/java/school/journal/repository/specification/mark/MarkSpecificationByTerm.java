package school.journal.repository.specification.mark;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.Mark;
import school.journal.entity.Term;

import java.sql.Date;

import static school.journal.entity.enums.MarkType.*;
import static school.journal.service.impl.TermService.MILLISECONDS_IN_DAY;

public class MarkSpecificationByTerm extends MarkSpecification {

    private Date dateFrom;
    private Date dateTo;
//    private Integer termNumber;

    public MarkSpecificationByTerm(Term term) {
        this.dateFrom = term.getStart();
        this.dateTo = new Date(term.getEnd().getTime()+MILLISECONDS_IN_DAY);
//        this.termNumber = term.getNumber();
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.or(
                Restrictions.or(
                        Restrictions.eq("type",term),
                        Restrictions.eq("type", year)),
                Restrictions.between("date",dateFrom,dateTo));
//        return Restrictions.or(
//                Restrictions.or(
//                        Restrictions.eq("termNumber", termNumber),
//                        Restrictions.or(
//                                Restrictions.eq("type", term),
//                                Restrictions.eq("type", year))),
//                Restrictions.between("date", dateFrom, dateTo));
    }

    @Override
    public boolean specified(Mark mark) {
        Date date = mark.getDate();
        return (
//                        mark.getTermNumber().intValue() == termNumber &&
                mark.getType() == term)
                || mark.getType() == year
                || date.equals(dateFrom)
                || date.equals(dateTo)
                || (date.after(dateFrom) && date.before(dateTo));
    }
}
