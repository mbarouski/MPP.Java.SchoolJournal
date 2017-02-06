package school.journal.entity;

public class Subject {
    private int subjectId;
    private String name;
    private byte isDeleted;
    private String description;

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subject subject = (Subject) o;

        if (subjectId != subject.subjectId) return false;
        if (isDeleted != subject.isDeleted) return false;
        if (name != null ? !name.equals(subject.name) : subject.name != null) return false;
        if (description != null ? !description.equals(subject.description) : subject.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subjectId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) isDeleted;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
