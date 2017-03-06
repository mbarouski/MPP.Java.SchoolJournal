package school.journal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import school.journal.entity.util.TokenInfo;
import school.journal.entity.util.UserAuthInfo;
import school.journal.service.IAuthService;
import school.journal.service.exception.AuthException;
import school.journal.service.exception.ServiceException;

@Controller
@RequestMapping(value = "/api/auth")
public class AuthAPIController {

    @Autowired
    IAuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity login(@RequestBody UserAuthInfo user) {
        String token = null;
        try {
            token = authService.login(user.getUsername(), user.getPassword());
            if(token != null) {
                TokenInfo tokenInfo = new TokenInfo();
                tokenInfo.setValue(token);
                return new ResponseEntity(tokenInfo, HttpStatus.OK);
            }
        } catch (AuthException exc) {

        } catch (ServiceException exc) {

        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
