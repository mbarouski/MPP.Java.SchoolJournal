package school.journal.repository.specification;

import org.hibernate.criterion.Criterion;

public interface HibernateSpecification {
    Criterion toCriteria();
}
