package school.journal.controller.util.callable;

import java.util.List;

public interface CallableWithResultListWithParamsIntIntInt<T> {
    List<T> call(int a, int b, int c) throws Exception;
}
