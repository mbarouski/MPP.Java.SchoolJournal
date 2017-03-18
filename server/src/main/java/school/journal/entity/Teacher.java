package school.journal.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Teacher {
    private int teacherId;
    private String phoneNumber;
    private Integer classId;
    private String firstName;
    private String pathronymic;
    private String lastName;
    private String description;

    @Id
    @Column(name = "teacher_id")
    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    @Basic
    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "class_id")
    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    @Basic
    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "pathronymic")
    public String getPathronymic() {
        return pathronymic;
    }

    public void setPathronymic(String pathronymic) {
        this.pathronymic = pathronymic;
    }

    @Basic
    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "description")
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

        Teacher teacher = (Teacher) o;

        if (teacherId != teacher.teacherId) return false;
        if (phoneNumber != null ? !phoneNumber.equals(teacher.phoneNumber) : teacher.phoneNumber != null) return false;
        if (classId != null ? !classId.equals(teacher.classId) : teacher.classId != null) return false;
        if (firstName != null ? !firstName.equals(teacher.firstName) : teacher.firstName != null) return false;
        if (pathronymic != null ? !pathronymic.equals(teacher.pathronymic) : teacher.pathronymic != null) return false;
        if (lastName != null ? !lastName.equals(teacher.lastName) : teacher.lastName != null) return false;
        if (description != null ? !description.equals(teacher.description) : teacher.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = teacherId;
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (classId != null ? classId.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (pathronymic != null ? pathronymic.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
