package school.journal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/api/pupil")
public class PupilAPIController {

    private static Logger LOGGER = Logger.getLogger(PupilAPIController.class);

    @Autowired
    private IPupilService pupilService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity get(HttpServletRequest request)
            throws ControllerException {
        try {
            LOGGER.info("Get Pupil list controller method");
            return new ResponseEntity(pupilService.read(), HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            return new ResponseEntity(new ErrorObject("Error in getting pupil list"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity create(HttpServletRequest request,
                                 @RequestBody Pupil pupil)
            throws ControllerException {
        try {
            LOGGER.info("Create Pupil controller method");
            return new ResponseEntity(pupilService.create(pupil),
                    HttpStatus.CREATED);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            return new ResponseEntity(new ErrorObject("Error in pupil creation"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity update(HttpServletRequest request,
                                 @RequestBody Pupil pupil)
            throws ControllerException {
        try {
            LOGGER.info("Update Pupil controller method");
            return new ResponseEntity(pupilService.update(pupil), HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            return new ResponseEntity(new ErrorObject("Error in pupil updating"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{pupilId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(HttpServletRequest request,
                                 @PathVariable("pupilId") int pupilId)
            throws ControllerException {
        try {
            LOGGER.info("Delete Pupil controller method");
            pupilService.delete(pupilId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            return new ResponseEntity(new ErrorObject("Error in mark deleting"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{pupilId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getOne(HttpServletRequest request,
                                 @PathVariable("pupilId") int pupilId)
            throws ControllerException {
        try {
            LOGGER.info("Get Pupil entity Controller method");
            return new ResponseEntity(pupilService.getOne(pupilId),
                    HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            return new ResponseEntity(new ErrorObject("Error in pupil getting"),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
