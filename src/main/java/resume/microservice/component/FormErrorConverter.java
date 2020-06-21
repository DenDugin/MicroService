package resume.microservice.component;

import org.springframework.validation.BindingResult;


import java.lang.annotation.Annotation;


public interface FormErrorConverter {

	void convertFormErrorToFieldError( Class<? extends Annotation> validationAnnotationClass,  Object formInstance,  BindingResult bindingResult);
}
