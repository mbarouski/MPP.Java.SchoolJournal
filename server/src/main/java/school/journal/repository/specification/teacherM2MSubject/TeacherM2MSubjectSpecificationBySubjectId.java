package school.journal.repository.specification.teacherM2MSubject;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.TeacherM2MSubject;

public class TeacherM2MSubjectSpecificationBySubjectId extends TeacherM2MSubjectSpecification {
    private int subjectId;

    public TeacherM2MSubjectSpecificationBySubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("subject_id", subjectId);
    }

    @Override
    public boolean specified(TeacherM2MSubject teacherM2MSubject) {
        return teacherM2MSubject.getSubjectId() == subjectId;
    }
}
