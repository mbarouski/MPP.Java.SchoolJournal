package school.journal.repository.specification.mark;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.Mark;
import school.journal.entity.Pupil;

public class MarkSpecificationByPupil extends MarkSpecification {
    private Pupil pupil;

    public MarkSpecificationByPupil(Pupil pupil) {
        this.pupil = pupil;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("pupil", pupil);
    }

    @Override
    public boolean specified(Mark mark) {
        return mark.getPupil() == pupil;
    }
}
