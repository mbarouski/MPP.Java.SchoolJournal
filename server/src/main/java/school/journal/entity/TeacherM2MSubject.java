package school.journal.entity;

public class TeacherM2MSubject {
    private int teacherId;
    private int subjectId;
    private Teacher teacherByTeacherId;
    private Subject subjectBySubjectId;

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeacherM2MSubject that = (TeacherM2MSubject) o;

        if (teacherId != that.teacherId) return false;
        if (subjectId != that.subjectId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = teacherId;
        result = 31 * result + subjectId;
        return result;
    }

    public Teacher getTeacherByTeacherId() {
        return teacherByTeacherId;
    }

    public void setTeacherByTeacherId(Teacher teacherByTeacherId) {
        this.teacherByTeacherId = teacherByTeacherId;
    }

    public Subject getSubjectBySubjectId() {
        return subjectBySubjectId;
    }

    public void setSubjectBySubjectId(Subject subjectBySubjectId) {
        this.subjectBySubjectId = subjectBySubjectId;
    }
}
