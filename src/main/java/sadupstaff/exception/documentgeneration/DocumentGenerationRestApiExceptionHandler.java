package sadupstaff.exception.documentgeneration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sadupstaff.controller.documentgeneration.DocumentGenerationController;
import sadupstaff.exception.ErrorResponse;

@RestControllerAdvice(assignableTypes = DocumentGenerationController.class)
public class DocumentGenerationRestApiExceptionHandler {

    @ExceptionHandler(DocumentGenerationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleDocumentGenerationException(DocumentGenerationException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(IncorrectNAMEFormatException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleIncorrectNAMEFormatException(IncorrectNAMEFormatException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
