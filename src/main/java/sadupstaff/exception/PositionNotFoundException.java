package sadupstaff.exception;

public class PositionNotFoundException extends RuntimeException {
    public PositionNotFoundException(String position) {
        super(String.format("Должности '%s' не существует", position));
    }


}
