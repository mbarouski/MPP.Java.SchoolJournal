package school.journal.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import school.journal.service.IAuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Scope("AuthInterceptor")
public class AuthInterceptor extends HandlerInterceptorAdapter{
    @Autowired
    private IAuthService authService;
    
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object o) throws Exception {
//        String token = req.getHeader("Authorization");
//        User user;
//        try {
//            if ((user = authService.checkToken(token)) != null) {
//                req.setAttribute("user", user);
//            }
//        } catch (AuthException exc) {
//            resp.sendError(401);
//            return false;
//        }
//        return user != null;
        return true;
    }
}
