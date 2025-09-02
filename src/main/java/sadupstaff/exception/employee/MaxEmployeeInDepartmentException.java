package sadupstaff.exception.employee;

public class MaxEmployeeInDepartmentException extends RuntimeException{
    public MaxEmployeeInDepartmentException(String departmentName) {
        super(String.format("В '%s' максимальное количество сотрудников", departmentName));}
}
