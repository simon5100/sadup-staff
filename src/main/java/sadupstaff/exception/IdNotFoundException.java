package sadupstaff.exception;

public class IdNotFoundException extends RuntimeException{
    public IdNotFoundException(String id) {
        super(String.format("Id '%s' не найден", id));}

}
