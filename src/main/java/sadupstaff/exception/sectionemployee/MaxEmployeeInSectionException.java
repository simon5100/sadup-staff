package sadupstaff.exception.sectionemployee;

public class MaxEmployeeInSectionException extends RuntimeException{
    public MaxEmployeeInSectionException(String sectionName) {
        super(String.format("В '%s' максимальное количество сотрудников", sectionName));}
}
