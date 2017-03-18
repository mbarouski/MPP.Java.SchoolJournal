package school.journal.repository.specification.user;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class UserSpecificationByUsername extends UserSpecification {
    private String username;

    public UserSpecificationByUsername(String username){
        this.username = username;
    }

    @Override
    public boolean specified(User user) {
        return user.getUsername().equals(this.username);
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.ilike("username", this.username);
    }
}
