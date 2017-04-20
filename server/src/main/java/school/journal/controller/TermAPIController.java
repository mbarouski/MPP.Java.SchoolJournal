package school.journal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import school.journal.aop.Secured;
import school.journal.controller.exception.ControllerException;
import school.journal.entity.Term;
import school.journal.entity.enums.RoleEnum;
import school.journal.service.CRUDService;
import school.journal.service.ITermService;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/terms")
public class TermAPIController extends BaseController<Term> {

    private static Logger LOGGER = Logger.getLogger(TermAPIController.class);

    @Autowired
    @Qualifier("TermService")
    private ITermService termService;

    @GetMapping("/current")
    @ResponseBody
    @Secured(RoleEnum.USER)
    public ResponseEntity getCurrentTerm(HttpServletRequest request)
            throws ControllerException {
        return doResponse(termService::getCurrentTerm, "Can't get current term", LOGGER);
    }

    @GetMapping
    @ResponseBody
    @Secured(RoleEnum.USER)
    public ResponseEntity getTerms(HttpServletRequest request)
            throws ControllerException {
        return read(termService::read, "Can't get all terms", LOGGER);
    }

    @PutMapping
    @ResponseBody
    @Secured(RoleEnum.DIRECTOR_OF_STUDIES)
    public ResponseEntity update(HttpServletRequest request, @RequestBody Term term)
            throws ControllerException {
        return createOrUpdate(termService::update, term, "Can't update term", LOGGER);
    }
}
