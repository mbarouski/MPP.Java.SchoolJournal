package school.journal.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    private Integer userId;
    private String username;
    private String passHash;
<<<<<<< 28488bca656d04fc5395b1769047d0039713e80e
    private byte locked;
    private String email;

    @Id
    @Column(name = "user_id")
    public int getUserId() {
=======
    private Byte locked;
    private String email;
    private Role role;
    private String password;
    private Integer roleId;

    public Integer getUserId() {
>>>>>>> 60d5112bc89062b0dd824662e1516bbfec9be6ad
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

<<<<<<< 28488bca656d04fc5395b1769047d0039713e80e
    @Basic
    @Column(name = "role_id")
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Basic
    @Column(name = "username")
=======
>>>>>>> 60d5112bc89062b0dd824662e1516bbfec9be6ad
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "pass_hash")
    public String getPassHash() {
        return passHash;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }

<<<<<<< 28488bca656d04fc5395b1769047d0039713e80e
    @Basic
    @Column(name = "locked")
    public byte getLocked() {
=======
    public Byte getLocked() {
>>>>>>> 60d5112bc89062b0dd824662e1516bbfec9be6ad
        return locked;
    }

    public void setLocked(Byte locked) {
        this.locked = locked;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

<<<<<<< 28488bca656d04fc5395b1769047d0039713e80e
=======
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

>>>>>>> 60d5112bc89062b0dd824662e1516bbfec9be6ad
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != null ? !userId.equals(user.userId) : user.userId != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (passHash != null ? !passHash.equals(user.passHash) : user.passHash != null) return false;
        if (locked != null ? !locked.equals(user.locked) : user.locked != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (passHash != null ? passHash.hashCode() : 0);
        result = 31 * result + (locked != null ? locked.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
