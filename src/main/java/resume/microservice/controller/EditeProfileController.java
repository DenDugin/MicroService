package resume.microservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import resume.microservice.entity.Hobby;
import resume.microservice.form.*;
import resume.microservice.entity.Profile;
import resume.microservice.entity.Skill;
import resume.microservice.exception.CantCompleteClientRequestException;
import resume.microservice.exception.FormValidationException;
import resume.microservice.service.EditProfileService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/profile")
public class EditeProfileController {


    @Autowired
    private EditProfileService editProfileService;

    @GetMapping(value = "/skills", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Skill> getEditSkill(@RequestParam long id) {
        return editProfileService.listSkills(id);
    }


    @PostMapping(value = "/skills", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity  postEditSkill(@RequestBody SkillForm skillForm, @RequestParam long id) {

      if (skillForm == null)
            throw new CantCompleteClientRequestException("SkillForm is null");

        editProfileService.updateSkills(id,skillForm.getItems());

    return new ResponseEntity<>(HttpStatus.OK);
    }



    @GetMapping(value = "/info", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Profile> getEditProfile(@RequestParam Long id)
    {
        Profile profile = editProfileService.findProfileById(id);

        return new ResponseEntity<Profile>(profile,HttpStatus.OK);

    }



    @PostMapping(value = "/info", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity postEditSkill(@Valid @RequestBody InfoForm infoForm, @RequestParam Long id ) {
       // Profile profile = editProfileService.findProfileById(id);
        editProfileService.updateInfo(id, infoForm);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PasswordForm> getPassword(@RequestParam Long id)
    {
        return new ResponseEntity<PasswordForm>(new PasswordForm(),HttpStatus.OK);
    }


    @PostMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Profile> postChangePassword(@Valid @RequestBody PasswordForm passwordForm, @RequestParam Long id, BindingResult bindingResult ) {
        if(bindingResult.hasErrors()) {
            // formErrorConverter.convertFormErrorToFieldError(FieldMatch.class, form, bindingResult);
            throw new FormValidationException(bindingResult.getObjectName(),bindingResult.getTarget(),bindingResult.getFieldError().getCode().toString());
        } else {

        Profile profile = editProfileService.findProfileById(id);

        editProfileService.updateProfilePassword(profile, passwordForm);

        return new ResponseEntity<Profile>(profile, HttpStatus.OK); }
    }



    @GetMapping(value = "/hobby", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HobbyForm> getHobby(@RequestParam Long id)
    {
        Profile profile = editProfileService.findProfileById(id);

        List<Hobby> hobby = profile.getHobbies();
        HobbyForm hobbyForm = new HobbyForm(hobby);

        return new ResponseEntity<HobbyForm>(hobbyForm,HttpStatus.OK);
    }


    @GetMapping(value = "/hobbies", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Hobby>> getHobbies(@RequestParam Long id) {

        Profile profile = editProfileService.findProfileById(id);

        List<Hobby> hobbyList = editProfileService.listHobbiesWithProfileSelected(profile);

        return new ResponseEntity<List<Hobby>>(hobbyList,HttpStatus.OK);
    }


    @PostMapping(value = "/hobbies", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> postHobbies(@RequestBody List<String> hobbies, @RequestParam Long id ) {

        Profile profile = editProfileService.findProfileById(id);

        editProfileService.updateHobbies(profile,hobbies);

         return new ResponseEntity<List<String>>(hobbies, HttpStatus.OK);
    }




    @GetMapping(value = "/languages", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LanguageForm> getLanguages(@RequestParam Long id) {

        Profile profile = editProfileService.findProfileById(id);

        LanguageForm languageForm = new LanguageForm(editProfileService.listLanguages(profile));

        return new ResponseEntity<LanguageForm>(languageForm,HttpStatus.OK);
    }


    @PostMapping(value = "/languages", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Profile> postLanguages( @Valid @RequestBody LanguageForm form, @RequestParam Long id ) {

        Profile profile = editProfileService.findProfileById(id);

        editProfileService.updateLanguages(profile, form.getItems());

        return new ResponseEntity<Profile>(profile, HttpStatus.OK);
    }






}
