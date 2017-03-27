package school.journal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import school.journal.controller.exception.ControllerException;
import school.journal.controller.util.ErrorObject;
import school.journal.entity.Clazz;
import school.journal.service.exception.ServiceException;
import school.journal.service.impl.ClassService;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;
import static school.journal.controller.util.ErrorObject.CRITICAL_ERROR;

@Controller
@RequestMapping(value = "/api/classes")
public class ClassAPIController {

    private static Logger LOGGER = Logger.getLogger(ClassAPIController.class);
    private static final String classSubject = "Class Controller";

    @Autowired
    @Qualifier("ClassService")
    private ClassService classService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity get(HttpServletRequest request)
            throws ControllerException {
        ResponseEntity resultResponse = null;
        try {
            LOGGER.info("Get Class list controller method");
            resultResponse = new ResponseEntity(classService.read(), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject(classSubject, "get full list", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity create(HttpServletRequest request, @RequestBody Clazz clazz)
            throws ControllerException {
        ResponseEntity resultResponse = null;
        try {
            LOGGER.info("Create Class controller method");
            resultResponse = new ResponseEntity(classService.create(clazz), CREATED);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject(classSubject, "Create", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity update(HttpServletRequest request, @RequestBody Clazz clazz)
            throws ControllerException {
        ResponseEntity resultResponse = null;
        try {
            LOGGER.info("Update Class controller method");
            resultResponse = new ResponseEntity(classService.update(clazz), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject(classSubject, "Update", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(value = "/{classId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(HttpServletRequest request, @PathVariable("classId") int classId)
            throws ControllerException {
        ResponseEntity resultResponse = null;
        try {
            LOGGER.info("Delete Class controller method");
            classService.delete(classId);
            resultResponse = new ResponseEntity(OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject(classSubject, "Delete", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(value = "/{classId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getOne(HttpServletRequest request, @PathVariable("classId") int classId)
            throws ControllerException {
        ResponseEntity resultResponse = null;
        try {
            LOGGER.info("Get Class entity Controller method");
            resultResponse = new ResponseEntity(classService.getOne(classId), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject(classSubject, "Delete", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }
}

