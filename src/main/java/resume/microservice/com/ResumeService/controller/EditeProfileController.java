package resume.microservice.com.ResumeService.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import resume.microservice.com.ResumeService.Form.SkillForm;
import resume.microservice.com.ResumeService.entity.Profile;
import resume.microservice.com.ResumeService.entity.Skill;
import resume.microservice.com.ResumeService.entity.SkillCategory;
import resume.microservice.com.ResumeService.exception.CantCompleteClientRequestException;
import resume.microservice.com.ResumeService.repository.ProfileRepository;
import resume.microservice.com.ResumeService.service.EditProfileService;
import resume.microservice.com.ResumeService.service.FindProfileService;
import resume.microservice.com.ResumeService.service.ProfileService;

import javax.annotation.PreDestroy;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/edit")
public class EditeProfileController {


    @Autowired
    private EditProfileService editProfileService;


    @Autowired
    private FindProfileService findProfileService;



    @GetMapping("/skills")
    //public SkillForm getEditSkills(@RequestBody SkillForm skillForm) {
    public List<Skill> getEditSkill(@RequestParam long id) {

        return editProfileService.listSkills(id);
    }


    @PostMapping("/skills")
    //public SkillForm getEditSkills(@RequestBody SkillForm skillForm) {
    public ResponseEntity  postEditSkill(@RequestBody SkillForm skillForm, @RequestParam long id) {

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




    @GetMapping("/profile")
    public ResponseEntity<Profile> getEditProfile(@RequestParam String uid)
    {
        Profile profile = editProfileService.findProfileById(uid);

        if ( profile == null )
            throw new CantCompleteClientRequestException("Can't find profile by id : " + uid);

        return new ResponseEntity<Profile>(profile,HttpStatus.OK);

    }



    @PutMapping("/profile")
    public ResponseEntity postEditSkill(@RequestParam String uid, @RequestBody Profile profile) {

        editProfileService.updateProfileData(profile);

        return new ResponseEntity<>(HttpStatus.OK);
    }



}
