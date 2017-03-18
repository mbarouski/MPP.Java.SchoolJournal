package school.journal.repository.specification.role;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class RoleSpecificationByRoleId extends RoleSpecification {
    private int roleId;

    public RoleSpecificationByRoleId(int roleId){
        this.roleId = roleId;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("roleId", this.roleId);
    }

    @Override
    public boolean specified(Role role) {
        return role.getRoleId() == this.roleId;
    }
}
