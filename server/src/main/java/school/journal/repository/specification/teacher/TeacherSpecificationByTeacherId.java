package school.journal.repository.specification.teacher;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.Teacher;

public class TeacherSpecificationByTeacherId extends TeacherSpecification {
    private int teacherId;

    public TeacherSpecificationByTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("teacher_id", teacherId);
    }

    @Override
    public boolean specified(Teacher teacher) {
        return teacher.getTeacherId() == teacherId;
    }
}
