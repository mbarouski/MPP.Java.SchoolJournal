package school.journal.repository.specification.mark;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class MarkSpecificationByMarkId extends MarkSpecification {

    private int markId;

    public MarkSpecificationByMarkId(int markId) {
        this.markId = markId;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("markId", this.markId);
    }

    @Override
    public boolean specified(Mark mark) {
        return mark.getMarkId() == markId;
    }
}
