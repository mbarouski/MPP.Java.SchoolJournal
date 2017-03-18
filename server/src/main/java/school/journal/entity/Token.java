package school.journal.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Token {
    private String value;
<<<<<<< 28488bca656d04fc5395b1769047d0039713e80e
    private byte active;

    @Id
    @Column(name = "master_id")
    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }
=======
    private Byte active;
    private User user;
>>>>>>> 60d5112bc89062b0dd824662e1516bbfec9be6ad

    @Basic
    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

<<<<<<< 28488bca656d04fc5395b1769047d0039713e80e
    @Basic
    @Column(name = "active")
    public byte getActive() {
=======
    public Byte getActive() {
>>>>>>> 60d5112bc89062b0dd824662e1516bbfec9be6ad
        return active;
    }

    public void setActive(Byte active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (value != null ? !value.equals(token.value) : token.value != null) return false;
        if (active != null ? !active.equals(token.active) : token.active != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (active != null ? active.hashCode() : 0);
        return result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
