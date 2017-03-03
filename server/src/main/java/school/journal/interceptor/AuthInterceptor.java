package school.journal.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import school.journal.entity.User;
import school.journal.service.IAuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor extends HandlerInterceptorAdapter{
    @Autowired
    private IAuthService authService;
    
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object o) throws Exception {
//        String token = req.getHeader("Authorization");
//        User user;
//        if((user = authService.checkToken(token)) != null) {
//            req.setAttribute("user", user);
//        }
//        return user != null;
        return true;
    }
}
