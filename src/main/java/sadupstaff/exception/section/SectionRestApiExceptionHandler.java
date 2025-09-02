package sadupstaff.exception.section;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sadupstaff.controller.section.SectionRESTController;
import sadupstaff.exception.ErrorResponse;

@RestControllerAdvice(assignableTypes = SectionRESTController.class)
public class SectionRestApiExceptionHandler {

    @ExceptionHandler(MaxSectionInDistrictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerMaxSectionInDistrictException(MaxSectionInDistrictException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(DeleteSectionException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handlerDeleteSectionException(DeleteSectionException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
