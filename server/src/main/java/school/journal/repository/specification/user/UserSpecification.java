package school.journal.repository.specification.user;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.User;
import school.journal.repository.specification.HibernateSpecification;
import school.journal.repository.specification.Specification;

public abstract class UserSpecification extends Specification<User> {
}
