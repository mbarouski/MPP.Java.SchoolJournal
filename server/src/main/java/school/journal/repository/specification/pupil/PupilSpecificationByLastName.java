package school.journal.repository.specification.pupil;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class PupilSpecificationByLastName extends PupilSpecification {

    private String lastName;

    public PupilSpecificationByLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.ilike("last_name", this.lastName);
    }

    @Override
    public boolean specified(Pupil pupil) {
        return pupil.getLastName().equals(lastName);
    }
}
