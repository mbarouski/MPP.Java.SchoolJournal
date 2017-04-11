package school.journal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.journal.controller.exception.ControllerException;
import school.journal.controller.util.ErrorObject;
import school.journal.entity.Teacher;
import school.journal.service.ITeacherService;
import school.journal.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static school.journal.controller.util.ErrorObject.CRITICAL_ERROR;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/teachers")
public class TeacherAPIController extends BaseController<Teacher> {
    private static final Logger LOGGER = Logger.getLogger(TeacherAPIController.class);

    @Autowired
    @Qualifier("TeacherService")
    private ITeacherService teacherService;

    @PostMapping
    public ResponseEntity create(HttpServletRequest request, @RequestBody Teacher teacher) throws ControllerException {
        return createOrUpdate((Teacher t) -> teacherService.create(t), teacher, "Can't create teacher", LOGGER);
    }

    @PutMapping
    public ResponseEntity update(HttpServletRequest request, @RequestBody Teacher teacher) throws ControllerException {
        return createOrUpdate((Teacher t) -> teacherService.update(t), teacher, "Can't update teacher", LOGGER);
    }

    @DeleteMapping("/{teacherId}")
    public ResponseEntity delete(HttpServletRequest request, @PathVariable("teacherId") int teacherId) throws ControllerException {
        return delete((int id) -> teacherService.delete(id), teacherId, "Can't delete teacher by id", LOGGER);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity getListOfTeachersForClass(HttpServletRequest request, @PathVariable("classId") int classId)
            throws ControllerException {
        return doResponse((int id) -> teacherService.getListOfTeachersForClass(id), classId,
                "Can't get teacher list for class with class id", LOGGER);
    }

    @RequestMapping(value = "/{teacherId}", method = RequestMethod.GET)
    public ResponseEntity getOne(HttpServletRequest request, @PathVariable("teacherId") int teacherId) throws ControllerException {
        return getOne((int i) -> teacherService.getOne(i), teacherId, "Can't get teacher by id", LOGGER);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity getAll(HttpServletRequest request) throws ControllerException {
        return read(() -> teacherService.read(), "Can't get teacher list", LOGGER);
    }

    @PostMapping(value = "/changeClassOfTeacher", params = {"teacherId", "classId"})
    public ResponseEntity changeClassOfTeacher(HttpServletRequest request, @RequestParam("teacherId") int teacherId, @RequestParam("classId") int classId)
            throws ControllerException {
        return doResponse((int tId, int cId) -> teacherService.changeClassOfTeacher(tId, cId),
                teacherId, classId, "Can't change class of teacher", LOGGER, false);
    }

    @PostMapping("/changeDirectorOfStudiesStatus/{teacherId}")
    public ResponseEntity changeDirectorOfStudiesStatus(HttpServletRequest request, @PathVariable("teacherId") int teacherId, @RequestParam("isDirector") boolean isDirector)
            throws ControllerException {
        return doResponse((int i, boolean f) -> teacherService.changeDirectorOfStudies(i, f), teacherId, isDirector,
                "Can't change director of studies status", LOGGER);
    }
}
