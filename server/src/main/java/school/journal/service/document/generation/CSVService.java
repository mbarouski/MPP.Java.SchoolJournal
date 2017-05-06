package school.journal.service.document.generation;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import school.journal.entity.*;
import school.journal.service.document.generation.enums.WeekDay;
import school.journal.service.exception.ServiceException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

import static java.lang.String.format;
import static school.journal.entity.enums.MarkType.term;
import static school.journal.entity.enums.MarkType.year;


@Service("CSVService")
public class CSVService implements IGenerator {

    private static final Logger LOGGER = Logger.getLogger(CSVService.class);

    private static String EMPTY = "";

    private final static String CLASS_SCHEDULE_HEADER = "Расписание для класса:";
    private static final String TEACHER_SCHEDULE_HEADER = "Расписание преподавателя";
    private final static String FULL_SCHEDULE_HEADER = "Полное расписание:";
    private final static String FORM_TEACHER_HEADER = "Классный руководитель:";
    private static int WORK_DAY_COUNT = 6;
    private final static DateTimeFormatter DTF_FOR_TIME = DateTimeFormat.forPattern("HH:mm");
    private final static DateTimeFormatter DTF_FOR_DATE = DateTimeFormat.forPattern("MM:dd");

    private static CSVWriter createSimpleWriter(OutputStream os) {
        final Character SEPARATOR = ';';
        final Character QUOTECHAR = '\"';
        return new CSVWriter(new OutputStreamWriter(os), SEPARATOR, QUOTECHAR);
    }

    private String formatSubjectInSchedule(SubjectInSchedule schedule) {
        final String SCHEDULE_FORMAT = "%1s %2s %3s ";
        return format(SCHEDULE_FORMAT,
                formatClass(schedule.getClazz()),
                formatSubject(schedule.getSubject()),
                schedule.getPlace());
    }

    private String formatSubject(Subject subject) {
        final String SUBJECT_FORMAT = "%1s";
        return format(SUBJECT_FORMAT, subject.getName());
    }

    private String formatPupil(Pupil pupil) {
        final String PUPIL_FORMAT = "%1s %2s %3s ";
        return pupil != null
                ? format(PUPIL_FORMAT, pupil.getLastName(), pupil.getFirstName(), pupil.getPathronymic())
                : EMPTY;
    }

    private String formatTeacher(Teacher teacher) {
        final String TEACHER_FORMAT = "%1s %2s %3s ";
        return teacher != null
                ? format(TEACHER_FORMAT, teacher.getLastName(), teacher.getFirstName(), teacher.getPathronymic())
                : EMPTY;
    }

    private String formatClass(Clazz clazz) {
        final String CLASS_HEADER_FORMAT = "Класс %1d \"%2s\"";
        return format(CLASS_HEADER_FORMAT, clazz.getNumber(), clazz.getLetterMark());
    }

    private String formatMark(Mark mark) {
        if (mark == null) {
            return EMPTY;
        } else {
            switch (mark.getType()) {
                case simple:
                    return format("%d", mark.getValue());
                case apsent:
                    return " Н ";
                case control:
                    return format("%d(%s)", mark.getValue(), "К");
                case self:
                    return format("%d(%s)", mark.getValue(), "С");
                case term:
                    return format("%d(%s)", mark.getValue(), "Ч");
                case year:
                    return format("%d(%s)", mark.getValue(), "Г");
                default:
                    return EMPTY;
            }
        }
    }

    private String formatTime(Time time) {
        return DTF_FOR_TIME.print(time.getTime());
    }

    private String formatDate(Date date) {
        return DTF_FOR_DATE.print(date.getTime());
    }

    private String[] wrapPupil(Pupil pupil) {
        return new String[]{
                pupil.getLastName(),
                pupil.getFirstName(),
                pupil.getPathronymic(),
                pupil.getPhoneNumber()};
    }

    private String[] wrapTeacher(Teacher teacher) {
        return teacher == null
                ? new String[0]
                : new String[]{
                teacher.getLastName(),
                teacher.getFirstName(),
                teacher.getPathronymic(),
                teacher.getPhoneNumber()
        };
    }

    private String[] wrapClass(Clazz clazz) {
        return new String[]{formatClass(clazz)};
    }

    private String[] wrapSubject(SubjectInSchedule sis) {
        return new String[]{
                sis.getSubject().getName(),
                formatClass(sis.getClazz()),
                formatTime(sis.getBeginTime()),
                sis.getPlace()
        };
    }

    private String[] wrapLessonDates(List<Date> lessonDates) {
        String[] wrappedDates = new String[lessonDates.size()];
        for (int i = 0; i < wrappedDates.length; i++) {
            wrappedDates[i] = formatDate(lessonDates.get(i));
        }
        return wrappedDates;
    }

    private List<List<String[]>> wrapScheduleDaily(List<SubjectInSchedule> subjectInScheduleList) {
        List<List<String[]>> lists = new LinkedList<>();
        for (WeekDay weekDay :
                WeekDay.values()) {
            if (weekDay != WeekDay.sunday) {
                lists.add(wrapDayScheduleToDay(weekDay, subjectInScheduleList));
            }
        }
        return lists;
    }

    private List<String[]> wrapDayScheduleToDay(WeekDay weekDay, List<SubjectInSchedule> subjectInSchedules) {
        List<SubjectInSchedule> daySubjects = getDaySchedule(weekDay, subjectInSchedules);
        ArrayList<String[]> day = new ArrayList<>();
        day.add(new String[]{weekDay.getValue()});
        daySubjects.forEach(subjectInSchedule -> day.add(wrapSubject(subjectInSchedule)));
        return day;
    }

    private String[] wrapPupilMarks(Pupil pupil, List<Mark> marks, List<Date> dates) {
        int cellsForPupil = 1;
        int daysCount = marks.size();
        int specialMarkCount = 5;
        int rowCellsTotal = cellsForPupil + daysCount + specialMarkCount;
        String[] pupilRow = new String[rowCellsTotal];
        pupilRow[0] = formatPupil(pupil);
        int indexInRow = 1;
        int indexInMarks = 0;
        for (Date date : dates) {
            Mark mark = marks.get(indexInMarks);
            if (mark.getDate() != null) {
                if (mark.getDate().equals(date)) {
                    pupilRow[indexInRow++] = formatMark(mark);
                    indexInMarks++;
                } else if (mark.getDate().after(date)) {
                    pupilRow[indexInRow++] = EMPTY;
                } else {
                    indexInMarks++;
                    if (mark.getType() == year) {
                        pupilRow[daysCount + specialMarkCount] = formatMark(mark);
                    } else if (mark.getType() == term) {
                        pupilRow[daysCount + mark.getTermNumber() - 1] = formatMark(mark);
                    }
                }
            } else {
                if (mark.getType() == year) {
                    pupilRow[daysCount + specialMarkCount] = formatMark(mark);
                } else if (mark.getType() == term) {
                    pupilRow[daysCount + mark.getTermNumber() - 1] = formatMark(mark);
                }
                indexInMarks++;
            }
        }
        return pupilRow;
    }

    private List<SubjectInSchedule> getDaySchedule(WeekDay weekDay, List<SubjectInSchedule> subjectInSchedules) {
        ArrayList<SubjectInSchedule> list = new ArrayList<>();
        subjectInSchedules.forEach(subjectInSchedule -> {
            if (weekDay.ordinal() == subjectInSchedule.getDayOfWeek().ordinal()) list.add(subjectInSchedule);
        });
        return list;
    }

    private String[] wrapFullScheduleDayName(WeekDay weekDay, int count) {
        String[] day = new String[count];
        final int DAY_LABEL = 0;
        for (int i = 0; i < count; i++) {
            day[i] = i == DAY_LABEL ? weekDay.getValue() : EMPTY;
        }
        return day;
    }

    private String[] wrapFullScheduleDayList(int lesson_in_day_count) {
        String[] dayTimes = new String[lesson_in_day_count * WORK_DAY_COUNT];
        for (WeekDay weekDay : WeekDay.values()) {
            if (weekDay != WeekDay.sunday) {
                System.arraycopy(wrapFullScheduleDayName(weekDay, lesson_in_day_count), 0, dayTimes, (weekDay.ordinal() - 1) * lesson_in_day_count, lesson_in_day_count);
            }
        }
        return dayTimes;
    }

    private String[] wrapFullScheduleDayLessonTimes(List<LessonTime> lessonTimeList) {
        String[] lesson = new String[lessonTimeList.size()];
        for (int i = 0; i < lesson.length; i++) {
            lesson[i] = formatTime(lessonTimeList.get(i).getStartTime());
        }
        return lesson;

    }

    private String[] wrapFullScheduleAllDayLessonTimes(List<LessonTime> lessonTimeList) {
        final int lesson_in_day_count = lessonTimeList.size();
        String[] allLessonTimes = new String[lesson_in_day_count * WORK_DAY_COUNT];
        for (WeekDay weekDay : WeekDay.values()) {
            if (weekDay != WeekDay.sunday) {
                System.arraycopy(wrapFullScheduleDayLessonTimes(lessonTimeList), 0, allLessonTimes, getActualDayIndex(weekDay) * lesson_in_day_count, lesson_in_day_count);
            }
        }
        return allLessonTimes;
    }

    private String[] wrapMarkListHeader(Clazz clazz, Subject subject) {
        final String MARKS_LIST_HEADER_LIST = "Список оценок всех учеников класса ";
        final String MARKS_LIST_HEADER_SUBJECT = "Предмет:";
        return new String[]{MARKS_LIST_HEADER_LIST, formatClass(clazz), MARKS_LIST_HEADER_SUBJECT, formatSubject(subject)};
    }

    private int getActualDayIndex(WeekDay weekDay) {
        return weekDay.ordinal() - 1;
    }

    private void tryAddToTeacherSchedule(List<SubjectInSchedule> teacherSchedule, Teacher teacher, SubjectInSchedule subjectInSchedule) {
        if (subjectInSchedule.getTeacher().getUserId().intValue() == teacher.getUserId().intValue()) {
            teacherSchedule.add(subjectInSchedule);
        }
    }

    private Map<Pupil, List<Mark>> organizeMarksForPupil(List<Pupil> pupils, List<Mark> marks) {
        Map<Pupil, List<Mark>> pupilMarkMap = new HashMap<>();
        pupils.forEach(pupil -> pupilMarkMap.put(pupil, getMarksForPupil(pupil, marks)));
        return pupilMarkMap;
    }

    private List<Mark> getMarksForPupil(Pupil pupil, List<Mark> marks) {
        List<Mark> markList = new ArrayList<>();
        marks.forEach(mark -> {
            if (mark.getPupil().equals(pupil)) {
                markList.add(mark);
            }
        });
        return markList;
    }

    private Map<Teacher, List<SubjectInSchedule>> organizeScheduleByTeacher(List<SubjectInSchedule> schedule) {
        Map<Teacher, List<SubjectInSchedule>> mappedSchedule = new HashMap<>();
        for (SubjectInSchedule subject : schedule) {
            Teacher teacher = subject.getTeacher();
            if (!mappedSchedule.containsKey(teacher)) {
                mappedSchedule.put(teacher, filterTeacherSchedule(teacher, schedule));
            }
        }
        return mappedSchedule;
    }

    private String fetchSubjectWithLesson(LessonTime lesson, List<SubjectInSchedule> subjects) {
        for (SubjectInSchedule subject : subjects) {
            if (subject.getBeginTime().equals(lesson.getStartTime())) {
                return formatSubjectInSchedule(subject);
            }
        }
        return EMPTY;
    }

    private String[] wrapTeacherSchedule(Teacher teacher, List<SubjectInSchedule> teachersSubjects, List<LessonTime> lessonList) {
        int cellsForTeacher = 1;
        int lessonsInDay = lessonList.size();
        int rowCellsTotal = cellsForTeacher + lessonsInDay * WORK_DAY_COUNT;
        String[] teacherSchedule = new String[rowCellsTotal];
        teacherSchedule[0] = formatTeacher(teacher);
        for (WeekDay weekDay : WeekDay.values()) {
            if (weekDay != WeekDay.sunday) {
                int destinationPosition = cellsForTeacher + getActualDayIndex(weekDay) * lessonsInDay;
                String[] teacherDaySchedule = getTeacherDaySchedule(filterSubjectsByDay(weekDay, teachersSubjects), lessonList);
                System.arraycopy(
                        teacherDaySchedule, 0, teacherSchedule, destinationPosition, lessonsInDay);
            }
        }
        return teacherSchedule;
    }

    private List<SubjectInSchedule> filterSubjectsByDay(WeekDay weekDay, List<SubjectInSchedule> subjectInScheduleList) {
        List<SubjectInSchedule> daySubjects = new LinkedList<>();
        subjectInScheduleList.forEach(subjectInSchedule -> {
            if (weekDay.ordinal() == subjectInSchedule.getDayOfWeek().ordinal()) {
                daySubjects.add(subjectInSchedule);
            }
        });
        return daySubjects;
    }

    private List<SubjectInSchedule> filterTeacherSchedule(Teacher teacher, List<SubjectInSchedule> subjectInScheduleList) {
        List<SubjectInSchedule> teacherSchedule = new LinkedList<>();
        subjectInScheduleList.forEach(subjectInSchedule -> tryAddToTeacherSchedule(teacherSchedule, teacher, subjectInSchedule));
        return teacherSchedule;
    }

    private String[] getTeacherDaySchedule(List<SubjectInSchedule> dailySubjectsOfTeacher, List<LessonTime> lessonSchedule) {
        String[] lessons = new String[lessonSchedule.size()];
        for (int i = 0; i < lessons.length; i++) {
            lessons[i] = fetchSubjectWithLesson(lessonSchedule.get(i), dailySubjectsOfTeacher);
        }
        return lessons;
    }


    private List<String[]> getFullScheduleTableHeader(List<LessonTime> lessonTimeList) {
        final int LESSON_IN_DAY_COUNT = lessonTimeList.size();
        List<String[]> tableHeader = new LinkedList<>();

        tableHeader.add(wrapFullScheduleDayList(LESSON_IN_DAY_COUNT));
        tableHeader.add(wrapFullScheduleAllDayLessonTimes(lessonTimeList));

        return tableHeader;
    }

    private List<String[]> getMarkListBody(List<Pupil> pupils, List<Mark> marks, List<Date> workDates) {
        Map<Pupil, List<Mark>> pupilMarkMap = organizeMarksForPupil(pupils, marks);
        List<String[]> tableBody = new LinkedList<>();
        for (Pupil pupil : pupils) {
            tableBody.add(wrapPupilMarks(pupil, pupilMarkMap.get(pupil), workDates));
        }
        return tableBody;


    }

    private List<String[]> getFullScheduleTableBody(List<SubjectInSchedule> schedule, List<LessonTime> lessons) {
        Map<Teacher, List<SubjectInSchedule>> scheduleMap = organizeScheduleByTeacher(schedule);
        List<String[]> tableBody = new LinkedList<>();
        for (Teacher teacher : scheduleMap.keySet()) {
            tableBody.add(wrapTeacherSchedule(teacher, scheduleMap.get(teacher), lessons));
        }
        return tableBody;
    }

    @Override
    public OutputStream generateClassPupilListDocument(OutputStream os, Teacher teacher, Clazz clazz, List<Pupil> pupilList) throws ServiceException {
        CSVWriter writer = createSimpleWriter(os);

        final String PUPIL_CLASS_HEADER = "Полный список учеников класса";
        writer.writeNext(new String[]{PUPIL_CLASS_HEADER});
        writer.writeNext(wrapTeacher(teacher));

        writer.writeNext(new String[]{FORM_TEACHER_HEADER});
        writer.writeNext(wrapClass(clazz));
        pupilList.forEach(pupil -> writer.writeNext(wrapPupil(pupil)));

        try {
            writer.flush();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return os;
    }

    @Override
    public OutputStream generateTeacherScheduleDocument(OutputStream os, Teacher teacher, List<SubjectInSchedule> subjectInScheduleList, List<LessonTime> lessonTimeList) throws ServiceException {
        CSVWriter writer = createSimpleWriter(os);

        writer.writeNext(new String[]{TEACHER_SCHEDULE_HEADER});
        writer.writeNext(wrapTeacher(teacher));

        wrapScheduleDaily(subjectInScheduleList).forEach(writer::writeAll);

        try {
            writer.flush();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return os;
    }

    @Override
    public OutputStream generateClassScheduleDocument(OutputStream os, Clazz clazz, List<SubjectInSchedule> subjectInScheduleList, List<LessonTime> lessonTimeList) throws ServiceException {
        CSVWriter writer = createSimpleWriter(os);

        writer.writeNext(new String[]{CLASS_SCHEDULE_HEADER});
        writer.writeNext(wrapClass(clazz));

        wrapScheduleDaily(subjectInScheduleList).forEach(writer::writeAll);

        try {
            writer.flush();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return os;
    }

    @Override
    public OutputStream generateFullScheduleDocument(OutputStream os, List<SubjectInSchedule> subjectInScheduleList, List<LessonTime> lessonTimeList) throws ServiceException {
        CSVWriter writer = createSimpleWriter(os);
        writer.writeNext(new String[]{FULL_SCHEDULE_HEADER});

        writer.writeAll(getFullScheduleTableHeader(lessonTimeList));

        writer.writeAll(getFullScheduleTableBody(subjectInScheduleList, lessonTimeList));

        try {
            writer.flush();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return os;
    }

    @Override
    public OutputStream generateMarksDocument(OutputStream os, Subject subject, List<Mark> markList, List<Pupil> pupilList, Clazz clazz, List<Date> lessonDateList) throws ServiceException {
        CSVWriter writer = createSimpleWriter(os);

        writer.writeNext(wrapMarkListHeader(clazz, subject));
        writer.writeNext(wrapLessonDates(lessonDateList));

        writer.writeAll(getMarkListBody(pupilList, markList, lessonDateList));

        try {
            writer.flush();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return os;
    }
}
