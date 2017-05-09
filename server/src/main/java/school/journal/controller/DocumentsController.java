package school.journal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import school.journal.controller.exception.ControllerException;
import school.journal.service.document.generation.DocumentType;
import school.journal.service.document.generation.IGenerationService;
import school.journal.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.IOException;

@CrossOrigin
@Controller
@RequestMapping(value = "/api/docs")
public class DocumentsController {

    @Autowired
    @Qualifier("GenerationService")
    private IGenerationService generationService;

//    @RequestMapping(value = "/getMarksForClass", method = RequestMethod.GET,produces = "text/csv")
//    @ResponseBody
//    public ResponseEntity getMarksForClass(HttpServletRequest request, HttpServletResponse response)
//            throws ControllerException {
//        ResponseEntity responseEntity = null;
//        try {
//            response.setContentType("text/csv");
//            response.setHeader("Content-Disposition", "attachment;filename=mdk.csv");
//            generationService.generateMarksDocument(response.getOutputStream(), DocumentType.CSV, 1, 1);
//            responseEntity = new ResponseEntity(HttpStatus.OK);
//        } catch (ServiceException | IOException exc) {
//            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
//        }
//        return responseEntity;
//    }

    @RequestMapping(value = "/getClassPupilsList/{classId}/pdf", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public ResponseEntity getClassPupilsListPDFDocument(HttpServletRequest request, HttpServletResponse response,
                                @PathVariable("classId") int classId) throws ControllerException {
        ResponseEntity responseEntity = null;
        try {
            response.setContentType("application/pdf");
            generationService.generateClassPupilListDocument(response.getOutputStream(), DocumentType.PDF, classId);
            responseEntity = new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException | IOException exc) {
            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/getClassSchedule/{classId}/pdf", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public ResponseEntity getClassSchedulePDFDocument(HttpServletRequest request, HttpServletResponse response,
                                                      @PathVariable("classId") int classId) throws ControllerException {
        ResponseEntity responseEntity = null;
        try {
            response.setContentType("application/pdf");
            generationService.generateClassScheduleDocument(response.getOutputStream(), DocumentType.PDF, classId);
            responseEntity = new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException | IOException exc) {
            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/getTeacherSchedule/{teacherId}/pdf", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public ResponseEntity getTeacherSchedulePDFDocument(HttpServletRequest request, HttpServletResponse response,
                                                      @PathVariable("teacherId") int teacherId) throws ControllerException {
        ResponseEntity responseEntity = null;
        try {
            response.setContentType("application/pdf");
            generationService.generateTeacherScheduleDocument(response.getOutputStream(), DocumentType.PDF, teacherId);
            responseEntity = new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException | IOException exc) {
            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }


    @RequestMapping(value = "/getFullSchedule/pdf", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public ResponseEntity getFullSchedulePDFDocument(HttpServletRequest request, HttpServletResponse response)
            throws ControllerException {
        ResponseEntity responseEntity = null;
        try {
            response.setContentType("application/pdf");
            generationService.generateFullScheduleDocument(response.getOutputStream(), DocumentType.PDF);
            responseEntity = new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException | IOException exc) {
            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }


    @RequestMapping(value = "/getMarks/class/{classId}/subject/{subjectId}/pdf",
            method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public ResponseEntity getTeacherSchedulePDFDocument(HttpServletRequest request, HttpServletResponse response,
                                                        @PathVariable("classId") int classId,
                                                        @PathVariable("subjectId") int subjectId)
            throws ControllerException {
        ResponseEntity responseEntity = null;
        try {
            response.setContentType("application/pdf");
            generationService.generateMarksDocument(response.getOutputStream(), DocumentType.PDF, subjectId, classId);
            responseEntity = new ResponseEntity(HttpStatus.OK);
        } catch (ServiceException | IOException exc) {
            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
}
