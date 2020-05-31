package resume.microservice.com.ResumeService.exception;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import resume.microservice.com.ResumeService.annotation.EnableFormErrorConvertation;
import resume.microservice.com.ResumeService.annotation.constraints.FieldMatch;
import resume.microservice.com.ResumeService.component.FormErrorConverter;
import resume.microservice.com.ResumeService.util.DataUtil;
import resume.microservice.com.ResumeService.validator.ErrorValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    public ResponseEntity<ErrorMessage> handleException(MethodArgumentNotValidException exc) {

        HttpHeaders headers;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept( Arrays.asList(MediaType.APPLICATION_JSON) );


        ErrorMessage errorResponse = new ErrorMessage();

        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimestamp(new Date());
        errorResponse.setField(exc.getBindingResult().getFieldError().getField());

        // errorResponse.setFieldErrorList(exc.getBindingResult().getFieldErrors());

        String message = exc.getBindingResult().getFieldError().getDefaultMessage();

        if ( ErrorValue.errors.containsKey(message) )
                message = ErrorValue.errors.get(message);
        //bindingResult.addError(new FieldError(formName, fieldName, value, false, objectError.getCodes(), objectError.getArguments(), objectError.getDefaultMessage()));

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
