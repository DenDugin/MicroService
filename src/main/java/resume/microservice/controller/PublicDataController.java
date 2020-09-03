package resume.microservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import resume.microservice.entity.Profile;
import resume.microservice.form.SignUpForm;
import resume.microservice.service.EditProfileService;
import resume.microservice.service.FindProfileService;
import resume.microservice.util.SecurityUtil;

import javax.validation.Valid;


@RestController
public class PublicDataController {


    @Autowired
    FindProfileService findProfileService;

    @Autowired
    private EditProfileService editProfileService;


    @GetMapping("/{uid}")
    public ResponseEntity<Profile> getProfile(@PathVariable("uid") String uid ) {

        Profile profile = findProfileService.findByUid(uid);

        return new ResponseEntity<>(profile, HttpStatus.OK);
    }


    @GetMapping("/welcome")
    public ResponseEntity<Page<Profile>> listAll() {

     Page<Profile> profiles = findProfileService.findAll( PageRequest.of(0, 10, Sort.by("id")));

       return new ResponseEntity<Page<Profile>>(profiles, HttpStatus.OK);
    };

    @GetMapping("/sign-in")
    public String signIn() {
        return "sign-in";
    }

    @GetMapping("/sign-up")
    public SignUpForm signUp(Model model) {
        return new SignUpForm();
    }


    @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Profile> signUp( @Valid SignUpForm signUpForm ) {

            Profile profile = editProfileService.createNewProfile(signUpForm);

            SecurityUtil.authentificate(profile);

            return new ResponseEntity<Profile>(profile,HttpStatus.OK);
        }



}
