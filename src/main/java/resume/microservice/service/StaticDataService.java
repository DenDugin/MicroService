package resume.microservice.service;


import resume.microservice.model.LanguageLevel;
import resume.microservice.model.LanguageType;
import resume.microservice.entity.Hobby;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface StaticDataService {

	 Set<Hobby> listAllHobbies();
	
	 List<Hobby> createHobbyEntitiesByNames(List<String> names);
	
	 Map<Integer, String> mapMonths();
	
	 List<Integer> listPracticsYears();
	
	 List<Integer> listCourcesYears();
	
	 List<Integer> listEducationYears();
	
	 Collection<LanguageType> getAllLanguageTypes();
	
	 Collection<LanguageLevel> getAllLanguageLevels();
}
