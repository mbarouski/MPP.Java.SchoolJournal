package school.journal.entity;

import java.sql.Time;

public class SubjectInSchedule {
    private int subectInScheduleId;
    private Enum dayOfWeek;
    private Time beginTime;
    private String place;
    private byte isDeleted;
    private Subject subjectBySubjectId;
    private Teacher teacherByTeacherId;
    private Clazz clazzByClassId;

    public int getSubectInScheduleId() {
        return subectInScheduleId;
    }

    public void setSubectInScheduleId(int subectInScheduleId) {
        this.subectInScheduleId = subectInScheduleId;
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

        SubjectInSchedule that = (SubjectInSchedule) o;

        if (subectInScheduleId != that.subectInScheduleId) return false;
        if (isDeleted != that.isDeleted) return false;
        if (dayOfWeek != null ? !dayOfWeek.equals(that.dayOfWeek) : that.dayOfWeek != null) return false;
        if (beginTime != null ? !beginTime.equals(that.beginTime) : that.beginTime != null) return false;
        if (place != null ? !place.equals(that.place) : that.place != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subectInScheduleId;
        result = 31 * result + (dayOfWeek != null ? dayOfWeek.hashCode() : 0);
        result = 31 * result + (beginTime != null ? beginTime.hashCode() : 0);
        result = 31 * result + (place != null ? place.hashCode() : 0);
        result = 31 * result + (int) isDeleted;
        return result;
    }

    public Subject getSubjectBySubjectId() {
        return subjectBySubjectId;
    }

    public void setSubjectBySubjectId(Subject subjectBySubjectId) {
        this.subjectBySubjectId = subjectBySubjectId;
    }

    public Teacher getTeacherByTeacherId() {
        return teacherByTeacherId;
    }

    public void setTeacherByTeacherId(Teacher teacherByTeacherId) {
        this.teacherByTeacherId = teacherByTeacherId;
    }

    public Clazz getClazzByClassId() {
        return clazzByClassId;
    }

    public void setClazzByClassId(Clazz clazzByClassId) {
        this.clazzByClassId = clazzByClassId;
    }
}
