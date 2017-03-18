package school.journal.entity;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "subject_in_schedule", schema = "school_journal_db", catalog = "")
public class SubjectInSchedule {
    private int subectInScheduleId;
    private int subjectId;
    private Integer teacherId;
    private Enum dayOfWeek;
    private Time beginTime;
    private String place;
    private int classId;

    public void setDayOfWeek(byte dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Id
    @Column(name = "subect_in_schedule_id")
    public int getSubectInScheduleId() {
        return subectInScheduleId;
    }

    public void setSubectInScheduleId(int subectInScheduleId) {
        this.subectInScheduleId = subectInScheduleId;
    }

    @Basic
    @Column(name = "subject_id")
    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    @Basic
    @Column(name = "teacher_id")
    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    @Basic
    @Column(name = "day_of_week")
    public Enum getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Enum dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Basic
    @Column(name = "begin_time")
    public Time getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Time beginTime) {
        this.beginTime = beginTime;
    }

    @Basic
    @Column(name = "place")
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Basic
    @Column(name = "class_id")
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
