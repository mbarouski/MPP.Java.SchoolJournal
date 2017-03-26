package school.journal.repository.specification.teacher;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.Teacher;

public class TeacherSpecificationByClassId extends TeacherSpecification {
    private int classId;

    public TeacherSpecificationByClassId(int classId) {
        this.classId = classId;
    }

    @Override
    public Criterion toCriteria() {
       return Restrictions.eq("class_id", classId);
    }

    @Override
    public boolean specified(Teacher teacher) {
        return teacher.getClassId() == classId;
    }
}
