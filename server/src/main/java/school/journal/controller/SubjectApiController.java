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
import school.journal.entity.Subject;
import school.journal.service.ISubjectService;
import school.journal.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping(value = "/api/subjects")
public class SubjectAPIController {
    private Logger LOGGER = Logger.getLogger(SubjectAPIController.class);

    @Autowired
    @Qualifier("SubjectService")
    private ISubjectService subjectService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity get(HttpServletRequest req)
            throws ControllerException {
        ResponseEntity resultResponse;
        try{
            LOGGER.info("get subject list controller method");
            resultResponse = new ResponseEntity(subjectService.read(), HttpStatus.OK);
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
    public ResponseEntity create(@RequestBody Subject subject)
            throws ControllerException {
        ResponseEntity resultResponse;
        try{
            resultResponse = new ResponseEntity(subjectService.create(subject), HttpStatus.OK);
        } catch (ServiceException exc){
            resultResponse = new ResponseEntity(new ErrorObject("Error in subject creating"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity update(@RequestBody Subject subject)
            throws ControllerException {
        ResponseEntity resultResponse;
        try{
            resultResponse = new ResponseEntity(subjectService.update(subject), HttpStatus.OK);
        } catch (ServiceException exc){
            resultResponse = new ResponseEntity(new ErrorObject("Error in subject updating"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(value = "/{subjectId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(@PathVariable("subjectId") int subjectId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try{
            subjectService.delete(subjectId);
            resultResponse = new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException exc){
            resultResponse = new ResponseEntity(new ErrorObject("Error in subject deleting"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(value = "/{subjectId}")
    @ResponseBody
    public ResponseEntity getOne(@PathVariable("subjectId") int subjectId)
            throws ControllerException {
        ResponseEntity resultResponse;
        try{
            resultResponse = new ResponseEntity(subjectService.getOne(subjectId), HttpStatus.OK);
        } catch (ServiceException exc){
            resultResponse = new ResponseEntity(new ErrorObject("Error in subject getting"), HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }
}
