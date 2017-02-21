package school.journal.repository.specification.teacherM2MSubject;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.TeacherM2MSubject;

public class TeacherM2MSubjectSpecificationByTeacherId extends TeacherM2MSubjectSpecification {
    private int teacherId;

    public TeacherM2MSubjectSpecificationByTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("teacher_id", teacherId);
    }

    @Override
    public boolean specified(TeacherM2MSubject teacherM2MSubject) {
        return teacherM2MSubject.getTeacherId() == teacherId;
    }
}
