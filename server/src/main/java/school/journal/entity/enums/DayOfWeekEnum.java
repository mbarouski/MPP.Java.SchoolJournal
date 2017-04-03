package school.journal.entity.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum DayOfWeekEnum{
    mon(1),
    tue(2),
    wed(3),
    thu(4),
    fri(5),
    sat(6);

    private int id;

    DayOfWeekEnum(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
