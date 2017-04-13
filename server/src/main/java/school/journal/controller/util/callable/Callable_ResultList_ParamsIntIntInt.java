package school.journal.controller.util.callable;

import java.util.List;

public interface Callable_ResultList_ParamsIntIntInt<T> {
    List<T> call(int a, int b, int c) throws Exception;
}
