package sadupstaff.exception;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(String departmentName) {
        super(String.format("Отдела '%s' не существует", departmentName));
    }


}
