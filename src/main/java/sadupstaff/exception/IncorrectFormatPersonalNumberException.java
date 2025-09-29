package sadupstaff.exception;

public class IncorrectFormatPersonalNumberException extends RuntimeException {
    public IncorrectFormatPersonalNumberException(String personalNumber) {

        super(String.format("Неверный формат персональлного номера участка: %s. " +
                "Пример верного формата: 54MS0111", personalNumber));
    }
}
