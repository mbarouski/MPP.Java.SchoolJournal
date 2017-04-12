package school.journal.controller.util.callable;

public interface CallableWithoutParams<T> {
    T call() throws Exception;
}
