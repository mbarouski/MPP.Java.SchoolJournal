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
public class SubjectAPIController extends BaseController<Subject>{
    private Logger LOGGER = Logger.getLogger(SubjectAPIController.class);

    @Autowired
    @Qualifier("SubjectService")
    private ISubjectService subjectService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity get(HttpServletRequest req)
            throws ControllerException {
        return read(() -> subjectService.read(), "Can't get full subjects list", LOGGER);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity create(@RequestBody Subject subject)
            throws ControllerException {
        return createOrUpdate((Subject s) -> subjectService.create(s), subject, "Can't create subject", LOGGER);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity update(@RequestBody Subject subject)
            throws ControllerException {
        return createOrUpdate((Subject s) -> subjectService.update(s), subject, "Can't update subject", LOGGER);
    }

    @RequestMapping(value = "/{subjectId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(@PathVariable("subjectId") int subjectId)
            throws ControllerException {
        return delete((int id) -> subjectService.delete(id), subjectId, "Can't delete subject by id", LOGGER);
    }

    @RequestMapping(value = "/{subjectId}")
    @ResponseBody
    public ResponseEntity getOne(@PathVariable("subjectId") int subjectId)
            throws ControllerException {
        return getOne((int id) -> subjectService.getOne(subjectId), subjectId, "Can't get subject by id", LOGGER);
    }
}
