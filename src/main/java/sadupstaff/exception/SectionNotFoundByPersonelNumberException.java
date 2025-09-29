package sadupstaff.exception;

public class SectionNotFoundByPersonelNumberException extends RuntimeException {
    public SectionNotFoundByPersonelNumberException(String personelNumber) {
        super(String.format("Участок '%s' не найден", personelNumber));
    }


}
