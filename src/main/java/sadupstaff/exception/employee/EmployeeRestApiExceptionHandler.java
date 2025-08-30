package sadupstaff.exception.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sadupstaff.controller.employee.EmployeeRESTController;
import sadupstaff.exception.ErrorResponse;

@RestControllerAdvice(assignableTypes = EmployeeRESTController.class)
public class EmployeeRestApiExceptionHandler {

    @ExceptionHandler(MaxEmployeeInDepartmentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerMaxEmployeeInDepartmentException(MaxEmployeeInDepartmentException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
