package school.journal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import school.journal.aop.Secured;
import school.journal.controller.exception.ControllerException;
import school.journal.entity.Role;
import school.journal.entity.enums.RoleEnum;
import school.journal.service.IRoleService;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/roles")
public class RoleAPIController extends BaseController<Role> {
    private static final Logger LOGGER = Logger.getLogger(RoleAPIController.class);

    @Autowired
    @Qualifier("RoleService")
    private IRoleService roleService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity get(HttpServletRequest req)
            throws ControllerException {
        return read(roleService::read, "Can't get all roles", LOGGER);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Secured(RoleEnum.ADMIN)
    public ResponseEntity create(@RequestBody Role role)
            throws ControllerException {
        return createOrUpdate(roleService::create, role, "Can't create role", LOGGER);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    @Secured(RoleEnum.ADMIN)
    public ResponseEntity update(@RequestBody Role role)
            throws ControllerException {
        return createOrUpdate(roleService::update, role, "Can't update role", LOGGER);
    }

    @RequestMapping(value = "/{roleId}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured(RoleEnum.ADMIN)
    public ResponseEntity delete(@PathVariable("roleId") int roleId)
            throws ControllerException {
        return delete(roleService::delete, roleId, "Can't delete role", LOGGER);
    }
}
