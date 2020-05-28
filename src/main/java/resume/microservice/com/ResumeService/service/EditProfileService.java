package resume.microservice.com.ResumeService.service;



import resume.microservice.com.ResumeService.Form.InfoForm;
import resume.microservice.com.ResumeService.Form.SignUpForm;
import resume.microservice.com.ResumeService.entity.Hobby;
import resume.microservice.com.ResumeService.entity.Profile;
import resume.microservice.com.ResumeService.entity.Skill;
import resume.microservice.com.ResumeService.entity.SkillCategory;

import java.util.List;


// сервис для обновления всех данных в профиле
public interface EditProfileService {

    Profile createNewProfile(SignUpForm signUpForm);

    // загрузка скилов для указанного профиля
    List<Skill> listSkills(long idProfile);


    //загрузить скилл категории
    List<SkillCategory> listSkillCategories();

    //загрузить хобби для указанного профиля
    List<Hobby> listHobby(long idProfile);


    // обновить скилы для указанного профиля
    void updateSkills(long idProfile, List<Skill> skills);


    // обновить скилы для указанного профиля
    void updateHobby(long idProfile, List<Hobby> hobby);


    void updateObjective(long id, String Objective, String Summary);


    Profile findProfileById(Long id);

    void updateProfileData(Profile profile);

    void updateInfo(Profile profile, InfoForm form);

}