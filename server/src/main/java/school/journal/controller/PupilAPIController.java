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
import school.journal.entity.Pupil;
import school.journal.service.IPupilService;
import school.journal.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/api/pupils")
public class PupilAPIController {

    private static Logger LOGGER = Logger.getLogger(PupilAPIController.class);

    @Autowired
    @Qualifier("PupilService")
    private IPupilService pupilService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity get(HttpServletRequest request)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get Pupil list controller method");
            resultResponse = new ResponseEntity(pupilService.read(), HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Error in getting pupil list"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity create(HttpServletRequest request,
                                 @RequestBody Pupil pupil)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Create Pupil controller method");
            resultResponse = new ResponseEntity(pupilService.create(pupil), HttpStatus.CREATED);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Error in pupil creation"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity update(HttpServletRequest request,
                                 @RequestBody Pupil pupil)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Update Pupil controller method");
            resultResponse = new ResponseEntity(pupilService.update(pupil), HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Error in pupil updating"),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(value = "/{pupilId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(HttpServletRequest request,
                                 @PathVariable("pupilId") int pupilId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Delete Pupil controller method");
            pupilService.delete(pupilId);
            resultResponse = new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Error in mark deleting"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(value = "/{pupilId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getOne(HttpServletRequest request,
                                 @PathVariable("pupilId") int pupilId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get Pupil entity Controller method");
            resultResponse = new ResponseEntity(pupilService.getOne(pupilId),
                    HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Error in pupil getting"),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }
}
