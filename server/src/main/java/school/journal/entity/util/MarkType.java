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

    public static MarkType fromMeaning(String meaning) {
        for (MarkType type : MarkType.values()) {
            if (type.getMeaning().equalsIgnoreCase(meaning)) {
                return type;
            }
        }
        throw new UnsupportedOperationException("The meaning " + meaning + "is unsupported");
    }

}
