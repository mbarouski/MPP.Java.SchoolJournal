package school.journal.repository.specification.pupil;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import school.journal.entity.Pupil;

public class PupilSpecificationByLastName extends PupilSpecification {

    private String lastName;

    public PupilSpecificationByLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("last_name", this.lastName);
    }

    @Override
    public boolean specified(Pupil pupil) {
        return pupil.getLastName().equals(lastName);
    }
}
