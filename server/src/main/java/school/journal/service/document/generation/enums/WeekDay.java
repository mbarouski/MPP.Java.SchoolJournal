package school.journal.service.document.generation.enums;

public enum WeekDay {
    sunday("Воскресенье"),
    monday("Понедельник"),
    tuesday("Вторник"),
    wednesday("Среда"),
    thursday("Четверг"),
    friday("Пятница"),
    saturday ("Суббота");

    private String value;

    WeekDay(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
