package resume.microservice.service;



import resume.microservice.entity.*;
import resume.microservice.form.InfoForm;
import resume.microservice.form.PasswordForm;
import resume.microservice.form.SignUpForm;


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

    void updateInfo(Long id, InfoForm form);

    Profile updateProfilePassword(Profile profile, PasswordForm form);


    List<Hobby> listHobbiesWithProfileSelected(Profile profile);

    void updateHobbies(Profile currentProfile, List<String> hobbies);

    List<Language> listLanguages(Profile currentProfile);

    void updateLanguages(Profile currentProfile, List<Language> languages);


}