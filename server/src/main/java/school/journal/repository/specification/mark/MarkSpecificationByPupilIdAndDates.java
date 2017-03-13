package school.journal.repository.specification.mark;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.Mark;
import school.journal.repository.specification.Specification;

import java.util.Date;

public class MarkSpecificationByPupilIdAndDates extends MarkSpecification {
    private int pupilId;
    private Date dateFrom;
    private Date dateTo;

    public MarkSpecificationByPupilIdAndDates(int pupilId, Date dateFrom, Date dateTo) {
        this.pupilId = pupilId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.and(
                Restrictions.eq("pupil_id", pupilId),
                Restrictions.between("date", dateFrom, dateTo));
    }

    @Override
    public boolean specified(Mark mark) {
        return mark.getPupilId() == pupilId
                && dateFrom.before(mark.getDate())
                && dateTo.after(mark.getDate());
    }
}
