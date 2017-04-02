package school.journal.controller.util.callable;

public interface CallableWithTParam<T> {
    abstract T call(T obj) throws Exception;
}
