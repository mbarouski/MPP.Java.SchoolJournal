package school.journal.controller.util.callable;

import java.util.List;

public interface CallableWithResultListWithParamsIntInt<T> {
    List<T> call(int a, int b) throws Exception;
}
