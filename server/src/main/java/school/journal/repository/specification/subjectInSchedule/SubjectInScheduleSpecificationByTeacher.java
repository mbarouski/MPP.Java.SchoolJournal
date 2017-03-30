package school.journal.repository.specification.subjectInSchedule;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.SubjectInSchedule;
import school.journal.entity.Teacher;

public class SubjectInScheduleSpecificationByTeacher extends SubjectInScheduleSpecification {
    private Teacher teacher;

    public SubjectInScheduleSpecificationByTeacher(Teacher teacher){
        this.teacher = teacher;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("teacher",teacher);
    }

    @Override
    public boolean specified(SubjectInSchedule subjectInSchedule) {
        return subjectInSchedule.getTeacher()==teacher;
    }
}
