package sadupstaff.exception;

public class SectionNotFoundByNameException extends RuntimeException {
    public SectionNotFoundByNameException(String name) {
        super(String.format("Участок '%s' не найден", name));
    }


}
