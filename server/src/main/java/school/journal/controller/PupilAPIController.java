package school.journal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import school.journal.aop.Secured;
import school.journal.controller.exception.ControllerException;
import school.journal.entity.Pupil;
import school.journal.entity.enums.RoleEnum;
import school.journal.service.IPupilService;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/pupils")
public class PupilAPIController extends BaseController<Pupil> {
    private static final Logger LOGGER = Logger.getLogger(PupilAPIController.class);
    private final IPupilService pupilService;

    @Autowired
    public PupilAPIController(@Qualifier("PupilService") IPupilService pupilService) {
        this.pupilService = pupilService;
    }

    @RequestMapping(method = GET)
    @ResponseBody
    @Secured(RoleEnum.PUPIL)
    public ResponseEntity get(HttpServletRequest request)
            throws ControllerException {
        return read(pupilService::read, "Can't get pupil list", LOGGER);
    }

    @RequestMapping(method = POST)
    @ResponseBody
    @Secured(RoleEnum.DIRECTOR_OF_STUDIES)
    public ResponseEntity create(HttpServletRequest request, @RequestBody Pupil pupil)
            throws ControllerException {
        return createOrUpdate(pupilService::create, pupil, "Can't create pupil", LOGGER);
    }

    @RequestMapping(method = PUT)
    @ResponseBody
    @Secured(RoleEnum.DIRECTOR_OF_STUDIES)
    public ResponseEntity update(HttpServletRequest request, @RequestBody Pupil pupil)
            throws ControllerException {
        return createOrUpdate(pupilService::update, pupil, "Can't update pupil", LOGGER);
    }

    @RequestMapping(value = "/{pupilId}", method = DELETE)
    @ResponseBody
    @Secured(RoleEnum.DIRECTOR_OF_STUDIES)
    public ResponseEntity delete(HttpServletRequest request, @PathVariable("pupilId") int pupilId)
            throws ControllerException {
        return delete(pupilService::delete, pupilId, "Can't delete pupil", LOGGER);
    }

    @RequestMapping(value = "/{pupilId}", method = GET)
    @ResponseBody
    @Secured(RoleEnum.PUPIL)
    public ResponseEntity getPupilInfo(HttpServletRequest request, @PathVariable("pupilId") int pupilId)
            throws ControllerException {
        return getOne(pupilService::getOne, pupilId, "Can't get info about Pupil", LOGGER);
    }

    @RequestMapping(method = GET, params = "classId")
    @ResponseBody
    @Secured(RoleEnum.PUPIL)
    public ResponseEntity getListOfPupilsInClass(HttpServletRequest request,
                                                 @RequestParam(value = "classId") int classId)
            throws ControllerException {
        return doResponse(pupilService::getListOfPupils, classId,
                "Can't get list of pupils of class", LOGGER);
    }

    @RequestMapping(method = PUT, params = {"pupilId", "classId"})
    @ResponseBody
    @Secured(RoleEnum.DIRECTOR_OF_STUDIES)
    public ResponseEntity movePupilToAnotherClass(HttpServletRequest request,
                                                  @RequestParam(value = "pupilId") Integer pupilId,
                                                  @RequestParam(value = "classId") Integer classId)
            throws ControllerException {
        return doResponse(pupilService::movePupilToAnotherClass, pupilId, classId,
                "Can't move pupil to another class", LOGGER, false);
    }

    @RequestMapping(method = GET, value = "/withoutClass")
    @ResponseBody
    @Secured(RoleEnum.DIRECTOR_OF_STUDIES)
    public ResponseEntity getPupilsWithoutClass(HttpServletRequest request)
            throws ControllerException {
        return read(pupilService::getPupilsWithoutClass, "Can't get pupils without class", LOGGER);
    }

    @RequestMapping(method = PUT, params = {"pupilId"}, value = "/removeFromClass")
    @ResponseBody
    @Secured(RoleEnum.DIRECTOR_OF_STUDIES)
    public ResponseEntity removeFromClass(HttpServletRequest request, @RequestParam(value = "pupilId") int pupilId)
            throws ControllerException {
        return doResponse(pupilService::removeFromClass, pupilId, "Can't remove pupil from class", LOGGER);
    }
}
