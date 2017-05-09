package school.journal.service.document.generation;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import school.journal.entity.*;
import school.journal.entity.enums.DayOfWeekEnum;
import school.journal.service.exception.ServiceException;
import school.journal.utils.MD5Generator;

import java.io.*;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.List;

@Service("PDFGenerator")
public class PDFGenerator implements IGenerator {
    private static final Logger LOGGER = Logger.getLogger(PDFGenerator.class);
    private static final String PATH_TO_FONT = "c:/Windows/Fonts/arial.ttf";

    @Override
    public OutputStream generateClassPupilListDocument(OutputStream os, Teacher teacher,
                                                       Clazz clazz, List<Pupil> pupilList) throws ServiceException {
        try {
            Font font = createFont();
            Document doc = createDocument(os);
            doc.open();
            doc.add(generateParagraph(font,
                    MessageFormat.format("Класс: {0} \"{1}\"", clazz.getNumber(), clazz.getLetterMark())));
            doc.add(generateParagraph(font,
                    MessageFormat.format("Классный руководитель: {0}", teacher.getFIO())));
            addTableWithPupils(doc, font, pupilList);
            addWatermarkToDocument(doc);
            doc.close();
        } catch (Exception exc) {
            LOGGER.error(exc);
            throw new ServiceException("Can't build document");
        }
        return os;
    }

    @Override
    public OutputStream generateTeacherScheduleDocument(OutputStream os, Teacher teacher, List<SubjectInSchedule> subjectInScheduleList, List<LessonTime> lessonTimeList) throws ServiceException {
        return null;
    }

    @Override
    public OutputStream generateClassScheduleDocument(OutputStream os, Clazz clazz, List<SubjectInSchedule> subjectInScheduleList, List<LessonTime> lessonTimeList) throws ServiceException {
        Document doc = new Document();
        try {
            BaseFont baseFont = BaseFont.createFont(PATH_TO_FONT, "CP1251", BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 10, Font.NORMAL);
            PdfWriter writer = PdfWriter.getInstance(doc, os);
            doc.open();
            doc.add(generateParagraph(font, "Расписание уроков для класса"));
            doc.add(generateParagraph(font,
                    MessageFormat.format("Класс: {0} \"{1}\"", clazz.getNumber(), clazz.getLetterMark())));
            addClassScheduleToDocument(doc, font, subjectInScheduleList, lessonTimeList);
            doc.close();
        } catch (Exception exc) {
            LOGGER.error(exc);
            throw new ServiceException("Can't build document");
        }
        return os;
    }

    @Override
    public OutputStream generateFullScheduleDocument(OutputStream os, List<SubjectInSchedule> subjectInScheduleList, List<LessonTime> lessonTimeList) throws ServiceException {
        return null;
    }

    @Override
    public OutputStream generateMarksDocument(OutputStream os, Subject subject, List<Mark> markList, List<Pupil> pupilList, Clazz clazz, List<Date> lessonDateList) throws ServiceException {
        return null;
    }

    private Paragraph generateParagraph(Font font, String text) throws DocumentException {
        Paragraph paragraph = new Paragraph(text, font);
        return paragraph;
    }

    private Phrase generatePhrase(Font font, String text) throws DocumentException {
        return new Phrase(text, font);
    }

    private void addTableWithPupils(Document doc, Font font, List<Pupil> pupilList) throws DocumentException {
        PdfPTable table = new PdfPTable(3);

        float[] columnWidths = {1f, 4f, 4f};
        table.setWidths(columnWidths);
        table.setSpacingBefore(4f);

        for(int i = 0; i < pupilList.size(); i++) {
            PdfPCell numberCell = new PdfPCell();
            PdfPCell fioCell = new PdfPCell();
            PdfPCell phoneNumberCell = new PdfPCell();

            numberCell.addElement(generateParagraph(font, i + 1 + ""));
            fioCell.addElement(generateParagraph(font, pupilList.get(i).getFIO()));
            phoneNumberCell.addElement(generateParagraph(font, pupilList.get(i).getPhoneNumber()));

            table.addCell(numberCell);
            table.addCell(fioCell);
            table.addCell(phoneNumberCell);
        }

        doc.add(table);
    }

    private void addWatermarkToDocument(Document doc) throws DocumentException {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            InputStream is = new FileInputStream(new File(classLoader.getResource("images/watermark.png").getFile()));
            byte[] data = new byte[is.available()];
            is.read(data);
            Image img = Image.getInstance(data);
            img.setAbsolutePosition(0, 0);
            doc.add(img);
        } catch (IOException exc) {}
    }

    private Document createDocument(OutputStream os) throws DocumentException {
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, os);
//        writer.setEncryption("123".getBytes(), "321".getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
        return doc;
    }

    private Font createFont() throws Exception {
        BaseFont baseFont = BaseFont.createFont(PATH_TO_FONT, "CP1251", BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 10, Font.NORMAL);
        return font;
    }

    private void addClassScheduleToDocument(Document doc, Font font, List<SubjectInSchedule> subjectInScheduleList,
                                            List<LessonTime> lessonTimeList) throws DocumentException {
        DayOfWeekEnum[] days = DayOfWeekEnum.values();
        for(int dayIndex = 0; dayIndex < days.length; dayIndex++) {
            DayOfWeekEnum day = days[dayIndex];
            doc.add(generateParagraph(font, day.name()));
        }
    }
}
