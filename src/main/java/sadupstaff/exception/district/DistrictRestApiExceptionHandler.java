package sadupstaff.exception.district;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sadupstaff.controller.district.DistrictRESTController;
import sadupstaff.exception.ErrorResponse;

@RestControllerAdvice(assignableTypes = DistrictRESTController.class)
public class DistrictRestApiExceptionHandler {

    @ExceptionHandler(DeleteDistrictException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handlerDeleteDistrictException(DeleteDistrictException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
