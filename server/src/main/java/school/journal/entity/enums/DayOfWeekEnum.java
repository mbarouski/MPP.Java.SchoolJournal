package school.journal.entity.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum DayOfWeekEnum{
    Monday(1),
    Tuesday(2),
    Wednesday(3),
    Thursday(4),
    Friday(5),
    Saturday(6);

    private int id;

    DayOfWeekEnum(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
