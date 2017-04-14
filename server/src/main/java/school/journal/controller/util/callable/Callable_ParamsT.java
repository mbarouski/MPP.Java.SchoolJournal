package school.journal.controller.util.callable;

public interface Callable_ParamsT<T> {
    abstract T call(T obj) throws Exception;
}
