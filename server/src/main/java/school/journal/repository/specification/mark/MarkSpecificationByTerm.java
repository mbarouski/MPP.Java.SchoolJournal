package school.journal.repository.specification.mark;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.Mark;
import school.journal.entity.enums.MarkType;

import static school.journal.entity.enums.MarkType.year;

public class MarkSpecificationByTerm extends MarkSpecification {

//    private Date dateFrom;
//    private Date dateTo;
    private Integer termNumber;

    public MarkSpecificationByTerm(Integer termNumber) {
//        this.dateFrom = term.getStart();
//        this.dateTo = new Date(term.getEnd().getTime()+MILLISECONDS_IN_DAY);
        this.termNumber = termNumber;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.or(
                Restrictions.eq("termNumber", termNumber),
                Restrictions.or(
                        Restrictions.eq("type", MarkType.term),
                        Restrictions.eq("type", year)));
//        return Restrictions.or(
//                Restrictions.between("date", dateFrom, dateTo),
//                Restrictions.or(
//                        Restrictions.eq("type", MarkType.term),
//                        Restrictions.eq("type", MarkType.year)));
    }

    @Override
    public boolean specified(Mark mark) {
        return (mark.getTermNumber().intValue() == termNumber
                && mark.getType() == MarkType.term)
                || mark.getType() == year;

//        Date date = mark.getDate();
//        return date.equals(dateFrom) || date.equals(dateTo) || date.after(dateFrom) && date.before(dateTo);
    }
}
