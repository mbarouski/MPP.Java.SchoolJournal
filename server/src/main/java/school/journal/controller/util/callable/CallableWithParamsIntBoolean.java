package school.journal.controller.util.callable;

public interface CallableWithParamsIntBoolean<T> {
    T call(int i, boolean f) throws Exception;
}
