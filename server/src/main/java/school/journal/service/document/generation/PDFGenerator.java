package school.journal.service.document.generation;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import school.journal.entity.*;
import school.journal.service.exception.ServiceException;
import school.journal.utils.MD5Generator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.util.List;

@Service("PDFGenerator")
public class PDFGenerator implements IGenerator {
    @Override
    public OutputStream generateClassPupilListDocument(OutputStream os, Teacher teacher, Clazz clazz, List<Pupil> pupilList) throws ServiceException {
        String tempFileName = MD5Generator.generate(((new java.util.Date()).getTime()) + "") + ".pdf";
        PdfDocument pdfDocument = new PdfDocument();
        try {
            PdfWriter writer = PdfWriter.getInstance(pdfDocument, new FileOutputStream(tempFileName));
            pdfDocument.open();
        } catch (DocumentException | IOException exc) {

        }
        return null;
    }

    @Override
    public OutputStream generateTeacherScheduleDocument(OutputStream os, Teacher teacher, List<SubjectInSchedule> subjectInScheduleList, List<LessonTime> lessonTimeList) throws ServiceException {
        return null;
    }

    @Override
    public OutputStream generateClassScheduleDocument(OutputStream os, Clazz clazz, List<SubjectInSchedule> subjectInScheduleList, List<LessonTime> lessonTimeList) throws ServiceException {
        return null;
    }

    @Override
    public OutputStream generateFullScheduleDocument(OutputStream os, List<SubjectInSchedule> subjectInScheduleList, List<LessonTime> lessonTimeList) throws ServiceException {
        return null;
    }

    @Override
    public OutputStream generateMarksDocument(OutputStream os, Subject subject, List<Mark> markList, List<Pupil> pupilList, Clazz clazz, List<Date> lessonDateList) throws ServiceException {
        return null;
    }
}
