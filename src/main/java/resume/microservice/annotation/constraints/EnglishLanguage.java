package resume.microservice.annotation.constraints;

import resume.microservice.validator.EnglishLanguageConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { EnglishLanguageConstraintValidator.class })
public @interface EnglishLanguage {

	String message() default "EnglishLanguage";
	// 0123456789
	boolean withNumbers() default false;

	//.,?!-:()'"[]{}; \t\n
	boolean withPunctuations() default true;

	//~#$%^&*-+=_\\|/@`!'\";:><,.?{}
	boolean withSpechSymbols() default true;

	Class<? extends Payload>[] payload() default { };

	Class<?>[] groups() default { };
}
