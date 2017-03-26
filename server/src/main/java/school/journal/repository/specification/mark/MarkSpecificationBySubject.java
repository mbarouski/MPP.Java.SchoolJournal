package school.journal.repository.specification.mark;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.Mark;
import school.journal.entity.Subject;

public class MarkSpecificationBySubject extends MarkSpecification {
    private Subject subject;

    public MarkSpecificationBySubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("subject", subject);
    }

    @Override
    public boolean specified(Mark mark) {
        return mark.getSubject() == subject;
    }
}
