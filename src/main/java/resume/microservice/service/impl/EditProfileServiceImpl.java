package resume.microservice.service.impl;


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
import resume.microservice.annotation.ProfileDataFieldGroup;
import resume.microservice.entity.*;
import resume.microservice.form.InfoForm;
import resume.microservice.form.PasswordForm;
import resume.microservice.form.SignUpForm;
import resume.microservice.repository.*;
import resume.microservice.service.StaticDataService;
import resume.microservice.util.DataUtil;
import resume.microservice.exception.CantCompleteClientRequestException;
import resume.microservice.exception.FormValidationException;
import resume.microservice.exception.UserServiceException;
import resume.microservice.service.EditProfileService;

import javax.annotation.PostConstruct;
import java.util.*;


@Service
public class EditProfileServiceImpl implements EditProfileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EditProfileServiceImpl.class);

    @Autowired
    private ProfileRepository profileRepository;


    @Autowired
    private SkillCategoryRepository skillCategoryRepository;

    @Autowired
    private HobbyRepository hobbyRepository;

    @Autowired
    private StaticDataService staticDataService;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    private LanguageRepository languageRepository;

    @Value("${generate.uid.suffix.length}")
    private int generateUidSuffixLength;

    @Value("${generate.uid.alphabet}")
    private String generateUidAlphabet;

    @Value("${generate.uid.max.try.count}")
    private int maxTryCountToGenerateUid;

    @Value("${profile.hobbies.max}")
    private int maxProfileHobbies;


    private Map<Class<? extends ProfileEntity>, AbstractProfileEntityRepository<? extends ProfileEntity>> profileEntityRepositoryMap;

    @PostConstruct
    private void postConstruct() {
//        profileCollectionsToReIndex = Collections.unmodifiableSet(
//                new HashSet<>(Arrays.asList(
//                        new String[]{"languages", "skills", "practics", "certificates", "courses"})));

        Map<Class<? extends ProfileEntity>, AbstractProfileEntityRepository<? extends ProfileEntity>> map = new HashMap<>();
//        map.put(Certificate.class, certificateRepository);
//        map.put(Course.class, courseRepository);
//        map.put(Education.class, educationRepository);
        map.put(Hobby.class, hobbyRepository);
//        map.put(Language.class, languageRepository);
//        map.put(Practic.class, practicRepository);
        //       map.put(Skill.class, skillRepository);
        profileEntityRepositoryMap = Collections.unmodifiableMap(map);
    }


    @Override
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
                // profileSearchRepository.save(profile);
                //LOGGER.info("New profile index created: {}", profile.getUid());
            }
        });
    }


    @Override
    public List<Skill> listSkills(long idProfile) {

        Optional<Profile> profile = profileRepository.findById(idProfile);

        return profile.isPresent() ? profile.get().getSkills() : null;
    }

    @Override
    public List<SkillCategory> listSkillCategories() {

        return  skillCategoryRepository.findAllByOrderById();
    }

    @Override
    public List<Hobby> listHobby(long idProfile) {

        return  profileRepository.findById(idProfile).get().getHobbies();
    }


    @Override
    @Transactional
    public void updateSkills(long idProfile, List<Skill> updatedData) {

        Profile profile = profileRepository.findById(idProfile).orElseThrow(() -> new UserServiceException("Can't find profile by id : "+idProfile) );

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

        Profile profile1 = profileRepository.findById(id).orElseThrow(()->new UserServiceException("Can't find profile by id : "+id) );

        return profile1;

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

        Profile profile = profileRepository.findById(idProfile).get();

        profile.setSkills(updatedData);
       //  profileSearchRepository.save(profile);                        // uncommit
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
    public void updateInfo(Long id, InfoForm form) {

        Profile loadedProfile = profileRepository.findById(id).orElseThrow(()->new UserServiceException("Can't find profile by id : "+id));

        if (!StringUtils.equals(loadedProfile.getInfo(), form.getInfo())) {
            loadedProfile.setInfo(form.getInfo());

            profileRepository.save(loadedProfile);

            // updateIndexProfileInfoIfTransactionSuccess(currentProfile, loadedProfile);
           // evilcProfileCacheIfTransactionSuccess(currentProfile);
        } else {
            LOGGER.debug("Profile info not updated");
        }
    }

    @Override
    @Transactional
    public Profile updateProfilePassword(Profile profile, PasswordForm form) {

       // Profile profile = profileRepository.findById(id).orElseThrow(()->new UserServiceException("Can't find profile by id : "+id));

        profile.setPassword(passwordEncoder.encode(form.getPassword()));
        profileRepository.save(profile);
        // sendPasswordChangedIfTransactionSuccess(profile);
        return profile;
    }


    @Override
    public List<Hobby> listHobbiesWithProfileSelected(Profile currentProfile) {
        List<Hobby> profileHobbies = hobbyRepository.findByProfileIdOrderByIdAsc(currentProfile.getId());
        List<Hobby> hobbies = new ArrayList<>();
        for (Hobby h : staticDataService.listAllHobbies()) {
            boolean selected = profileHobbies.contains(h);
            hobbies.add(new Hobby(h.getName(), selected));
        }
        return hobbies;
    }

    @Override
    @Transactional
    public void updateHobbies(Profile currentProfile, List<String> hobbies) {
        List<Hobby> updatedHobbies = staticDataService.createHobbyEntitiesByNames(hobbies);
        if (updatedHobbies.size() > maxProfileHobbies) {
            throw new CantCompleteClientRequestException("Detected more than " + maxProfileHobbies + " hobbies for profile: currentProfile=" + currentProfile + ", hobbies=" + updatedHobbies);
        }
        updateProfileEntities(currentProfile, updatedHobbies, Hobby.class);
    }


    protected <E extends ProfileEntity> void updateProfileEntities(Profile currentProfile, List<E> updatedData, Class<E> entityClass) {
        String collections = DataUtil.getCollectionName(entityClass);
        AbstractProfileEntityRepository<E> repository = findProfileEntityRepository(entityClass);
        List<E> profileData = repository.findByProfileIdOrderByIdAsc(currentProfile.getId());
        DataUtil.removeEmptyElements(updatedData);
        if (Comparable.class.isAssignableFrom(entityClass)) {
            Collections.sort((List<? extends Comparable>) updatedData);
        }
        if (DataUtil.areListsEqual(updatedData, profileData)) {
            LOGGER.debug("Profile {}: nothing to update", collections);
            return;
        } else {
            executeProfileEntitiesUpdate(currentProfile, repository, updatedData);
            //evilcProfileCacheIfTransactionSuccess(currentProfile);
            //updateIndexProfileEntitiesIfTransactionSuccess(currentProfile, updatedData, collections);
        }
    }


    protected <E extends ProfileEntity> void executeProfileEntitiesUpdate(Profile currentProfile, AbstractProfileEntityRepository<E> repository, List<E> updatedData) {
        repository.deleteByProfileId(currentProfile.getId());
        repository.flush();
        //Profile profileProxy = profileRepository.getOne(currentProfile.getId());
        Optional<Profile> profileProxy = profileRepository.findById(currentProfile.getId());
        for(E entity : updatedData) {
            entity.setId(null);
            entity.setProfile(profileProxy.get());
            repository.saveAndFlush(entity);
        }
    }

    protected <E extends ProfileEntity> AbstractProfileEntityRepository<E> findProfileEntityRepository(Class<E> entityClass) {
        AbstractProfileEntityRepository<E> repository = (AbstractProfileEntityRepository<E>) profileEntityRepositoryMap.get(entityClass);
        if(repository == null) {
            throw new IllegalArgumentException("ProfileEntityRepository not found for entityClass="+entityClass);
        }
        return repository;
    }


    @Override
    public List<Language> listLanguages(Profile currentProfile) {
        return languageRepository.findByProfileIdOrderByIdAsc(currentProfile.getId());
    }


    @Override
    @Transactional
    public void updateLanguages(Profile currentProfile, List<Language> languages) {
        updateProfileEntities(currentProfile, languages, Language.class);
    }

}