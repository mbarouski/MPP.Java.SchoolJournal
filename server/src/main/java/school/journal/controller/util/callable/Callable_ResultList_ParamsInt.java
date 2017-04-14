package school.journal.controller.util.callable;

import java.util.List;

public interface Callable_ResultList_ParamsInt<T> {
    List<T> call(int i) throws Exception;
}
