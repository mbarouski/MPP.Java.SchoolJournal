package school.journal.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Role {
    private Integer roleId;
    private String name;
    private Integer level;

<<<<<<< 28488bca656d04fc5395b1769047d0039713e80e
    @Id
    @Column(name = "role_id")
    public int getRoleId() {
=======
    public Integer getRoleId() {
>>>>>>> 60d5112bc89062b0dd824662e1516bbfec9be6ad
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

<<<<<<< 28488bca656d04fc5395b1769047d0039713e80e
    @Basic
    @Column(name = "level")
    public int getLevel() {
=======
    public Integer getLevel() {
>>>>>>> 60d5112bc89062b0dd824662e1516bbfec9be6ad
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (roleId != null ? !roleId.equals(role.roleId) : role.roleId != null) return false;
        if (name != null ? !name.equals(role.name) : role.name != null) return false;
        if (level != null ? !level.equals(role.level) : role.level != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = roleId != null ? roleId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        return result;
    }
}
