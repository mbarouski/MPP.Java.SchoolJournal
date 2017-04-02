package school.journal.controller.util.callable;

public interface CallableWithParamsIntInt<T> {
    T call(int a, int b) throws Exception;
}
