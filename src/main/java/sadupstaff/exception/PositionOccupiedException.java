package sadupstaff.exception;

public class PositionOccupiedException extends RuntimeException {
    public PositionOccupiedException(String position) {
        super(String.format("Позиция '%s' уже занята", position));
    }
}
