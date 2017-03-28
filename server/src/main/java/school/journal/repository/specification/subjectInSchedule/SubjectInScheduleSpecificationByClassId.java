package school.journal.repository.specification.subjectInSchedule;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.SubjectInSchedule;

public class SubjectInScheduleSpecificationByClassId extends SubjectInScheduleSpecification {
    private int classId;

    public SubjectInScheduleSpecificationByClassId(int classId) {
        this.classId = classId;
    }
    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("class_id",classId);
    }

    @Override
    public boolean specified(SubjectInSchedule subjectInSchedule) {
        return subjectInSchedule.getClazz().getClassId() == classId ;
    }
}
