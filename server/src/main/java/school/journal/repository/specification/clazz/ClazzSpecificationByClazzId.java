package school.journal.repository.specification.clazz;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.Clazz;

public class ClazzSpecificationByClazzId extends ClazzSpecification {

    private int classId;

    public ClazzSpecificationByClazzId(int classId) {
        this.classId = classId;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("classId", this.classId);
    }

    @Override
    public boolean specified(Clazz clazz) {
        return clazz.getClassId()==classId;
    }
}
