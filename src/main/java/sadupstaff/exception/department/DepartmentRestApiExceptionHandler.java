package sadupstaff.exception.department;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sadupstaff.controller.department.DepartmentRESTController;
import sadupstaff.exception.*;

@RestControllerAdvice(assignableTypes = DepartmentRESTController.class)
public class DepartmentRestApiExceptionHandler {

    @ExceptionHandler(DeleteDepartmentException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handlerDeleteDepartmentException(DeleteDepartmentException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
