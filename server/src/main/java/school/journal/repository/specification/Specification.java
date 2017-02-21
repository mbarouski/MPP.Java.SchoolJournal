package school.journal.repository.specification;

public abstract class Specification<T> implements HibernateSpecification {
    public abstract boolean specified(T t);
}
