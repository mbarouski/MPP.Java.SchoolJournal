package school.journal.entity;

import java.sql.Date;

public class Mark {
    private int markId;
    private Integer value;
    private Enum type;
    private Date date;
    private byte isDeleted;

    public int getMarkId() {
        return markId;
    }

    public void setMarkId(int markId) {
        this.markId = markId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Enum getType() {
        return type;
    }

    public void setType(Enum type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mark mark = (Mark) o;

        if (markId != mark.markId) return false;
        if (isDeleted != mark.isDeleted) return false;
        if (value != null ? !value.equals(mark.value) : mark.value != null) return false;
        if (type != null ? !type.equals(mark.type) : mark.type != null) return false;
        if (date != null ? !date.equals(mark.date) : mark.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = markId;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (int) isDeleted;
        return result;
    }
}
