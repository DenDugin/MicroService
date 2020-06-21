package resume.microservice.form;



import resume.microservice.entity.Language;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


public class LanguageForm {
	@Valid
	private List<Language> items = new ArrayList<>();
	
	public LanguageForm() {
		super();
	}

	public LanguageForm(List<Language> items) {
		super();
		this.items = items;
	}

	public List<Language> getItems() {
		return items;
	}

	public void setItems(List<Language> items) {
		this.items = items;
	}
}
