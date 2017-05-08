package school.journal.service.document.generation;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import school.journal.entity.*;
import school.journal.entity.enums.DayOfWeekEnum;
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
import static school.journal.service.document.generation.enums.WeekDay.sunday;


@Service("CSVService")
public class CSVService implements IGenerator {

    private static final Logger LOGGER = Logger.getLogger(CSVService.class);

    private static String EMPTY = "";

    private final static DateTimeFormatter DTF_FOR_TIME = DateTimeFormat.forPattern("HH:mm");
    private final static DateTimeFormatter DTF_FOR_DATE = DateTimeFormat.forPattern("MM.dd");
    private final static Date YEAR_MARK_DATE_STUB = new Date(5);
    private final static Date TERM_1_MARK_DATE_STUB = new Date(1);
    private final static Date TERM_2_MARK_DATE_STUB = new Date(2);
    private final static Date TERM_3_MARK_DATE_STUB = new Date(3);
    private final static Date TERM_4_MARK_DATE_STUB = new Date(4);
    private final static String PHONE_NUMBER_FORMAT = "tel:%s";

    private static CSVWriter createSimpleWriter(OutputStream os) {
        final Character SEPARATOR = ';';
        final Character QUOTECHAR = '\"';
        return new CSVWriter(new OutputStreamWriter(os), SEPARATOR, QUOTECHAR);
    }

    private String formatSubjectInSchedule(SubjectInSchedule schedule) {
        final String SCHEDULE_FORMAT = "%1s %2s %3s ";
        return format(SCHEDULE_FORMAT,
                formatClass(schedule.getClazz(), false),
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
        final String teacherFormat = "%1s %2s %3s ";
        return teacher != null
                ? format(teacherFormat, teacher.getLastName(), teacher.getFirstName(), teacher.getPathronymic())
                : EMPTY;
    }

    private String formatClass(Clazz clazz, boolean full) {
        final String classLabel = !full ? EMPTY : "Класс";
        final String classHeaderFormat = "%1s %2d \"%3s\"";
        return format(classHeaderFormat, classLabel, clazz.getNumber(), clazz.getLetterMark());
    }

    private String formatMark(Mark mark) {
        if (mark == null) {
            return EMPTY;
        } else {
            switch (mark.getType()) {
                case simple:
                case term:
                case year:
                    return format("%d", mark.getValue());
                case apsent:
                    return " Н ";
                case control:
                    return format("%d(%s)", mark.getValue(), "К");
                case self:
                    return format("%d(%s)", mark.getValue(), "С");
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
                String.format(PHONE_NUMBER_FORMAT, pupil.getPhoneNumber())};
    }

    private String[] wrapTeacher(Teacher teacher) {
        return teacher == null
                ? new String[0]
                : new String[]{
                teacher.getLastName(),
                teacher.getFirstName(),
                teacher.getPathronymic(),
                String.format(PHONE_NUMBER_FORMAT, teacher.getPhoneNumber())};
    }

    private String[] wrapClass(Clazz clazz) {
        return new String[]{formatClass(clazz, true)};
    }

    private String[] wrapSubject(SubjectInSchedule sis) {
        return new String[]{
                sis.getSubject().getName(),
                formatClass(sis.getClazz(), false),
                formatTime(sis.getBeginTime()),
                sis.getPlace()
        };
    }

    private List<List<String[]>> wrapScheduleDaily(List<SubjectInSchedule> subjectInScheduleList) {
        List<List<String[]>> lists = new LinkedList<>();
        for (WeekDay weekDay :
                WeekDay.values()) {
            if (weekDay != sunday) {
                lists.add(wrapDayScheduleToDay(weekDay, subjectInScheduleList));
            }
        }
        return lists;
    }


    private List<String[]> wrapDayScheduleToDay(WeekDay weekDay, List<SubjectInSchedule> subjectInSchedules) {
        List<SubjectInSchedule> daySubjects = getDaySchedule(weekDay, subjectInSchedules);
        ArrayList<String[]> day = new ArrayList<>();
        day.add(new String[]{weekDay.getValue()});
        day.add(wrapScheduleLegend());
        daySubjects.forEach(subjectInSchedule -> day.add(wrapSubject(subjectInSchedule)));
        return day;
    }

    private String[] wrapScheduleLegend() {
        return new String[]{"Предмет", "Класс", "Время начала занятия", "Место проведения занятия"};
    }

    private List<SubjectInSchedule> getDaySchedule(WeekDay weekDay, List<SubjectInSchedule> subjectInSchedules) {
        ArrayList<SubjectInSchedule> list = new ArrayList<>();
        subjectInSchedules.forEach(subjectInSchedule -> {
            if (weekDay.ordinal() == subjectInSchedule.getDayOfWeek().ordinal()) list.add(subjectInSchedule);
        });
        return list;
    }

    private String[] wrapMarkListHeader(Clazz clazz, Subject subject) {
        final String MARKS_LIST_HEADER_LIST = "Список оценок всех учеников класса ";
        final String MARKS_LIST_HEADER_SUBJECT = "Предмет:";
        return new String[]{MARKS_LIST_HEADER_LIST, formatClass(clazz, true), MARKS_LIST_HEADER_SUBJECT, formatSubject(subject)};
    }

    private Map<Pupil, Map<Date, Mark>> mapMarkDatePupil(List<Pupil> pupils, List<Mark> marks) {
        Map<Pupil, Map<Date, Mark>> map = new HashMap<>();
        for (Pupil pupil : pupils) {
            map.put(pupil, mapMarksDate(filterMarksForPupil(pupil, marks)));
        }
        return map;
    }

    private Map<Date, Mark> mapMarksDate(List<Mark> marks) {
        Map<Date, Mark> map = new HashMap<>();
        for (Mark mark : marks) {
            map.put(mapMarkDate(mark), mark);
        }
        return map;
    }

    private Date mapMarkDate(Mark mark) {
        if (mark.getType() == year || mark.getType() == term) {
            if (mark.getType() == year) return YEAR_MARK_DATE_STUB;
            else switch (mark.getTermNumber()) {
                case 1:
                    return TERM_1_MARK_DATE_STUB;
                case 2:
                    return TERM_2_MARK_DATE_STUB;
                case 3:
                    return TERM_3_MARK_DATE_STUB;
                case 4:
                    return TERM_4_MARK_DATE_STUB;
                default:
                    return new Date(0);
            }
        } else {
            return mark.getDate();
        }
    }

    private List<Mark> filterMarksForPupil(Pupil pupil, List<Mark> marks) {
        List<Mark> pupilMarks = new LinkedList<>();
        for (Mark mark : marks) {
            if (mark.getPupil().equals(pupil)) {
                pupilMarks.add(mark);
            }
        }
        return pupilMarks;
    }

    private Map<WeekDay, Map<Teacher, Map<LessonTime, SubjectInSchedule>>> mapSubjectsLessonTimeTeacherWeekDay(List<SubjectInSchedule> schedule, List<LessonTime> lessons) {
        Map<WeekDay, Map<Teacher, Map<LessonTime, SubjectInSchedule>>> map = new HashMap<>();
        for (WeekDay weekDay : WeekDay.values()) {
            if (weekDay != sunday) {
                map.put(weekDay, mapSubjectsLessonTimeTeacher(filterSubjectsForDay(weekDay, schedule), lessons));
            }
        }
        return map;
    }

    private Map<Teacher, Map<LessonTime, SubjectInSchedule>> mapSubjectsLessonTimeTeacher(List<SubjectInSchedule> schedule, List<LessonTime> lessons) {
        Map<Teacher, Map<LessonTime, SubjectInSchedule>> map = new HashMap<>();
        for (SubjectInSchedule subject : schedule) {
            Teacher teacher = subject.getTeacher();
            if (!map.containsKey(teacher)) {
                map.put(teacher, mapSubjectsLessonTime(filterSubjectsForTeacher(teacher, schedule), lessons));
            }
        }
        return map;
    }

    private Map<LessonTime, SubjectInSchedule> mapSubjectsLessonTime(List<SubjectInSchedule> daySchedule, List<LessonTime> lessons) {
        Map<LessonTime, SubjectInSchedule> map = new HashMap<>();
        for (LessonTime lesson : lessons) {
            SubjectInSchedule subject = getFirstSubjectForLessonTime(lesson, daySchedule);
            if (subject != null) {
                map.put(lesson, subject);
            }
        }
        return map;
    }

    private List<SubjectInSchedule> filterSubjectsForTeacher(Teacher teacher, List<SubjectInSchedule> schedule) {
        List<SubjectInSchedule> teacherSchedule = new LinkedList<>();
        for (SubjectInSchedule subject : schedule) {
            if (teacher.equals(subject.getTeacher())) {
                teacherSchedule.add(subject);
            }
        }
        return teacherSchedule;
    }

    private List<SubjectInSchedule> filterSubjectsForDay(WeekDay weekDay, List<SubjectInSchedule> schedule) {
        List<SubjectInSchedule> daySchedule = new LinkedList<>();
        for (SubjectInSchedule subject : schedule) {
            if (weekDaysEquals(weekDay, subject.getDayOfWeek())) {
                daySchedule.add(subject);
            }
        }
        return daySchedule;
    }

    private SubjectInSchedule getFirstSubjectForLessonTime(LessonTime lesson, List<SubjectInSchedule> schedule) {
        SubjectInSchedule lessonSubject = null;
        for (SubjectInSchedule subject : schedule) {
            if (subject.getBeginTime().equals(lesson.getStartTime())) {
                lessonSubject = subject;
                break;
            }
        }
        return lessonSubject;
    }

    private boolean weekDaysEquals(WeekDay weekDay, DayOfWeekEnum dayOfWeek) {
        return weekDay.ordinal() == dayOfWeek.ordinal();
    }

    private List<String[]> getMarkTable(List<Pupil> pupils, List<Mark> marks, List<Date> workDates) {
        Map<Pupil, Map<Date, Mark>> map = mapMarkDatePupil(pupils, marks);
        List<String[]> tableBody = new LinkedList<>();
        int pupilNameOffset = 1;
        tableBody.add(wrapWorkDates(pupilNameOffset, workDates));

        for (Pupil pupil : sortSet(pupils, getPupilComparator())) {
            tableBody.add(wrapPupilMarks(pupil, map.get(pupil), workDates));
        }
        return tableBody;
    }

    private String[] wrapWorkDates(int pupilNameOffset, List<Date> dates) {
        final String[] specialMarkDates = new String[]{"I", "II", "III", "IV", "Годовая"};
        List<String> row = new LinkedList<>();
        for (int i = 0; i < pupilNameOffset; i++) row.add(EMPTY);
        dates.forEach(date -> row.add(formatDate(date)));
        Collections.addAll(row, specialMarkDates);
        String[] markDates = new String[row.size()];
        return row.toArray(markDates);
    }

    private String[] wrapPupilMarks(Pupil pupil, Map<Date, Mark> map, List<Date> workDates) {
        List<String> row = new LinkedList<>();
        row.add(formatPupil(pupil));
        for (Date date : workDates) {
            row.add(formatMark(map.get(date)));
        }
        row.add(formatMark(map.get(TERM_1_MARK_DATE_STUB)));
        row.add(formatMark(map.get(TERM_2_MARK_DATE_STUB)));
        row.add(formatMark(map.get(TERM_3_MARK_DATE_STUB)));
        row.add(formatMark(map.get(TERM_4_MARK_DATE_STUB)));
        row.add(formatMark(map.get(YEAR_MARK_DATE_STUB)));

        String[] pupilMarks = new String[row.size()];
        return row.toArray(pupilMarks);
    }

    private Comparator<Pupil> getPupilComparator() {
        return Comparator.comparing(Pupil::getLastName).thenComparing(Comparator.comparing(Pupil::getFirstName)).thenComparing(Comparator.comparing(Pupil::getPathronymic));
    }

    private List<String[]> getFullScheduleTable(List<SubjectInSchedule> schedule, List<LessonTime> lessons) {
        List<String[]> table = new LinkedList<>();
        Map<WeekDay, Map<Teacher, Map<LessonTime, SubjectInSchedule>>> map = mapSubjectsLessonTimeTeacherWeekDay(schedule, lessons);
        for (WeekDay weekDay : sortSet(map.keySet(), getWeekDayComparator())) {
            table.addAll(wrapFullDaySchedule(weekDay, map.get(weekDay), lessons));
        }
        return table;
    }

    private List<String[]> wrapFullDaySchedule(WeekDay weekDay, Map<Teacher, Map<LessonTime, SubjectInSchedule>> map, List<LessonTime> lessons) {
        int teacherNameOffset = 1;
        List<String[]> table = new LinkedList<>();
        table.add(wrapDayRow(weekDay));
        table.add(wrapLessonTimes(teacherNameOffset, lessons));
        for (Teacher teacher : sortSet(map.keySet(), getTeacherComparator())) {
            table.add(wrapTeacherRowSchedule(teacher, map.get(teacher), lessons));
        }
        return table;
    }

    private <T> SortedSet<T> sortSet(Collection<T> collection, Comparator<T> comparator) {
        SortedSet<T> result = new TreeSet<>(comparator);
        result.addAll(collection);
        return result;
    }

    private String[] wrapTeacherRowSchedule(Teacher teacher, Map<LessonTime, SubjectInSchedule> map, List<LessonTime> allLessons) {
        List<String> row = new LinkedList<>();
        row.add(formatTeacher(teacher));
        for (LessonTime lesson : allLessons) {
            row.add(wrapLessonSchedule(map.get(lesson)));
        }
        String[] teacherDaySchedule = new String[row.size()];
        return row.toArray(teacherDaySchedule);
    }

    private String wrapLessonSchedule(SubjectInSchedule subject) {
        return subject != null ? formatSubjectInSchedule(subject) : EMPTY;
    }

    private Comparator<WeekDay> getWeekDayComparator() {
        return Comparator.comparingInt(Enum::ordinal);
    }

    private String[] wrapLessonTimes(int teacherNameOffset, List<LessonTime> lessons) {
        String[] result = new String[teacherNameOffset + lessons.size()];
        for (int i = teacherNameOffset; i < result.length; i++) {
            result[i] = formatLesson(lessons.get(i - teacherNameOffset));
        }
        return result;
    }

    private String formatLesson(LessonTime lessonTime) {
        String LESSON_TIME_FORMAT = "%s - %s";
        return String.format(LESSON_TIME_FORMAT, formatTime(lessonTime.getStartTime()), formatTime(lessonTime.getEndTime()));
    }

    private String[] wrapDayRow(WeekDay weekDay) {
        return new String[]{weekDay.getValue()};
    }


    private Comparator<Teacher> getTeacherComparator() {
        return Comparator.comparing(Teacher::getLastName).thenComparing(Comparator.comparing(Teacher::getFirstName)).thenComparing(Comparator.comparing(Teacher::getPathronymic));
    }

    @Override
    public OutputStream generateClassPupilListDocument(OutputStream os, Teacher teacher, Clazz clazz, List<Pupil> pupilList) throws ServiceException {
        CSVWriter writer = createSimpleWriter(os);

        final String pupilClassHeader = "Полный список учеников класса";
        final String formTeacherHeader = "Классный руководитель:";
        final String firstName = "Имя";
        final String lastName = "Фамилия";
        final String patronymic = "Отчество";
        final String phone = "Телефон";
        writer.writeNext(new String[]{pupilClassHeader});
        writer.writeNext(new String[]{formTeacherHeader});
        writer.writeNext(wrapTeacher(teacher));
        writer.writeNext(new String[]{lastName, firstName, patronymic, phone});
        writer.writeNext(wrapClass(clazz));
        pupilList.forEach(pupil -> writer.writeNext(wrapPupil(pupil)));

        return os;
    }

    @Override
    public OutputStream generateTeacherScheduleDocument(OutputStream os, Teacher teacher, List<SubjectInSchedule> subjectInScheduleList, List<LessonTime> lessonTimeList) throws ServiceException {
        CSVWriter writer = createSimpleWriter(os);

        String teacherScheduleHeader = "Расписание преподавателя";
        writer.writeNext(new String[]{teacherScheduleHeader});
        writer.writeNext(wrapTeacher(teacher));

        wrapScheduleDaily(subjectInScheduleList).forEach(writer::writeAll);

        return os;
    }

    @Override
    public OutputStream generateClassScheduleDocument(OutputStream os, Clazz clazz, List<SubjectInSchedule> subjectInScheduleList, List<LessonTime> lessonTimeList) throws ServiceException {
        CSVWriter writer = createSimpleWriter(os);

        String classScheduleHeader = "Расписание для класса:";
        writer.writeNext(new String[]{classScheduleHeader});
        writer.writeNext(wrapClass(clazz));

        wrapScheduleDaily(subjectInScheduleList).forEach(writer::writeAll);

        return os;
    }


    @Override
    public OutputStream generateFullScheduleDocument(OutputStream os, List<SubjectInSchedule> subjectInScheduleList, List<LessonTime> lessonTimeList) throws ServiceException {
        CSVWriter writer = createSimpleWriter(os);
        String fullScheduleHeader = "Полное расписание:";
        writer.writeNext(new String[]{fullScheduleHeader});

        writer.writeAll(getFullScheduleTable(subjectInScheduleList, lessonTimeList));

        return os;
    }

    @Override
    public OutputStream generateMarksDocument(OutputStream os, Subject subject, List<Mark> markList, List<Pupil> pupilList, Clazz clazz, List<Date> lessonDateList) throws ServiceException {
        CSVWriter writer = createSimpleWriter(os);

        writer.writeNext(wrapMarkListHeader(clazz, subject));
        writer.writeAll(getMarkTable(pupilList, markList, lessonDateList));

        return os;
    }
}
