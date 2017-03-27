package school.journal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import school.journal.controller.util.ErrorObject;
import school.journal.entity.User;
import school.journal.entity.util.TokenInfo;
import school.journal.entity.util.UserAuthInfo;
import school.journal.service.IAuthService;
import school.journal.service.exception.AuthException;
import school.journal.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/api/auth")
public class AuthAPIController {
    private final static Logger LOGGER = Logger.getLogger(AuthAPIController.class);

//    @Autowired
    @Qualifier("AuthService")
    private IAuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity login(@RequestBody UserAuthInfo user) {
        String token = null;
        ResponseEntity resultResponse = null;
        try {
            token = authService.login(user.getUsername(), user.getPassword());
            if(token != null) {
                TokenInfo tokenInfo = new TokenInfo();
                tokenInfo.setValue(token);
                resultResponse = new ResponseEntity(tokenInfo, HttpStatus.OK);
            }
        } catch (AuthException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity("Login error", HttpStatus.BAD_REQUEST);
        }  catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @PostMapping(value = "/logout")
    public ResponseEntity logout(HttpServletRequest req) {
        ResponseEntity resultResponse;
        try {
            authService.logout((User)req.getAttribute("user"));
            resultResponse = new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException | AuthException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return resultResponse;
    }
}
