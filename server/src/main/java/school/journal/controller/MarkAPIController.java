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
import school.journal.entity.Mark;
import school.journal.service.IMarkService;
import school.journal.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@SuppressWarnings(value = "unchecked")
@Controller
@RequestMapping(value = "/api/marks")
public class MarkAPIController {

    private static Logger LOGGER = Logger.getLogger(MarkAPIController.class);

    @Autowired
    @Qualifier("MarkService")
    private IMarkService markService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity get(HttpServletRequest request)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get Mark list controller method");
            resultResponse = new ResponseEntity(markService.read(), HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Error in getting mark list"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity create(HttpServletRequest request,
                                 @RequestBody Mark mark)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Create Mark controller method");
            resultResponse = new ResponseEntity(markService.create(mark), HttpStatus.CREATED);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Error in mark creation"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity update(HttpServletRequest request,
                                 @RequestBody Mark mark)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Update Mark controller method");
            resultResponse = new ResponseEntity(markService.update(mark), HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Error in mark updating"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(value = "/{markId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(HttpServletRequest request,
                                 @PathVariable("markId") int markId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Delete mark controller method");
            markService.delete(markId);
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

    @RequestMapping(value = "/{markId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getOne(HttpServletRequest request,
                                 @PathVariable("markId") int markId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get mark entity Controller method");
            resultResponse = new ResponseEntity(markService.getOne(markId), HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Error in class getting"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getMarksForSubjectInClass(
            HttpServletRequest request,
            @RequestParam(value = "subjectId") int subjectId,
            @RequestParam(value = "classId") int classId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get mark entities by subject and class Controller method");
            resultResponse = new ResponseEntity(markService.
                    getMarksForSubjectInClass(subjectId, classId), HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Error in class getting"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getMarksForTermOrder(
            HttpServletRequest request,
            @RequestParam(value = "classId") int classId,
            @RequestParam(value = "startTerm") Date startTerm,
            @RequestParam(value = "endTerm") Date endTerm)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get mark entities by class and time terms Controller method");
            resultResponse = new ResponseEntity(markService.
                    getMarksForTermOrder(classId, startTerm, endTerm),
                    HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(
                    new ErrorObject("Error in class getting"),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(
                    new ErrorObject("Some critical error"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getMarksForPupil(
            HttpServletRequest request,
            @RequestParam(value = "pupilId") int pupilId,
            @RequestParam(value = "startTerm") Date startTerm,
            @RequestParam(value = "endTerm") Date endTerm)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get mark entities by pupil and time terms Controller method");
            resultResponse = new ResponseEntity(markService.
                    getMarksForPupil(pupilId, startTerm, endTerm), HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(
                    new ErrorObject("Error in class getting"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(
                    new ErrorObject("Some critical error"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }
}
