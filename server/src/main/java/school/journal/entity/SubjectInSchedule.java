package school.journal.entity;

import java.sql.Time;

public class SubjectInSchedule {
    private int subectInScheduleId;
    private int subjectId;
    private Integer teacherId;
    private Enum dayOfWeek;
    private Time beginTime;
    private String place;
    private int classId;

    public int getSubectInScheduleId() {
        return subectInScheduleId;
    }

    public void setSubectInScheduleId(int subectInScheduleId) {
        this.subectInScheduleId = subectInScheduleId;
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

    public Enum getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Enum dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Time getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Time beginTime) {
        this.beginTime = beginTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubjectInSchedule that = (SubjectInSchedule) o;

        if (subectInScheduleId != that.subectInScheduleId) return false;
        if (subjectId != that.subjectId) return false;
        if (classId != that.classId) return false;
        if (teacherId != null ? !teacherId.equals(that.teacherId) : that.teacherId != null) return false;
        if (dayOfWeek != null ? !dayOfWeek.equals(that.dayOfWeek) : that.dayOfWeek != null) return false;
        if (beginTime != null ? !beginTime.equals(that.beginTime) : that.beginTime != null) return false;
        if (place != null ? !place.equals(that.place) : that.place != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subectInScheduleId;
        result = 31 * result + subjectId;
        result = 31 * result + (teacherId != null ? teacherId.hashCode() : 0);
        result = 31 * result + (dayOfWeek != null ? dayOfWeek.hashCode() : 0);
        result = 31 * result + (beginTime != null ? beginTime.hashCode() : 0);
        result = 31 * result + (place != null ? place.hashCode() : 0);
        result = 31 * result + classId;
        return result;
    }
}
