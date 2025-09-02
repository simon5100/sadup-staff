package sadupstaff.exception;

public class DistrictNotFoundException extends RuntimeException {
    public DistrictNotFoundException(String districtName) {
        super(String.format("Района '%s' не существует", districtName));
    }


}
