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
import school.journal.entity.SubjectInSchedule;
import school.journal.entity.User;
import school.journal.service.ISubjectInScheduleService;
import school.journal.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static school.journal.controller.util.ErrorObject.CRITICAL_ERROR;

@Controller
@RequestMapping(value = "/api/schedule")
public class SubjectInScheduleAPIController {
    private Logger LOGGER = Logger.getLogger(SubjectInScheduleAPIController.class);

    @Autowired
    @Qualifier("SubjectInScheduleService")
    private ISubjectInScheduleService subjectInScheduleService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getFullSchedule(HttpServletRequest req)
            throws ControllerException {
        ResponseEntity resultResponse;
        try{
            LOGGER.info("get subject in schedule list controller method");
            resultResponse = new ResponseEntity(subjectInScheduleService.read(), HttpStatus.OK);
        } catch (ServiceException exc){
            resultResponse = new ResponseEntity(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @GetMapping("/class/{classId}")
    @ResponseBody
    public ResponseEntity getPupilSchedule(HttpServletRequest request, @PathVariable int classId)
            throws ControllerException {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = new ResponseEntity(subjectInScheduleService.getPupilSchedule(classId), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Can't get pupil schedule"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @GetMapping("/teacher")
    @ResponseBody
    public ResponseEntity getTeacherShedule(HttpServletRequest request)
            throws ControllerException {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = new ResponseEntity(subjectInScheduleService.getTeacherSchedule(((User)request.getAttribute("user")).getUserId()), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Can't get teacher schedule"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity create(@RequestBody SubjectInSchedule subject)
            throws ControllerException {
        ResponseEntity resultResponse;
        try{
            resultResponse = new ResponseEntity(subjectInScheduleService.create(subject), HttpStatus.OK);
        } catch (ServiceException exc){
            resultResponse = new ResponseEntity(new ErrorObject("Error in subjectInSchedule creating"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity update(@RequestBody SubjectInSchedule subjectInSchedule)
            throws ControllerException {
        ResponseEntity resultResponse;
        try{
            resultResponse = new ResponseEntity(subjectInScheduleService.update(subjectInSchedule), HttpStatus.OK);
        } catch (ServiceException exc){
            resultResponse = new ResponseEntity(new ErrorObject("Error in subject updating"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(value = "/{subjectInScheduleId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(@PathVariable("subjectInScheduleId") int subjectId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try{
            subjectInScheduleService.delete(subjectId);
            resultResponse = new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException exc){
            resultResponse = new ResponseEntity(new ErrorObject("Error in subjectInSchedule deleting"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }
}
