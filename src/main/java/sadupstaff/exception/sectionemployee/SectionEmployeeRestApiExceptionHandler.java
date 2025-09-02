package sadupstaff.exception.sectionemployee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sadupstaff.controller.sectionemployee.SectionEmployeeRESTController;
import sadupstaff.exception.ErrorResponse;

@RestControllerAdvice(assignableTypes = SectionEmployeeRESTController.class)
public class SectionEmployeeRestApiExceptionHandler {

    @ExceptionHandler(MaxEmployeeInSectionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerMaxEmployeeInSectionException(MaxEmployeeInSectionException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
