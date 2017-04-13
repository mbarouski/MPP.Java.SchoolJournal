package school.journal.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import school.journal.entity.Role;
import school.journal.entity.User;
import school.journal.entity.enums.RoleEnum;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class SecureBeforeMethod {
    private static final Logger LOGGER = Logger.getLogger(SecureBeforeMethod.class);

    @Around("@annotation(school.journal.aop.Secured)")
    public ResponseEntity doSecure(ProceedingJoinPoint joinPoint) {
        MethodSignature  signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        int needLevel = method.getAnnotation(Secured.class).value().getValue();
        User currentUser = ((User)((HttpServletRequest)joinPoint.getArgs()[0]).getAttribute("user"));
        if(currentUser != null) {
            int currentUserLevel = currentUser.getRole().getLevel();
            if (currentUserLevel >= needLevel) {
                try {
                    return (ResponseEntity) joinPoint.proceed();
                } catch (Throwable exc) {
                    LOGGER.error(exc);
                }
            }
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
