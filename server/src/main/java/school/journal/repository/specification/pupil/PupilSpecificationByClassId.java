package school.journal.repository.specification.pupil;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.Pupil;

public class PupilSpecificationByClassId extends PupilSpecification {

    private int classId;

    public PupilSpecificationByClassId(int classId) {
        this.classId = classId;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("classId", classId);
    }

    @Override
    public boolean specified(Pupil pupil) {
        return pupil.getClassId() == classId;
    }
}
