package school.journal.service.document.generation;

public enum DocumentType {
    PDF("PDF"),
    CSV("CSV"),
    XLSX("XLSX");

    private String typeName;

    DocumentType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public DocumentType getByTypeName(String typeName) {
        DocumentType[] values = DocumentType.values();
        for(int i = 0; i < values.length; i++) {
            if(values[i].getTypeName().equals(typeName))
                return values[i];
        }
        return null;
    }
}
