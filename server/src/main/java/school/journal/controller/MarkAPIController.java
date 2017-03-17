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

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

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
            resultResponse = new ResponseEntity(markService.read(), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Error in getting mark list"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity create(HttpServletRequest request, @RequestBody Mark mark)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Create Mark controller method");
            resultResponse = new ResponseEntity(markService.create(mark), HttpStatus.CREATED);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Error in mark creation"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity update(HttpServletRequest request, @RequestBody Mark mark)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Update Mark controller method");
            resultResponse = new ResponseEntity(markService.update(mark), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Error in mark updating"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(value = "/{markId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(HttpServletRequest request, @PathVariable("markId") int markId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Delete mark controller method");
            markService.delete(markId);
            resultResponse = new ResponseEntity(OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Error in mark deleting"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(value = "/{markId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getOne(HttpServletRequest request, @PathVariable("markId") int markId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get mark entity Controller method");
            resultResponse = new ResponseEntity(markService.getOne(markId), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Error in mark getting"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getMarksForSubjectInClass(HttpServletRequest request, @RequestParam(value = "subjectId") int subjectId, @RequestParam(value = "classId") int classId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get mark entities by subject and class Controller method");
            resultResponse = new ResponseEntity(markService.
                    getMarksForSubjectInClass(subjectId, classId), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Error in class getting"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getMarksForTermOrder(HttpServletRequest request, @RequestParam(value = "classId") int classId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get mark entities by class and time terms Controller method");
            resultResponse = new ResponseEntity(markService.getMarksForTermOrder(classId), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(
                    new ErrorObject("Error in mark getting"),
                    BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(
                    new ErrorObject("Some critical error"),
                    INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getMarksForPupil(HttpServletRequest request, @RequestParam(value = "pupilId") int pupilId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get mark entities by pupil and time terms Controller method");
            resultResponse = new ResponseEntity(markService.
                    getMarksForPupil(pupilId), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(
                    new ErrorObject("Error in mark getting"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(
                    new ErrorObject("Some critical error"),
                    INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }
}
