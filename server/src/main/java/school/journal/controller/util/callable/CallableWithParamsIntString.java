package school.journal.controller.util.callable;

public interface CallableWithParamsIntString<T> {
    T call(int i, String s) throws Exception;
}

