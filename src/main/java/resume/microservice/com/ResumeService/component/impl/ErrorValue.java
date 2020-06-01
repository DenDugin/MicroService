package resume.microservice.com.ResumeService.component.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;


@Component
@PropertySource(value={"application-errors.properties"})
public class ErrorValue {

    @Value("#{${simple.map}}")
    public HashMap<String,String> errors;

}
