package resume.microservice.com.ResumeService.Form;

import resume.microservice.com.ResumeService.entity.Profile;


public class InfoForm {

	//  @EnglishLanguage
	//  @SafeHtml
	private String info;

	public InfoForm() {
		super();
	}

	public InfoForm(String info) {
		super();
		this.info = info;
	}
	
	public InfoForm(Profile profile) {
		super();
		this.info = profile.getInfo();
	}

	public String getInfo() {
		return info != null ? info.trim() : null;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
