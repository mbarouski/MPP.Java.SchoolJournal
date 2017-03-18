package school.journal.repository.specification.user;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class UserSpecificationByUserId extends UserSpecification {
    private int userId;

    public UserSpecificationByUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("user_id", this.userId);
    }

    @Override
    public boolean specified(User user) {
        return user.getUserId() == this.userId;
    }
}
