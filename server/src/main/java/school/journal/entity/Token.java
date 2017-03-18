package school.journal.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Token {
    private int masterId;
    private String value;
    private byte active;

    @Id
    @Column(name = "master_id")
    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    @Basic
    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Basic
    @Column(name = "active")
    public byte getActive() {
        return active;
    }

    public void setActive(byte active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (masterId != token.masterId) return false;
        if (active != token.active) return false;
        if (value != null ? !value.equals(token.value) : token.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = masterId;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (int) active;
        return result;
    }
}
