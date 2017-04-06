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
public class PupilAPIController extends BaseController<Pupil>{

    private static Logger LOGGER = Logger.getLogger(PupilAPIController.class);

    private final IPupilService pupilService;

    @Autowired
    public PupilAPIController(@Qualifier("PupilService") IPupilService pupilService) {
        this.pupilService = pupilService;
    }

    @RequestMapping(method = GET)
    @ResponseBody
    public ResponseEntity get(HttpServletRequest request)
            throws ControllerException {
        return read(pupilService, "Can't get pupil list", LOGGER);
    }

    @RequestMapping(method = POST)
    @ResponseBody
    public ResponseEntity create(HttpServletRequest request, @RequestBody Pupil pupil)
            throws ControllerException {
        return create(pupilService,pupil,"Can't create pupil", LOGGER);
    }

    @RequestMapping(method = PUT)
    @ResponseBody
    public ResponseEntity update(HttpServletRequest request, @RequestBody Pupil pupil)
            throws ControllerException {
        return update(pupilService, pupil, "Can't update pupil", LOGGER);
    }

    @RequestMapping(value = "/{pupilId}", method = DELETE)
    @ResponseBody
    public ResponseEntity delete(HttpServletRequest request, @PathVariable("pupilId") int pupilId)
            throws ControllerException {
        return delete(pupilService, pupilId, "Can't delete pupil", LOGGER);
    }

    @RequestMapping(value = "/{pupilId}", method = GET)
    @ResponseBody
    public ResponseEntity getPupilInfo(HttpServletRequest request, @PathVariable("pupilId") int pupilId)
            throws ControllerException {
        return getOne(pupilService::getOne, pupilId, "Can't get info about Pupil", LOGGER);
    }

    @RequestMapping(method = GET, params = "classId")
    @ResponseBody
    public ResponseEntity getListOfPupilsInClass(HttpServletRequest request, @RequestParam(value = "classId") int classId)
            throws ControllerException {
        return doResponse(pupilService::getListOfPupils, classId, "Can't get list of pupils of class", LOGGER);
    }

    @RequestMapping(method = PUT, params = {"pupilId", "classId"})
    @ResponseBody
    public ResponseEntity movePupilToAnotherClass(HttpServletRequest request, @RequestParam(value = "pupilId") int pupilId, @RequestParam(value = "classId") Integer classId)
            throws ControllerException {
        return doResponse(pupilService::movePupilToAnotherClass, pupilId, classId, "Can't move pupil to another class", LOGGER, false);
    }

}
