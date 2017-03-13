package school.journal.repository.specification.mark;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.Mark;

public class MarkSpecificationBySubjectId extends MarkSpecification {
    private int subjectId;

    public MarkSpecificationBySubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("subject_id", subjectId);
    }

    @Override
    public boolean specified(Mark mark) {
        return mark.getSubjectId() == subjectId;
    }
}
