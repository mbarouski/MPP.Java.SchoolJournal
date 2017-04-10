package school.journal.entity;

import java.sql.Time;

public class LessonTime {

    private int lessonId;
    private int number;
    private Time startTime;
    private Time endTime;

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LessonTime)) return false;

        LessonTime that = (LessonTime) o;

        if (getLessonId() != that.getLessonId()) return false;
        if (getNumber() != that.getNumber()) return false;
        if (getStartTime() != null ? !getStartTime().equals(that.getStartTime()) : that.getStartTime() != null)
            return false;
        return getEndTime() != null ? getEndTime().equals(that.getEndTime()) : that.getEndTime() == null;
    }

    @Override
    public int hashCode() {
        int result = getLessonId();
        result = 31 * result + getNumber();
        result = 31 * result + (getStartTime() != null ? getStartTime().hashCode() : 0);
        result = 31 * result + (getEndTime() != null ? getEndTime().hashCode() : 0);
        return result;
    }
}
