package resume.microservice.com.ResumeService.form;



import resume.microservice.com.ResumeService.entity.Hobby;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HobbyForm implements Serializable {
    private static final long serialVersionUID = 4135568197764740034L;
    @Valid
    private List<Hobby> items = new ArrayList<>();

    public HobbyForm() {
        super();
    }

    public HobbyForm(List<Hobby> items) {
        super();
        this.items = items;
    }

    public List<Hobby> getItems() {
        return items;
    }

    public void setItems(List<Hobby> items) {
        this.items = items;
    }


}
