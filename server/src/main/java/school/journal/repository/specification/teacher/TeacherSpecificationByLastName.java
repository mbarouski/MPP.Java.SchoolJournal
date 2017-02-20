package school.journal.repository.specification.teacher;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.Teacher;

public class TeacherSpecificationByLastName extends TeacherSpecification {
    private String lastName;

    public TeacherSpecificationByLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("last_name",lastName);
    }

    @Override
    public boolean specified(Teacher teacher) {
        return teacher.getLastName().equals(lastName);
    }
}
