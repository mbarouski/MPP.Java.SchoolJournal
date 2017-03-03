package school.journal.entity;

import java.sql.Date;

public class Mark implements IId<Mark>{
    private int markId;
    private int pupilId;
    private Integer value;
    private Enum type;
    private Date date;
    private int subjectId;
    private Integer teacherId;


    public int getMarkId() {
        return markId;
    }

    public void setMarkId(int markId) {
        this.markId = markId;
    }

    public int getPupilId() {
        return pupilId;
    }

    public void setPupilId(int pupilId) {
        this.pupilId = pupilId;
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

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mark mark = (Mark) o;

        if (markId != mark.markId) return false;
        if (pupilId != mark.pupilId) return false;
        if (subjectId != mark.subjectId) return false;
        if (value != null ? !value.equals(mark.value) : mark.value != null) return false;
        if (type != null ? !type.equals(mark.type) : mark.type != null) return false;
        if (date != null ? !date.equals(mark.date) : mark.date != null) return false;
        if (teacherId != null ? !teacherId.equals(mark.teacherId) : mark.teacherId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = markId;
        result = 31 * result + pupilId;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + subjectId;
        result = 31 * result + (teacherId != null ? teacherId.hashCode() : 0);
        return result;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setId() {

    }

    public Mark create(){
        return new Mark();
    }
}
