package school.journal.repository.specification.subjectInSchedule;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.Clazz;
import school.journal.entity.SubjectInSchedule;

public class SubjectInScheduleSpecificationByClass extends SubjectInScheduleSpecification {
    private Clazz clazz;

    public SubjectInScheduleSpecificationByClass(Clazz clazz) {
        this.clazz = clazz;
    }
    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("clazz",clazz);
    }

    @Override
    public boolean specified(SubjectInSchedule subjectInSchedule) {
        return subjectInSchedule.getClazz() == clazz ;
    }
}
