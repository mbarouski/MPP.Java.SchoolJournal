package school.journal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import school.journal.aop.Secured;
import school.journal.controller.exception.ControllerException;
import school.journal.entity.LessonTime;
import school.journal.entity.Term;
import school.journal.entity.enums.RoleEnum;
import school.journal.service.IAuthService;
import school.journal.service.ILessonTimeService;
import school.journal.service.ITermService;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/lessons")
public class LessonTimeAPIController extends BaseController<LessonTime> {

    private static Logger LOGGER = Logger.getLogger(LessonTimeAPIController.class);

    private final ILessonTimeService lessonTimeService;

    @Autowired
    public LessonTimeAPIController(@Qualifier("LessonTimeService") ILessonTimeService lessonTimeService) {
        this.lessonTimeService = lessonTimeService;
    }

    @GetMapping
    @ResponseBody
    @Secured(RoleEnum.USER)
    public ResponseEntity getAll(HttpServletRequest request)
            throws ControllerException {
        return read(lessonTimeService::getLessonTimeList, "Can't get full lesson list", LOGGER);
    }

    @PutMapping
    @ResponseBody
    @Secured(RoleEnum.DIRECTOR_OF_STUDIES)
    public ResponseEntity update(HttpServletRequest request, @RequestBody LessonTime lesson)
            throws ControllerException {
        return createOrUpdate(lessonTimeService::update, lesson, "Can't update lesson", LOGGER);
    }
}
