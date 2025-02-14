package resume.microservice.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


public enum LanguageLevel {

    BEGINNER,

    ELEMENTARY,

    PRE_INTERMEDIATE,

    INTERMEDIATE,

    UPPER_INTERMEDIATE,

    ADVANCED,

    PROFICIENCY;

    public int getSliderIntValue(){
        return ordinal();
    }

    public String getDbValue(){
        return name().toLowerCase();
    }


    // моя кастомная аннотация !
    @Converter
    public static class PersistJPAConverter implements AttributeConverter<LanguageLevel, String> {
        @Override
        public String convertToDatabaseColumn(LanguageLevel attribute) {
            return attribute.getDbValue();
        }
        @Override
        public LanguageLevel convertToEntityAttribute(String dbValue) {
            return LanguageLevel.valueOf(dbValue.toUpperCase());
        }
    }
}