package school.journal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.journal.aop.Secured;
import school.journal.entity.User;
import school.journal.entity.enums.RoleEnum;
import school.journal.service.IUserService;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/users")
public class UsersAPIController extends BaseController<User> {
    private final static Logger LOGGER = Logger.getLogger(UsersAPIController.class);

    @Autowired
    @Qualifier("UserService")
    private IUserService userService;

    @PutMapping(value="/changeRole", params={"userId", "roleId"})
    @ResponseBody
    @Secured(RoleEnum.DIRECTOR_OF_STUDIES)
    public ResponseEntity changeRole(HttpServletRequest req,  @RequestParam("userId") int userId,  @RequestParam("roleId") int roleId) {
        return doResponse(userService::changeRole, userId, roleId, "Can't change role of user", LOGGER, false);
    }

    @GetMapping
    @ResponseBody
    @Secured(RoleEnum.DIRECTOR_OF_STUDIES)
    public ResponseEntity getAll(HttpServletRequest req) {
        return read(userService::read, "Can't get all users", LOGGER);
    }

    @PostMapping
    @ResponseBody
    @Secured(RoleEnum.DIRECTOR_OF_STUDIES)
    public ResponseEntity create(HttpServletRequest req, @RequestBody User user) {
        return createOrUpdate(userService::create, user, "Can't create user", LOGGER);
    }

    @PutMapping
    @ResponseBody
    @Secured(RoleEnum.USER)
    public ResponseEntity update(HttpServletRequest req, @RequestBody User user) {
        return createOrUpdate(userService::update, user, "Can't update user", LOGGER);
    }

    @DeleteMapping(value = "/{userId}")
    @ResponseBody
    @Secured(RoleEnum.DIRECTOR_OF_STUDIES)
    public ResponseEntity delete(HttpServletRequest req, @PathVariable("userId") int userId) {
        return delete(userService::delete, userId, "Can't create user", LOGGER);
    }

    @GetMapping(value = "/{userId}")
    @ResponseBody
    @Secured(RoleEnum.PUPIL)
    public ResponseEntity getOne(HttpServletRequest req, @PathVariable("userId") int userId) {
        return getOne(userService::getOne, userId, "Can't get user", LOGGER);
    }

    @PutMapping(value = "/{userId}", params = {"password"})
    @ResponseBody
    @Secured(RoleEnum.PUPIL)
    public ResponseEntity changePassword(HttpServletRequest req, @PathVariable("userId") int userId,
    @RequestParam("password") String password) {
        return doResponse(userService::changePassword, userId, password, "Can't change password of user", LOGGER);
    }
}
