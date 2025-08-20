package sadupstaff.exception.district;

public class DeleteDistrictException extends RuntimeException{
    public DeleteDistrictException(String name) {
        super(String.format("%s содержиь участки, удаление запрещено", name));
    }
}
