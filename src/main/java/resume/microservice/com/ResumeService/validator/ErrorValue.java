package resume.microservice.com.ResumeService.validator;

import java.util.HashMap;

public class ErrorValue {

    public static final HashMap<String,String> errors = new HashMap<String, String>(){{

        put("MinDigitCount","Минимальное число цифровых знаков должно быть : 1");

        put("MinLowerCharCount","Минимальное число букв с нижним регистром должно быть : 1");

        put("MinSpecCharCount","Минимальное число спец. символов должно быть : 1");

        put("MinUpperCharCount","Минимальное число заглавных букв  должно быть : 1");

        put("EnglishLanguage","Необходимо исполльзовать англ. язык");

        }};


}
