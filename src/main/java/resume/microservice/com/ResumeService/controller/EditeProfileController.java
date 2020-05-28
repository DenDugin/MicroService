package resume.microservice.com.ResumeService.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import resume.microservice.com.ResumeService.Form.InfoForm;
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




    @GetMapping("/profile")
    public ResponseEntity<Profile> getEditProfile(@RequestParam Long id)
    {
        Profile profile = editProfileService.findProfileById(id);

        if ( profile == null )
            throw new CantCompleteClientRequestException("Can't find profile by id : " + id);

        return new ResponseEntity<Profile>(profile,HttpStatus.OK);

    }



    @PostMapping("/infoform")
    public ResponseEntity postEditSkill(@RequestBody InfoForm infoForm, @RequestParam Long id ) {

        Profile profile = editProfileService.findProfileById(id);

        if ( profile == null )
            throw new CantCompleteClientRequestException("Can't find profile by id : " + id);

        editProfileService.updateInfo(profile,infoForm);

        // editProfileService.updateProfileData(profile);

        return new ResponseEntity<>(HttpStatus.OK);
    }



}
