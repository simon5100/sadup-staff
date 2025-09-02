package sadupstaff.exception.section;

public class MaxSectionInDistrictException extends RuntimeException{
    public MaxSectionInDistrictException(String districtName) {
        super(String.format("В '%s' максимальное количество участков", districtName));}
}
