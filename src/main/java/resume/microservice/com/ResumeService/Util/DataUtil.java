package resume.microservice.com.ResumeService.Util;


import org.apache.commons.lang.WordUtils;
import org.springframework.util.ReflectionUtils;
import resume.microservice.com.ResumeService.Form.SignUpForm;
import resume.microservice.com.ResumeService.annotation.ProfileDataFieldGroup;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Random;

public class DataUtil {

    public static String normalizeName(String name) {
        return name.trim().toLowerCase();
    }

    public static String capitalizeName(String name) {
        return WordUtils.capitalize(normalizeName(name));
    }

    public static String generateProfileUid(SignUpForm profile) {
        return normalizeName(profile.getFirstName()) + UID_DELIMETER + normalizeName(profile.getLastName());
    }

    public static String regenerateUidWithRandomSuffix(String baseUid, String alphabet, int letterCount) {
        return baseUid + UID_DELIMETER + generateRandomSuffix(alphabet, letterCount);
    }

    public static String generateRandomSuffix(String alphabet, int letterCount) {
        Random r = new Random();
        StringBuilder uid = new StringBuilder();
        for (int i = 0; i < letterCount; i++) {
            uid.append(alphabet.charAt(r.nextInt(alphabet.length())));
        }
        return uid.toString();
    }


    public static <T extends Annotation> int copyFields(final Object from, final Object to, Class<T> annotation) {
        final CopiedFieldsCounter copiedFieldsCounter = new CopiedFieldsCounter();
        ReflectionUtils.doWithFields(to.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                ReflectionUtils.makeAccessible(field);
                copyAccessibleField(field, from, to, copiedFieldsCounter);
            }
        }, createFieldFilter(annotation));
        return copiedFieldsCounter.counter;
    }

    private static void copyAccessibleField(Field field, Object from, Object to, CopiedFieldsCounter copiedFieldsCounter) throws IllegalAccessException {
        Object fromValue = field.get(from);
        Object toValue = field.get(to);
        if (fromValue == null) {
            if (toValue != null) {
                field.set(to, null);
                copiedFieldsCounter.counter++;
            }
        } else {
            if (!fromValue.equals(toValue)) {
                field.set(to, fromValue);
                copiedFieldsCounter.counter++;
            }
        }
    }

    private static <T extends Annotation> ReflectionUtils.FieldFilter createFieldFilter(Class<T> annotation) {
        if (annotation == null) {
            return ReflectionUtils.COPYABLE_FIELDS;
        } else {
            return new org.springframework.data.util.ReflectionUtils.AnnotationFieldFilter(annotation);
        }
    }

    private static final String UID_DELIMETER = "-";

    private static final class CopiedFieldsCounter {
        private int counter;
    }
}
