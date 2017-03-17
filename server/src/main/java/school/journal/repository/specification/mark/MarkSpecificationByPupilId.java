package school.journal.repository.specification.mark;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.Mark;

public class MarkSpecificationByPupilId extends MarkSpecification {
    private int pupilId;

    public MarkSpecificationByPupilId(int pupilId) {
        this.pupilId = pupilId;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("pupil_id", pupilId);
    }

    @Override
    public boolean specified(Mark mark) {
        return mark.getPupilId() == pupilId;
    }
}
