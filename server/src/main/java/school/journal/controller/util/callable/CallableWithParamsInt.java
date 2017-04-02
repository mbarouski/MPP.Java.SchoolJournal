package school.journal.controller.util.callable;

public interface CallableWithParamsInt<T> {
    T call(int i) throws Exception;
}
