package school.journal.entity.enums;

public enum RoleEnum {
    PUPIL(1),
    TEACHER(2),
    CLASS_TEACHER(3),
    DIRECTOR_OF_STUDIES(4),
    DIRECTOR(5),
    ADMIN(6);

    private int id;

    RoleEnum(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
