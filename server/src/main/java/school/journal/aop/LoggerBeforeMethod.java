package school.journal.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Formatter;

@Aspect
@Component
public class LoggerBeforeMethod {
    private final static Logger LOGGER = Logger.getLogger("AOP-Logger");
    private final static Formatter fmt = new Formatter();

    @Before("execution(* school.journal..*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        //  LOGGER.info(fmt.format("%s: %s", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName()));
    }
}
