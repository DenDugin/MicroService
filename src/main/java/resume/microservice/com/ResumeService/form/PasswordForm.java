package resume.microservice.com.ResumeService.form;

import resume.microservice.com.ResumeService.annotation.constraints.FieldMatch;
import resume.microservice.com.ResumeService.annotation.constraints.PasswordStrength;
import resume.microservice.com.ResumeService.annotation.EnableFormErrorConvertation;


@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
@EnableFormErrorConvertation(formName="passwordForm", fieldReference="confirmPassword", validationAnnotationClass=FieldMatch.class)
public class PasswordForm {


	@PasswordStrength
	private String password;
	
	private String confirmPassword;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
