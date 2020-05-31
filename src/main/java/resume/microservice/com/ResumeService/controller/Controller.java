package resume.microservice.com.ResumeService.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import resume.microservice.com.ResumeService.util.Parser;
import resume.microservice.com.ResumeService.util.RequestUtil;
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






    private myThread thread = null;

    @GetMapping(path = "/thread")
    public String testThread() {

        if ( thread != null  ) {
            if (thread.isAlive())
                return "Thread BUSY";
            else  { thread = null; return thread.rezult; }
        }

        try {


            thread = new myThread();
            thread.start();

            return "Thread is RUN !";


        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Hello Thread!";
    }


    public static class myThread extends Thread {

        public  static String rezult;

        public static int i = 1;

        @Override
        public void run() {
            try {

                for (int i=0; i<5; i++)
                    Thread.sleep(1000);

                rezult = "DONE : " + i;

                i++;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}

