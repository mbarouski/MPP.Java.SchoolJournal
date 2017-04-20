package school.journal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import school.journal.aop.Secured;
import school.journal.controller.exception.ControllerException;
import school.journal.controller.util.ErrorObject;
import school.journal.entity.SubjectInSchedule;
import school.journal.entity.User;
import school.journal.entity.enums.RoleEnum;
import school.journal.service.CRUDService;
import school.journal.service.ISubjectInScheduleService;
import school.journal.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static school.journal.controller.util.ErrorObject.CRITICAL_ERROR;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/schedule")
public class SubjectInScheduleAPIController extends BaseController<SubjectInSchedule> {
    private Logger LOGGER = Logger.getLogger(SubjectInScheduleAPIController.class);

    @Autowired
    @Qualifier("SubjectInScheduleService")
    private ISubjectInScheduleService subjectInScheduleService;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    @Secured(RoleEnum.PUPIL)
    public ResponseEntity getOne(HttpServletRequest req, @PathVariable("id") int subjectId)
            throws ControllerException {
        return getOne(((CRUDService<SubjectInSchedule>) subjectInScheduleService)::getOne, subjectId, "Can't get subject in schedule", LOGGER);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Secured(RoleEnum.PUPIL)
    public ResponseEntity getFullSchedule(HttpServletRequest req)
            throws ControllerException {
        return read(() -> subjectInScheduleService.read(), "Can't get full schedule of sujects", LOGGER);
    }

    @GetMapping("/class/{classId}")
    @ResponseBody
    @Secured(RoleEnum.PUPIL)
    public ResponseEntity getPupilSchedule(HttpServletRequest request, @PathVariable int classId)
            throws ControllerException {
        return read(() -> subjectInScheduleService.getPupilSchedule(classId), "Can't get pupil schedule", LOGGER);
    }

    @GetMapping(params = {"classId", "teacherId", "subjectId"})
    @ResponseBody
    @Secured(RoleEnum.PUPIL)
    public ResponseEntity getSubjectsWithTeacherClassSubject(HttpServletRequest request,
                                                             @RequestParam("classId") int classId,
                                                             @RequestParam("teacherId") int teacherId,
                                                             @RequestParam("subjectId") int subjectId)
            throws ControllerException {
        return doResponse(subjectInScheduleService::getSubjectsWithTeacherClassSubject, classId, teacherId, subjectId, "Can't get subjects", LOGGER);
    }

    @GetMapping("/teacher")
    @ResponseBody
    @Secured(RoleEnum.PUPIL)
    public ResponseEntity getTeacherShedule(HttpServletRequest request)
            throws ControllerException {
        return read(() -> subjectInScheduleService.getTeacherSchedule(((User) request.getAttribute("user")).getUserId()), "Can't get teacher schedule", LOGGER);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Secured(RoleEnum.DIRECTOR_OF_STUDIES)
    public ResponseEntity create(HttpServletRequest req, @RequestBody SubjectInSchedule subjectInSchedule)
            throws ControllerException {
        return createOrUpdate((SubjectInSchedule s) -> subjectInScheduleService.create(s), subjectInSchedule, "Can't create subject in schedule", LOGGER);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    @Secured(RoleEnum.DIRECTOR_OF_STUDIES)
    public ResponseEntity update(HttpServletRequest req, @RequestBody SubjectInSchedule subjectInSchedule)
            throws ControllerException {
        return createOrUpdate((SubjectInSchedule s) -> subjectInScheduleService.update(s), subjectInSchedule, "Can't update subjectInSchedule", LOGGER);
    }

    @RequestMapping(value = "/{subjectInScheduleId}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured(RoleEnum.DIRECTOR_OF_STUDIES)
    public ResponseEntity delete(HttpServletRequest req, @PathVariable("subjectInScheduleId") int subjectId)
            throws ControllerException {
        return delete((int id) -> subjectInScheduleService.delete(id), subjectId, "Can't delete subjectInShcedule by id", LOGGER);
    }
}
