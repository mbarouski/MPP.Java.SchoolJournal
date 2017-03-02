package school.journal.controller;


import org.apache.log4j.Logger;
import org.hibernate.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import school.journal.controller.exception.ControllerException;
import school.journal.controller.util.ErrorObject;
import school.journal.entity.Role;
import school.journal.service.IRoleService;
import school.journal.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/api/roles")
public class RoleAPIController {
    private Logger LOGGER = Logger.getLogger(RoleAPIController.class);

    @Autowired
    private IRoleService roleService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<Role> get(HttpServletRequest req)
            throws ControllerException {
        try{
            LOGGER.info("get role list controller method");
            return roleService.getRoles();
        } catch (ServiceException exc){
            return new ArrayList<>();
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity create(@RequestBody Role role)
            throws ControllerException {
        try{
            return new ResponseEntity(roleService.createRole(role), HttpStatus.OK);
        } catch (ServiceException exc){
            return new ResponseEntity(new ErrorObject("Error in role creating"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity update(@RequestBody Role role)
            throws ControllerException {
        try{
            return new ResponseEntity(roleService.updateRole(role), HttpStatus.OK);
        } catch (ServiceException exc){
            return new ResponseEntity(new ErrorObject("Error in role updating"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{roleId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(@PathVariable("roleId") int roleId)
            throws ControllerException {
        try{
            return new ResponseEntity(roleService.deleteRole(roleId), HttpStatus.OK);
        } catch (ServiceException exc){
            return new ResponseEntity(new ErrorObject("Error in role deleting"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{roleId}")
    @ResponseBody
    public ResponseEntity getOne(@PathVariable("roleId") int roleId)
            throws ControllerException {
        try{
            return new ResponseEntity(roleService.getOne(roleId), HttpStatus.OK);
        } catch (ServiceException exc){
            return new ResponseEntity(new ErrorObject("Error in role getting"), HttpStatus.BAD_REQUEST);
        }
    }
}
