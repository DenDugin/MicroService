package resume.microservice.com.ResumeService.controller;

//import org.elasticsearch.search.suggest.SortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import resume.microservice.com.ResumeService.entity.Profile;
import resume.microservice.com.ResumeService.service.FindProfileService;

import java.io.Externalizable;
import java.io.Serializable;

@RestController
public class PublicDataController {


    @Autowired
    FindProfileService findProfileService;

    @GetMapping("/search")
    public String getSearch(){

        System.out.println("doGet search!");

        return "search-form";

    }

    @GetMapping("/{uid}")
    public ResponseEntity<Profile> getProfile( @PathVariable("uid") String uid ) {

        Profile profile = findProfileService.findByUid(uid);

        return new ResponseEntity<>(profile, HttpStatus.OK);
    }


    @GetMapping("/welcome")
    public ResponseEntity<Page<Profile>> listAll() {

     Page<Profile> profiles = findProfileService.findAll( PageRequest.of(0, 10, Sort.by("id")));

       return new ResponseEntity<Page<Profile>>(profiles, HttpStatus.OK);
    };



}
