package resume.microservice.com.ResumeService.service.impl;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import resume.microservice.com.ResumeService.form.InfoForm;
import resume.microservice.com.ResumeService.form.PasswordForm;
import resume.microservice.com.ResumeService.form.SignUpForm;
import resume.microservice.com.ResumeService.util.DataUtil;
import resume.microservice.com.ResumeService.annotation.ProfileDataFieldGroup;
import resume.microservice.com.ResumeService.entity.Hobby;
import resume.microservice.com.ResumeService.entity.Profile;
import resume.microservice.com.ResumeService.entity.Skill;
import resume.microservice.com.ResumeService.entity.SkillCategory;
import resume.microservice.com.ResumeService.exception.CantCompleteClientRequestException;
import resume.microservice.com.ResumeService.exception.FormValidationException;
import resume.microservice.com.ResumeService.exception.UserServiceException;
import resume.microservice.com.ResumeService.repository.ProfileRepository;
import resume.microservice.com.ResumeService.elastic.ProfileSearchRepository;
import resume.microservice.com.ResumeService.repository.SkillCategoryRepository;
import resume.microservice.com.ResumeService.service.EditProfileService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class EditProfileServiceImpl implements EditProfileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EditProfileServiceImpl.class);

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileSearchRepository profileSearchRepository;

    @Autowired
    private SkillCategoryRepository skillCategoryRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Value("${generate.uid.suffix.length}")
    private int generateUidSuffixLength;

    @Value("${generate.uid.alphabet}")
    private String generateUidAlphabet;

    @Value("${generate.uid.max.try.count}")
    private int maxTryCountToGenerateUid;

    @Override
    // Аннотация говорит, что все изменения сделанные в этом метода выполняются в одной транзакции!
    @Transactional
    public Profile createNewProfile(SignUpForm signUpForm) {
        Profile profile = new Profile();
        profile.setUid(generateProfileUid(signUpForm));
        profile.setFirstName(DataUtil.capitalizeName(signUpForm.getFirstName()));
        profile.setLastName(DataUtil.capitalizeName(signUpForm.getLastName()));
        profile.setPassword(signUpForm.getPassword());
        profile.setCompleted(false);
        profileRepository.save(profile);
        registerCreateIndexProfileIfTrancationSuccess(profile);
        return profile;
    }

    private String generateProfileUid(SignUpForm signUpForm) throws CantCompleteClientRequestException {
        String baseUid = DataUtil.generateProfileUid(signUpForm);
        String uid = baseUid;
        for (int i = 0; profileRepository.countByUid(uid) > 0; i++) {
            uid = DataUtil.regenerateUidWithRandomSuffix(baseUid, generateUidAlphabet, generateUidSuffixLength);
            if (i >= maxTryCountToGenerateUid) {
                throw new CantCompleteClientRequestException("Can't generate unique uid for profile: " + baseUid+": maxTryCountToGenerateUid detected");
            }
        }
        return uid;
    }


    // данный метод решает проблему рассинхронизации данных бд и elastic-а, т.е. обновляет данные в elastic-е
    private void registerCreateIndexProfileIfTrancationSuccess(final Profile profile) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                //LOGGER.info("New profile created: {}", profile.getUid());
                profile.setCertificates(Collections.EMPTY_LIST);
                profile.setPractics(Collections.EMPTY_LIST);
                profile.setLanguages(Collections.EMPTY_LIST);
                profile.setSkills(Collections.EMPTY_LIST);
                profile.setCourses(Collections.EMPTY_LIST);
                profileSearchRepository.save(profile);
                //LOGGER.info("New profile index created: {}", profile.getUid());
            }
        });
    }


    @Override
    public List<Skill> listSkills(long idProfile) {

        Optional<Profile> profile = profileRepository.findById(idProfile);

        return profile.get().getSkills();

    }

    @Override
    public List<SkillCategory> listSkillCategories() {
        //return skillCategoryRepository.findAll(new Sort("id"));

        return  skillCategoryRepository.findAllByOrderById();

    }

    @Override
    public List<Hobby> listHobby(long idProfile) {

        //return profileRepository.findOne(idProfile).getHobbies();

        return  profileRepository.findById(idProfile).get().getHobbies();

    }


    @Override
    // Аннотация говорит, что все изменения сделанные в этом метода выполняются в одной транзакции!
    @Transactional
    public void updateSkills(long idProfile, List<Skill> updatedData) {

        // Profile profile = profileRepository.findOne(idProfile);

        Profile profile = profileRepository.findById(idProfile).get();

        if (CollectionUtils.isEqualCollection(updatedData, profile.getSkills())) {
            //LOGGER.debug("Profile skills: nothing to update");
            return;
        } else {
            profile.setSkills(updatedData);
            profileRepository.save(profile);
            // registerUpdateIndexProfileSkillsIfTransactionSuccess(idProfile, updatedData);
        }
    }

    @Override
    @Transactional
    public void updateHobby(long idProfile, List<Hobby> hobby) {

        //Profile profile = profileRepository.findOne(idProfile);

        Profile profile = profileRepository.findById(idProfile).get();

        if (CollectionUtils.isEqualCollection(hobby, profile.getHobbies()))  return;

        if (profile != null )
        {
            profile.setHobbies(hobby);
            profileRepository.save(profile);
        }


    }

    @Override
    @Transactional
    public void updateObjective(long idProfile, String Objective, String Summary) {

        // Profile profile = profileRepository.findOne(idProfile);

        Profile profile = profileRepository.findById(idProfile).get();

        if (profile != null )
        {
            profile.setObjective(Objective);
            profile.setSummary(Summary);
            profileRepository.save(profile);
        }
    }

    @Override
    public Profile findProfileById(Long id) {

        Optional<Profile> profile = profileRepository.findById(id);

        return profile.get();

        //return profileRepository.findByUid(id);
    }


    private void registerUpdateIndexProfileSkillsIfTransactionSuccess(final long idProfile, final List<Skill> updatedData) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                //LOGGER.info("Profile skills updated");
                updateIndexProfileSkills(idProfile, updatedData);
            }
        });
    }

    private void updateIndexProfileSkills(long idProfile, List<Skill> updatedData) {
        // Profile profile = profileSearchRepository.findOne(idProfile);

        Profile profile = profileRepository.findById(idProfile).get();

        profile.setSkills(updatedData);
        profileSearchRepository.save(profile);
        LOGGER.info("Profile skills index updated");
    }

    @Override
    @Transactional
    public void updateProfileData(Profile profileForm) {

        Optional<Profile> loadedProfile = profileRepository.findById(profileForm.getId());

        if (!loadedProfile.isPresent())
            throw new UserServiceException("No find user by id : "+ profileForm.getId());


        int copiedFieldsCount = DataUtil.copyFields(profileForm, loadedProfile.get(), ProfileDataFieldGroup.class);

        if (copiedFieldsCount > 0 )
            executeUpdateProfileData(loadedProfile.get(), profileForm);

    }


    protected void executeUpdateProfileData(Profile currentProfile, Profile loadedProfile) {
        loadedProfile.setCompleted(true);
        synchronized (this) {
            checkForDuplicatesEmailAndPhone(loadedProfile);
            profileRepository.save(loadedProfile);
            // profileRepository.flush();
        }

    }


    protected void checkForDuplicatesEmailAndPhone(Profile profileForm) {
        Profile profileForEmail = profileRepository.findByEmail(profileForm.getEmail());
        if (profileForEmail != null && !profileForEmail.getId().equals(profileForm.getId())) {
            throw new FormValidationException("email", profileForm.getEmail());
        }
        Profile profileForPhone = profileRepository.findByPhone(profileForm.getPhone());
        if (profileForPhone != null && !profileForPhone.getId().equals(profileForm.getId())) {
            throw new FormValidationException("phone", profileForm.getPhone());
        }
    }

    @Override
    @Transactional
    public void updateInfo(Profile profile, InfoForm form) {

        Optional<Profile> loadedProfile = profileRepository.findById(profile.getId());

        if (!StringUtils.equals(loadedProfile.get().getInfo(), form.getInfo())) {
            loadedProfile.get().setInfo(form.getInfo());

            profileRepository.save(loadedProfile.get());

            // updateIndexProfileInfoIfTransactionSuccess(currentProfile, loadedProfile);
           // evilcProfileCacheIfTransactionSuccess(currentProfile);
        } else {
            LOGGER.debug("Profile info not updated");
        }
    }

    @Override
    @Transactional
    public Profile updateProfilePassword(Profile currentProfile, PasswordForm form) {

        Optional<Profile> profile = profileRepository.findById(currentProfile.getId());

        profile.get().setPassword(passwordEncoder.encode(form.getPassword()));
        profileRepository.save(profile.get());
        // sendPasswordChangedIfTransactionSuccess(profile);
        return profile.get();
    }


}