package resume.microservice.com.ResumeService.Util;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import resume.microservice.com.ResumeService.Language.CurrentProfile;
import resume.microservice.com.ResumeService.entity.Profile;

import java.util.UUID;

// класс для получения доступа к текущему sucurit-е контексту
public final class SecurityUtil {

    // вернуть залогиненый профиль
    public static CurrentProfile getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CurrentProfile) {
            return ((CurrentProfile)principal);
        } else {
            return null;
        }
    }

    public static Long getCurrentIdProfile() {
        CurrentProfile currentProfile = getCurrentProfile();
        return currentProfile != null ? currentProfile.getId() : null;
    }

    // аутентификация текущего пользователя
    public static void authentificate(Profile profile) {
        CurrentProfile currentProfile = new CurrentProfile(profile);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                currentProfile, currentProfile.getPassword(), currentProfile.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static boolean isCurrentProfileAuthentificated() {
        return getCurrentProfile() != null;
    }

    public static String generateNewActionUid(){
        return UUID.randomUUID().toString();
    }

    public static String generateNewRestoreAccessToken(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}