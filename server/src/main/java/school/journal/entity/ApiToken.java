package school.journal.entity;

public class ApiToken {
    private int userId;
    private String value;
    private byte isActive;
    private String lastUse;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public byte getIsActive() {
        return isActive;
    }

    public void setIsActive(byte isActive) {
        this.isActive = isActive;
    }

    public String getLastUse() {
        return lastUse;
    }

    public void setLastUse(String lastUse) {
        this.lastUse = lastUse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiToken apiToken = (ApiToken) o;

        if (userId != apiToken.userId) return false;
        if (isActive != apiToken.isActive) return false;
        if (value != null ? !value.equals(apiToken.value) : apiToken.value != null) return false;
        if (lastUse != null ? !lastUse.equals(apiToken.lastUse) : apiToken.lastUse != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (int) isActive;
        result = 31 * result + (lastUse != null ? lastUse.hashCode() : 0);
        return result;
    }
}
