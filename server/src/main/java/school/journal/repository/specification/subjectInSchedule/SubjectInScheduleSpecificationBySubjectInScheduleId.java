package school.journal.repository.specification.subjectInSchedule;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class SubjectInScheduleSpecificationBySubjectInScheduleId extends SubjectInScheduleSpecification {
    private int subjectInScheduleId;

    public SubjectInScheduleSpecificationBySubjectInScheduleId(int subjectInScheduleId) {
        this.subjectInScheduleId = subjectInScheduleId;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("subject_in_schedule_id", subjectInScheduleId);
    }

    @Override
    public boolean specified(SubjectInSchedule subjectInSchedule) {
        return subjectInSchedule.getSubectInScheduleId() == subjectInScheduleId;
    }
}
