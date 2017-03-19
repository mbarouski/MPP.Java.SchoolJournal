package school.journal.entity;

import java.sql.Date;

public class Mark {
    private Integer markId;
    private Integer value;
    private Short type;
    private Date date;
    private Pupil pupil;
    private Subject subject;
    private Teacher teacher;

    public Integer getMarkId() {
        return markId;
    }

    public void setMarkId(Integer markId) {
        this.markId = markId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mark mark = (Mark) o;

        if (markId != null ? !markId.equals(mark.markId) : mark.markId != null) return false;
        if (value != null ? !value.equals(mark.value) : mark.value != null) return false;
        if (type != null ? !type.equals(mark.type) : mark.type != null) return false;
        if (date != null ? !date.equals(mark.date) : mark.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = markId != null ? markId.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    public Pupil getPupil() {
        return pupil;
    }

    public void setPupil(Pupil pupil) {
        this.pupil = pupil;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
