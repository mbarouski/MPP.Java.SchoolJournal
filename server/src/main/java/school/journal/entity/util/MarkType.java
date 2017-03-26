package school.journal.entity.util;

public enum MarkType {
    simple("simple"),
    apsent("apsent"),
    control("control"),
    self("self"),
    term("term"),
    year("year");

    final String meaning;

    MarkType(String meaning) {
        this.meaning = meaning;
    }

    public String getMeaning() {
        return meaning;
    }


    @Override
    public String toString() {
        return meaning;
    }
}
