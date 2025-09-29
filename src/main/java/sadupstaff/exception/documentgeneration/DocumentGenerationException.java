package sadupstaff.exception.documentgeneration;

public class DocumentGenerationException extends RuntimeException {
    public DocumentGenerationException() {
        super("Ошибка при геннерации документа, повторите попутку позже");
    }
}
