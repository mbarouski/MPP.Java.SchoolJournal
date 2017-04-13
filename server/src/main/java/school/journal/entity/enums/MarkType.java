package school.journal.entity.enums;

public enum MarkType {
    _(0),
    simple(1),
    apsent(2),
    control(3),
    self(4),
    term(5),
    year(6);

    final int meaning;

    MarkType(int meaning) {
        this.meaning = meaning;
    }

    public int getMeaning() {
        return meaning;
    }


}
