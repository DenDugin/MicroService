package resume.microservice.com.ResumeService.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

// перехват exception
@ControllerAdvice
public class AppExceptionsHandler {

    @ExceptionHandler//(value = {StudentNotFoundException.class})
    // ErrorMessage - type of body response
    public ResponseEntity<ErrorMessage> handleException(UserServiceException exc) {

        ErrorMessage errorResponse = new ErrorMessage();

        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimestamp(new Date());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(FormValidationException exc) {

        ErrorMessage errorResponse = new ErrorMessage();

        errorResponse.setStatus(HttpStatus.CONFLICT.value());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimestamp(new Date());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }



    @ExceptionHandler
    // ErrorMessage - type of body response
    public ResponseEntity<ErrorMessage> handleException(CantCompleteClientRequestException exc) {

        ErrorMessage errorResponse = new ErrorMessage();

        errorResponse.setStatusValue(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimestamp(new Date());


        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }







    // catch all exception
    @ExceptionHandler
    // StudentErrorResponse - type response body
    public ResponseEntity<ErrorMessage> handleException(Exception exc) {

        ErrorMessage errorResponse = new ErrorMessage();

        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimestamp(new Date());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

}
