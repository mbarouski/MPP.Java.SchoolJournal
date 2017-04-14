package school.journal.controller.util.callable;

import java.util.List;

public interface Callable_ResultList<T> {
    List<T> call() throws Exception;
}
