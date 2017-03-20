package school.journal.entity;

public class Clazz {
    private Integer classId;
    private Integer number;
    private String letterMark;

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getLetterMark() {
        return letterMark;
    }

    public void setLetterMark(String letterMark) {
        this.letterMark = letterMark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Clazz clazz = (Clazz) o;

        if (classId != null ? !classId.equals(clazz.classId) : clazz.classId != null) return false;
        if (number != null ? !number.equals(clazz.number) : clazz.number != null) return false;
        if (letterMark != null ? !letterMark.equals(clazz.letterMark) : clazz.letterMark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = classId != null ? classId.hashCode() : 0;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (letterMark != null ? letterMark.hashCode() : 0);
        return result;
    }
}
