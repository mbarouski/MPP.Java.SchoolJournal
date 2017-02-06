package school.journal.entity;

import java.io.Serializable;

public class TeacherM2MSubjectPK implements Serializable {
    private int teacherId;
    private int subjectId;

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

        TeacherM2MSubjectPK that = (TeacherM2MSubjectPK) o;

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
}
