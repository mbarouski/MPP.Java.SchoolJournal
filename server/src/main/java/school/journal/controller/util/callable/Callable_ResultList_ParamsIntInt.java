package school.journal.controller.util.callable;

import java.util.List;

public interface Callable_ResultList_ParamsIntInt<T> {
    List<T> call(int a, int b) throws Exception;
}
