package school.journal.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import school.journal.entity.User;
import school.journal.service.IAuthService;
import school.journal.service.exception.AuthException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin
//@Scope("AuthInterceptor")
public class AuthInterceptor extends HandlerInterceptorAdapter{
    @Autowired
    private IAuthService authService;
    
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object o) throws Exception {
//        if(req.getMethod().equals("OPTIONS")) return true;
//        String token = req.getParameter("token");
//        User user = null;
//        try {
//            if ((token != null) && ((user = authService.checkToken(token)) != null)) {
//                req.setAttribute("user", user);
//            } else {
//                throw new AuthException("Invalid token");
//            }
//        } catch (AuthException exc) {
//            resp.sendError(401);
//            return false;
//        }
//        return user != null;
        return true;
    }
}
