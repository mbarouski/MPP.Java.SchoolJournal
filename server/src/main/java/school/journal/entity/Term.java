package school.journal.entity;

import java.sql.Date;

public class Term {
    private Integer termId;
    private Date start;
    private Date end;
    private Integer number;

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Term term = (Term) o;

        if (!termId.equals(term.termId)) return false;
        if (!start.equals(term.start)) return false;
        return end.equals(term.end);
    }

    @Override
    public int hashCode() {
        int result = termId.hashCode();
        result = 31 * result + start.hashCode();
        result = 31 * result + end.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Term{" +
                "termId=" + termId +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
