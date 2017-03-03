package school.journal.entity;

public interface IId {
    int getId();
    void setId();

    static <T> T create();
}
