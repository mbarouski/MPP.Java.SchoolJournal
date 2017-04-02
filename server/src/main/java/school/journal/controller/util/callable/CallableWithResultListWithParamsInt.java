package school.journal.controller.util.callable;

import java.util.List;

public interface CallableWithResultListWithParamsInt<T> {
    List<T> call(int i) throws Exception;
}
