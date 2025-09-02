package sadupstaff.exception.department;

public class DeleteDepartmentException extends RuntimeException{
    public DeleteDepartmentException(String name) {
        super(String.format("%s имеет сотрудников", name));
    }
}
