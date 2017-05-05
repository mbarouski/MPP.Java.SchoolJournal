package school.journal.service.document.generation;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import school.journal.entity.*;
import school.journal.entity.enums.DayOfWeekEnum;
import school.journal.service.*;
import school.journal.service.exception.ServiceException;
import school.journal.service.impl.ClassService;
import school.journal.service.impl.MarkService;

import java.io.OutputStream;
import java.sql.Date;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("GenerationService")
public class GenerationService implements IGenerationService{
    private HashMap<DocumentType, IGenerator> GENERATOR_MAP = new HashMap<>();

    @Qualifier("PDFGenerator")
    @Autowired
    private IGenerator PDF_GENERATOR;

    {
        GENERATOR_MAP.put(DocumentType.CSV, null);
        GENERATOR_MAP.put(DocumentType.PDF, PDF_GENERATOR);
        GENERATOR_MAP.put(DocumentType.XLSX, null);
    }

    @Autowired
    @Qualifier("PupilService")
    private IPupilService pupilService;

    @Autowired
    @Qualifier("TeacherService")
    private ITeacherService teacherService;

    @Autowired
    @Qualifier("SubjectService")
    private ISubjectService subjectService;

    @Autowired
    @Qualifier("MarkService")
    private IMarkService markService;

    @Autowired
    @Qualifier("ClassService")
    private IClassService classService;

    @Autowired
    @Qualifier("TermService")
    private ITermService termService;

    @Autowired
    @Qualifier("LessonTimeService")
    private ILessonTimeService lessonTimeService;

    @Autowired
    @Qualifier("SubjectInScheduleService")
    private ISubjectInScheduleService subjectInScheduleService;

    @Override
    public OutputStream generateClassPupilListDocument(OutputStream os, DocumentType documentType, int classId) throws ServiceException {
        Clazz clazz = classService.getOne(classId);
        List<Pupil> pupilList = pupilService.getListOfPupils(classId);
        List<Teacher> teacherList = teacherService.getListOfTeachersForClass(classId);
        Teacher teacher = null;
        for (Teacher t : teacherList) {
            if(t.getClassId() == classId){
                teacher = t;
            }
        }
        IGenerator generator = GENERATOR_MAP.get(documentType);
        return generator.generateClassPupilListDocument(os, teacher, clazz, pupilList);
    }

    @Override
    public OutputStream generateTeacherScheduleDocument(OutputStream os, DocumentType documentType, int teacherId)
            throws ServiceException {
        Teacher teacher = teacherService.getOne(teacherId);
        List<SubjectInSchedule> subjectInScheduleList = subjectInScheduleService.getTeacherSchedule(teacherId);
        List<LessonTime> lessonTimeList = lessonTimeService.getLessonTimeList();
        IGenerator generator = GENERATOR_MAP.get(documentType);
        return generator.generateTeacherScheduleDocument(os, teacher, subjectInScheduleList, lessonTimeList);
    }

    @Override
    public OutputStream generateClassScheduleDocument(OutputStream os, DocumentType documentType, int classId) throws ServiceException {
        Clazz clazz = classService.getOne(classId);
        List<SubjectInSchedule> subjectInScheduleList = subjectInScheduleService.getPupilSchedule(classId);
        List<LessonTime> lessonTimeList = lessonTimeService.getLessonTimeList();
        IGenerator generator = GENERATOR_MAP.get(documentType);
        return generator.generateClassScheduleDocument(os, clazz, subjectInScheduleList, lessonTimeList);
    }

    @Override
    public OutputStream generateFullScheduleDocument(OutputStream os, DocumentType documentType) throws ServiceException {
        List<SubjectInSchedule> subjectInScheduleList = subjectInScheduleService.read();
        List<LessonTime> lessonTimeList = lessonTimeService.getLessonTimeList();
        IGenerator generator = GENERATOR_MAP.get(documentType);
        return generator.generateFullScheduleDocument(os, subjectInScheduleList, lessonTimeList);
    }

    @Override
    public OutputStream generateMarksDocument(OutputStream os, DocumentType documentType, int subjectId, int classId)
            throws ServiceException {
        Clazz clazz = classService.getOne(classId);
        Subject subject = subjectService.getOne(subjectId);
        List<Mark> markList = markService.getMarksForSubjectInClass(subjectId,
                classId, termService.getCurrentTerm().getNumber());
        List<Pupil> pupilList = pupilService.getListOfPupils(classId);
        List<LessonTime> lessonTimeList = lessonTimeService.getLessonTimeList();
        List<SubjectInSchedule> subjectInScheduleList = getSubjectInScheduleForSubjectAndClass(subjectId, classId);
        IGenerator generator = GENERATOR_MAP.get(documentType);
        return generator.generateMarksDocument(os, subject, markList, pupilList, clazz, getLessonDateList(subjectInScheduleList));
    }

    private List<SubjectInSchedule> getSubjectInScheduleForSubjectAndClass(int subjectId, int classId)
            throws ServiceException {
        List<SubjectInSchedule> result = new ArrayList<>();
        List<SubjectInSchedule> wholeList = subjectInScheduleService.read();
        for (SubjectInSchedule subject : wholeList) {
            if(subject.getClazz().getClassId() == classId && subject.getSubject().getSubjectId() == subjectId) {
                result.add(subject);
            }
        }
        return result;
    }

    private List<Date> getLessonDateList(List<SubjectInSchedule> subjectInScheduleList) throws ServiceException {
        Term currentTerm = termService.getCurrentTerm();
        List<DayOfWeekEnum> lessonDays = new ArrayList<>();
        for (SubjectInSchedule subject : subjectInScheduleList) {
            if(!lessonDays.contains(subject.getDayOfWeek())) {
                lessonDays.add(subject.getDayOfWeek());
            }
        }
        List<Date> days = new ArrayList<>();
        List<DateTime> dates = getDateRange(currentTerm.getStart(), currentTerm.getEnd());
        for (DateTime dateTime : dates) {
            if(contains(lessonDays, dateTime)) {
                days.add(new Date(dateTime.getMillis()));
            }
        }
        return days;
    }

    private boolean contains(List<DayOfWeekEnum> daysOfWeek, DateTime dateTime) {
        int dayNumber = dateTime.getDayOfWeek();
        for (DayOfWeekEnum day : daysOfWeek) {
            if(day.getValue() == dayNumber) return true;
        }
        return false;
    }

    private static List<DateTime> getDateRange(Date start, Date end) {
        List<DateTime> result = new ArrayList<DateTime>();
        DateTime jodaEnd = new DateTime(end.getTime());
        DateTime tmp = new DateTime(start.getTime());
        while(tmp.isBefore(jodaEnd) || tmp.equals(jodaEnd)) {
            result.add(tmp);
            tmp = tmp.plusDays(1);
        }
        return result;
    }
}
