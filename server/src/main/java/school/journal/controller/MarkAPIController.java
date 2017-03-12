package school.journal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import school.journal.controller.exception.ControllerException;
import school.journal.controller.util.ErrorObject;
import school.journal.entity.Mark;
import school.journal.service.IMarkService;
import school.journal.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/api/marks")
public class MarkAPIController {

    private static Logger LOGGER = Logger.getLogger(MarkAPIController.class);

    @Autowired
    private IMarkService markService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity get(HttpServletRequest request)
            throws ControllerException {
        try {
            LOGGER.info("Get Mark list controller method");
            return new ResponseEntity(markService.read(), HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            return new ResponseEntity(new ErrorObject("Error in getting mark list"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity create(HttpServletRequest request,
                                 @RequestBody Mark mark)
            throws ControllerException {
        try {
            LOGGER.info("Create Mark controller method");
            return new ResponseEntity(markService.create(mark),
                    HttpStatus.CREATED);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            return new ResponseEntity(new ErrorObject("Error in mark creation"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity update(HttpServletRequest request,
                                 @RequestBody Mark mark)
            throws ControllerException {
        try {
            LOGGER.info("Update Mark controller method");
            return new ResponseEntity(markService.update(mark), HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            return new ResponseEntity(new ErrorObject("Error in mark updating"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{markId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(HttpServletRequest request,
                                 @PathVariable("markId") int markId)
            throws ControllerException {
        try {
            LOGGER.info("Delete mark controller method");
            markService.delete(markId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            return new ResponseEntity(new ErrorObject("Error in mark deleting"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{markId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getOne(HttpServletRequest request,
                                 @PathVariable("markId") int markId)
            throws ControllerException {
        try {
            LOGGER.info("Get mark entity Controller method");
            return new ResponseEntity(markService.getOne(markId),
                    HttpStatus.OK);
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            return new ResponseEntity(new ErrorObject("Error in class getting"),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
