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
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static school.journal.controller.util.ErrorObject.CRITICAL_ERROR;

@Controller
@RequestMapping(value = "/api/marks")
public class MarkAPIController {

    private static Logger LOGGER = Logger.getLogger(MarkAPIController.class);

    @Autowired
    @Qualifier("MarkService")
    private IMarkService markService;

    @RequestMapping(method = GET)
    @ResponseBody
    public ResponseEntity get(HttpServletRequest request)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get Mark list controller method");
            resultResponse = new ResponseEntity(markService.read(), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Mark Controller", "Get full list", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = POST)
    @ResponseBody
    public ResponseEntity create(HttpServletRequest request, @RequestBody Mark mark)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Create Mark controller method");
            resultResponse = new ResponseEntity(markService.create(mark), HttpStatus.CREATED);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Mark Controller", "Create", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = PUT)
    @ResponseBody
    public ResponseEntity update(HttpServletRequest request, @RequestBody Mark mark)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Update Mark controller method");
            resultResponse = new ResponseEntity(markService.update(mark), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Mark Controller", "Update", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(value = "/{markId}", method = DELETE)
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
            resultResponse = new ResponseEntity(new ErrorObject("Mark Controller", "Delete", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(value = "/{markId}", method = GET)
    @ResponseBody
    public ResponseEntity getOne(HttpServletRequest request, @PathVariable("markId") int markId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get mark entity Controller method");
            resultResponse = new ResponseEntity(markService.getOne(markId), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Mark Controller", "Get by id", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = GET, params = {"subjectId", "classId"})
    @ResponseBody
    public ResponseEntity getMarksForSubjectInClass(HttpServletRequest request, @RequestParam(value = "subjectId") int subjectId, @RequestParam(value = "classId") int classId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            resultResponse = new ResponseEntity(markService.
                    getMarksForSubjectInClass(subjectId, classId), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Mark Controller", "Get for Subject In Class", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = GET, params = "classId")
    @ResponseBody
    public ResponseEntity getMarksForTermOrder(HttpServletRequest request, @RequestParam(value = "classId") int classId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get mark entities by class and time terms Controller method");
            resultResponse = new ResponseEntity(markService.getMarksForTermOrder(classId), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Mark Controller", "Get for class", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = GET, params = "pupilId")
    @ResponseBody
    public ResponseEntity getMarksForPupil(HttpServletRequest request, @RequestParam(value = "pupilId") int pupilId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try {
            LOGGER.info("Get mark entities by pupil and time terms Controller method");
            resultResponse = new ResponseEntity(markService.getMarksForPupil(pupilId), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Mark Controller", "Get for pupil id", exc), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }
}
