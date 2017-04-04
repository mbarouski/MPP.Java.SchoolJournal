package school.journal.controller;


import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

@CrossOrigin
@Controller
@RequestMapping(value = "/api/roles")
public class RoleAPIController {
    private Logger LOGGER = Logger.getLogger(RoleAPIController.class);

    @Autowired
    @Qualifier("RoleService")
    private IRoleService roleService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity get(HttpServletRequest req)
            throws ControllerException {
        ResponseEntity resultResponse;
        try{
            LOGGER.info("get role list controller method");
            resultResponse = new ResponseEntity(roleService.read(), HttpStatus.OK);
        } catch (ServiceException exc){
            resultResponse = new ResponseEntity(new ArrayList<>(), HttpStatus.OK);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity create(@RequestBody Role role)
            throws ControllerException {
        ResponseEntity resultResponse;
        try{
            resultResponse = new ResponseEntity(roleService.create(role), HttpStatus.OK);
        } catch (ServiceException exc){
            resultResponse = new ResponseEntity(new ErrorObject("Error in role creating"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity update(@RequestBody Role role)
            throws ControllerException {
        ResponseEntity resultResponse;
        try{
            resultResponse = new ResponseEntity(roleService.update(role), HttpStatus.OK);
        } catch (ServiceException exc){
            resultResponse = new ResponseEntity(new ErrorObject("Error in role updating"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(value = "/{roleId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(@PathVariable("roleId") int roleId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try{
            roleService.delete(roleId);
            resultResponse = new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException exc){
            resultResponse = new ResponseEntity(new ErrorObject("Error in role deleting"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }
}
