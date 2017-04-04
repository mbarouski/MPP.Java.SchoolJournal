package school.journal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import school.journal.controller.exception.ControllerException;
import school.journal.controller.util.ErrorObject;
import school.journal.entity.Pupil;
import school.journal.service.IPupilService;
import school.journal.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static school.journal.controller.util.ErrorObject.CRITICAL_ERROR;

@CrossOrigin
@SuppressWarnings("unchecked")
@Controller
@RequestMapping(value = "/api/pupils")
public class PupilAPIController {

    private static Logger LOGGER = Logger.getLogger(PupilAPIController.class);

    @Autowired
    @Qualifier("PupilService")
    private IPupilService pupilService;

    @RequestMapping(method = GET)
    @ResponseBody
    public ResponseEntity get(HttpServletRequest request)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get Pupil list controller method");
            resultResponse = new ResponseEntity(pupilService.read(), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Pupil Controller", "Get full list", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = POST)
    @ResponseBody
    public ResponseEntity create(HttpServletRequest request, @RequestBody Pupil pupil)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Create Pupil controller method");
            resultResponse = new ResponseEntity(pupilService.create(pupil), CREATED);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Pupil Controller", "Create", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = PUT)
    @ResponseBody
    public ResponseEntity update(HttpServletRequest request, @RequestBody Pupil pupil)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Update Pupil controller method");
            resultResponse = new ResponseEntity(pupilService.update(pupil), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Pupil Controller", "Update", exc),
                    BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(value = "/{pupilId}", method = DELETE)
    @ResponseBody
    public ResponseEntity delete(HttpServletRequest request, @PathVariable("pupilId") int pupilId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Delete Pupil controller method");
            pupilService.delete(pupilId);
            resultResponse = new ResponseEntity(OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Pupil Controller", "Delete", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(value = "/{pupilId}", method = GET)
    @ResponseBody
    public ResponseEntity getPupilInfo(HttpServletRequest request, @PathVariable("pupilId") int pupilId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get Pupil entity Controller method");
            resultResponse = new ResponseEntity(pupilService.getPupilInfo(pupilId), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Pupil Controller", "Get one", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = GET, params = "classId")
    @ResponseBody
    public ResponseEntity getListOfPupilsInClass(HttpServletRequest request, @RequestParam(value = "classId") int classId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get Pupil entities Controller method");
            resultResponse = new ResponseEntity(pupilService.getListOfPupils(classId), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Pupil Controller", "Get list for class", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = PUT, params = {"pupilId", "classId"})
    @ResponseBody
    public ResponseEntity movePupilToAnotherClass(HttpServletRequest request, @RequestParam(value = "pupilId") int pupilId, @RequestParam(value = "classId") Integer classId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Update Pupil controller method");
            resultResponse = new ResponseEntity(pupilService.movePupilToAnotherClass(pupilId, classId), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Pupil Controller", "Move to another Class", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

}
