package school.journal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import school.journal.controller.exception.ControllerException;
import school.journal.controller.util.ErrorObject;
import school.journal.service.ISubjectInScheduleService;
import school.journal.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping(value = "/api/schedule")
public class SubjectInScheduleAPIController {
    private Logger LOGGER = Logger.getLogger(SubjectInScheduleAPIController.class);

    @Autowired
    @Qualifier("SubjectInScheduleService")
    private ISubjectInScheduleService subjectInScheduleService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity get(HttpServletRequest req)
            throws ControllerException {
        ResponseEntity resultResponse;
        try{
            LOGGER.info("get subject in schedule list controller method");
            resultResponse = new ResponseEntity(subjectInScheduleService.read(), HttpStatus.OK);
        } catch (ServiceException exc){
            resultResponse = new ResponseEntity(new ArrayList<>(), HttpStatus.OK);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Some critical error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }


}
