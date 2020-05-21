package resume.microservice.com.ResumeService.controller;


import com.squareup.okhttp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import resume.microservice.com.ResumeService.Util.Parser;
import resume.microservice.com.ResumeService.Util.RequestUtil;
import resume.microservice.com.ResumeService.entity.Profile;
import resume.microservice.com.ResumeService.entity.SkillCategory;
import resume.microservice.com.ResumeService.exception.CantCompleteClientRequestException;
import resume.microservice.com.ResumeService.exception.UserServiceException;
import resume.microservice.com.ResumeService.service.ProfileService;

@RestController
@RequestMapping("/api")
public class Controller {


    private ProfileService profileService;


    @Autowired
    public Controller(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/test")
    public String getTest(){
        return "HelloWorld!";
    }


    @GetMapping("/profile/{id}")
    public ResponseEntity<Profile>  getProfileById( @PathVariable Long id ) {

       Profile profile = profileService.findById(id);

       if (profile == null)
           throw  new UserServiceException("No found user by id : " + id);
           //return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(profile, HttpStatus.OK);

    }


    @PostMapping("/profile")
    public Profile test( @RequestBody Profile profile ) {
        return profile;
    }


    @PostMapping("/skill")
    public ResponseEntity<SkillCategory> getSkill(@RequestBody SkillCategory skillCategory) {

        return new ResponseEntity<>(skillCategory,HttpStatus.NOT_FOUND);
    }



    @GetMapping(path = "/manu/{id}", produces = { MediaType.TEXT_HTML_VALUE })
    public ResponseEntity<String> getManchester(@PathVariable int id ) {

        final String url = "http://www.manutd.ru/";
        String Rez = null;
        Parser parser;

        try {
           // Response response =  RequestUtil.sendGet(url);

           // Rez = response.body().toString();

            Rez = RequestUtil.run(url);

            parser = new Parser(Rez);
            Rez = parser.getData(id);


        } catch (Exception e) {
            e.printStackTrace();
            //return new ResponseEntity<>(Rez, HttpStatus.INTERNAL_SERVER_ERROR);
            throw new CantCompleteClientRequestException("Error while do GET to : "+ url, e );
        }

        return new ResponseEntity<>(Rez, HttpStatus.OK);
    }


}
