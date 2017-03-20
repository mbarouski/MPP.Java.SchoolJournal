package school.journal.entity;

import java.sql.Time;

public class SubjectInSchedule {
    private Integer subectInScheduleId;
    private Short dayOfWeek;
    private Time beginTime;
    private String place;
    private Subject subject;
    private Teacher teacher;
    private Clazz clazz;

    public Integer getSubectInScheduleId() {
        return subectInScheduleId;
    }

    public void setSubectInScheduleId(Integer subectInScheduleId) {
        this.subectInScheduleId = subectInScheduleId;
    }

    public Short getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Short dayOfWeek) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubjectInSchedule that = (SubjectInSchedule) o;

        if (subectInScheduleId != null ? !subectInScheduleId.equals(that.subectInScheduleId) : that.subectInScheduleId != null)
            return false;
        if (dayOfWeek != null ? !dayOfWeek.equals(that.dayOfWeek) : that.dayOfWeek != null) return false;
        if (beginTime != null ? !beginTime.equals(that.beginTime) : that.beginTime != null) return false;
        if (place != null ? !place.equals(that.place) : that.place != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subectInScheduleId != null ? subectInScheduleId.hashCode() : 0;
        result = 31 * result + (dayOfWeek != null ? dayOfWeek.hashCode() : 0);
        result = 31 * result + (beginTime != null ? beginTime.hashCode() : 0);
        result = 31 * result + (place != null ? place.hashCode() : 0);
        return result;
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

    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
    }
}
