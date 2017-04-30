package school.journal.controller.util.callable;

import java.util.List;

public interface CallableWithResultList<T> {
    List<T> call() throws Exception;
}
