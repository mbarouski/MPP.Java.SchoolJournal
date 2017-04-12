package school.journal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import school.journal.controller.exception.ControllerException;
import school.journal.entity.Term;
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

    @GetMapping
    @ResponseBody
    public ResponseEntity getCurrentTerm(HttpServletRequest request)
            throws ControllerException {
        return doResponse(termService::getCurrentTerm, "Can't get current term", LOGGER);
    }
}
