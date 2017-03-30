package school.journal.repository.specification.pupil;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.Pupil;

public class PupilSpecificationByPupilId extends PupilSpecification {

    private int pupilId;

    public PupilSpecificationByPupilId(int pupilId) {
        this.pupilId = pupilId;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("userId", this.pupilId);
    }

    @Override
    public boolean specified(Pupil pupil) {
        return pupil.getUserId() == pupilId;
    }
}
