package resume.microservice.exception;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import resume.microservice.component.impl.ErrorValue;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;


@ControllerAdvice
public class AppExceptionsHandler {

    @Autowired
    private ErrorValue errorValue;

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(UserServiceException exc) {

        ErrorMessage errorResponse = new ErrorMessage();

        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatusValue(HttpStatus.BAD_REQUEST.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(MethodArgumentNotValidException exc) throws UnsupportedEncodingException {

        HttpHeaders headers;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept( Arrays.asList(MediaType.APPLICATION_JSON) );


        ErrorMessage errorResponse = new ErrorMessage();

        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimestamp(new Date());
        errorResponse.setField(exc.getBindingResult().getFieldError().getField());

        String message = exc.getBindingResult().getFieldError().getDefaultMessage();


        if ( errorValue.errors.containsKey(message) )
                message = errorValue.errors.get(message);


        errorResponse.setStatusValue(exc.getBindingResult().getFieldError().getField() + " : "+ message);

        return new ResponseEntity<ErrorMessage>(errorResponse, HttpStatus.CONFLICT);
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
    public ResponseEntity<ErrorMessage> handleException(CantCompleteClientRequestException exc) {

        ErrorMessage errorResponse = new ErrorMessage();

        errorResponse.setStatusValue(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimestamp(new Date());


        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(Exception exc) {

        ErrorMessage errorResponse = new ErrorMessage();

        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimestamp(new Date());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

}
