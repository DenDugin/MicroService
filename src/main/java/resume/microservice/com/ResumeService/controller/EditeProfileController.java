package resume.microservice.com.ResumeService.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import resume.microservice.com.ResumeService.form.InfoForm;
import resume.microservice.com.ResumeService.form.PasswordForm;
import resume.microservice.com.ResumeService.form.SkillForm;
import resume.microservice.com.ResumeService.entity.Profile;
import resume.microservice.com.ResumeService.entity.Skill;
import resume.microservice.com.ResumeService.exception.CantCompleteClientRequestException;
import resume.microservice.com.ResumeService.exception.FormValidationException;
import resume.microservice.com.ResumeService.service.EditProfileService;
import resume.microservice.com.ResumeService.service.FindProfileService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class EditeProfileController {


    @Autowired
    private EditProfileService editProfileService;


    @Autowired
    private FindProfileService findProfileService;



//    @GetMapping("/info")
//    public ResponseEntity<Profile> getInfo( @RequestParam long id ) {
//
//        Profile profile = findProfileService.findOne( id );
//
//        if ( profile == null )
//            throw new CantCompleteClientRequestException("Can't find profile by id : " + id);
//
//        return new ResponseEntity<Profile>(profile, HttpStatus.OK);
//    }





    @GetMapping("/skills")
    //public SkillForm getEditSkills(@RequestBody SkillForm skillForm) {
    public List<Skill> getEditSkill(@RequestParam long id) {

        return editProfileService.listSkills(id);
    }


    @PostMapping("/skills")
    //public SkillForm getEditSkills(@RequestBody SkillForm skillForm) {
    public ResponseEntity  postEditSkill(@RequestBody SkillForm skillForm, @RequestParam long id) {

      if (skillForm == null)
            throw new CantCompleteClientRequestException("SkillForm is null");

        editProfileService.updateSkills(id,skillForm.getItems());

    return new ResponseEntity<>(HttpStatus.OK);
    }



    @GetMapping("/objectivee")
    public ResponseEntity<Profile> getEditObjectiveMS( @RequestParam long id ) {

        Profile prf = findProfileService.findOne( id );

        return new ResponseEntity<>(prf, HttpStatus.OK);
    }


    @PostMapping("/objectivee")
    //public SkillForm getEditSkills(@RequestBody SkillForm skillForm) {
    public ResponseEntity postEditSkill(@RequestBody Profile profile, @RequestParam long id) {

        editProfileService.updateObjective( id, profile.getObjective(), profile.getSummary());
        
        return new ResponseEntity<>(HttpStatus.OK);
    }


// ------------------------------------

    @GetMapping("/info")
    public ResponseEntity<Profile> getEditProfile(@RequestParam Long id)
    {
        Profile profile = editProfileService.findProfileById(id);

        if ( profile == null )
            throw new CantCompleteClientRequestException("Can't find profile by id : " + id);

        return new ResponseEntity<Profile>(profile,HttpStatus.OK);

    }



    @PostMapping("/info")
    public ResponseEntity postEditSkill(@Valid @RequestBody InfoForm infoForm, @RequestParam Long id ) {

        Profile profile = editProfileService.findProfileById(id);

        if ( profile == null )
            throw new CantCompleteClientRequestException("Can't find profile by id : " + id);

        editProfileService.updateInfo(profile,infoForm);

        // editProfileService.updateProfileData(profile);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/password")
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

        if ( profile == null )
            throw new CantCompleteClientRequestException("Can't find profile by id : " + id);

        editProfileService.updateProfilePassword(profile, passwordForm);

        return new ResponseEntity<Profile>(profile, HttpStatus.OK); }
    }


}
