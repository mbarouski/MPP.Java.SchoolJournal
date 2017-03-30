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

@RestController
@RequestMapping(value = "/api/teachers")
public class TeacherAPIController {
    private static final Logger LOGGER = Logger.getLogger(TeacherAPIController.class);

    @Autowired
    @Qualifier("TeacherService")
    private ITeacherService teacherService;

    @PostMapping
    public ResponseEntity create(HttpServletRequest request, Teacher teacher) throws ControllerException {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = new ResponseEntity(teacherService.create(teacher), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Can't create teacher"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @PutMapping
    public ResponseEntity update(HttpServletRequest request, Teacher teacher) throws ControllerException {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = new ResponseEntity(teacherService.update(teacher), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Can't update teacher"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @DeleteMapping("/{teacherId}")
    public ResponseEntity delete(HttpServletRequest request, @PathVariable("teacherId") int teacherId) throws ControllerException {
        ResponseEntity resultResponse = null;
        try {
            teacherService.delete(teacherId);
            resultResponse = new ResponseEntity(OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Can't delete teacher"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity getListOfTeachersForClass(HttpServletRequest request, @PathVariable("classId") int classId)
            throws ControllerException {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = new ResponseEntity(teacherService.getListOfTeachersForClass(classId), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Can't get teacher list for class"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }



    @RequestMapping(value = "/{teacherId}", method = RequestMethod.GET)
    public ResponseEntity getOne(HttpServletRequest request, @PathVariable("teacherId") int teacherId) throws ControllerException {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = new ResponseEntity(teacherService.getOne(teacherId), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Can't get teacher list"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity getAll(HttpServletRequest request) throws ControllerException {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = new ResponseEntity(teacherService.read(), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Can't get teacher list"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @PostMapping("/{teacherId}/{classId}")
    public ResponseEntity changeClassOfTeacher(HttpServletRequest request, @PathVariable("teacherId") int teacherId, @PathVariable("classId") int classId)
            throws ControllerException {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = new ResponseEntity(teacherService.changeClassOfTeacher(teacherId, classId), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Can't change class of teacher"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }

    @PostMapping("/changeDirectorOfStudiesStatus/{teacherId}")
    public ResponseEntity changeDirectorOfStudiesStatus(HttpServletRequest request, @PathVariable("teacherId") int teacherId, @RequestParam("isDirector") boolean isDirector)
            throws ControllerException {
        ResponseEntity resultResponse = null;
        try {
            resultResponse = new ResponseEntity(teacherService.changeDirectorOfStudies(teacherId, isDirector), OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(new ErrorObject("Can't change director of studies status"), BAD_REQUEST);
        } catch (Exception exc) {
            LOGGER.error(exc);
            resultResponse = new ResponseEntity(CRITICAL_ERROR, INTERNAL_SERVER_ERROR);
        }
        return resultResponse;
    }
}
