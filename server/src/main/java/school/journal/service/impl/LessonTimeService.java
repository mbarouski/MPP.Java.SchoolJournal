package school.journal.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;
import school.journal.entity.LessonTime;
import school.journal.service.CRUDService;
import school.journal.service.ILessonTimeService;
import school.journal.service.exception.ServiceException;

import java.sql.Time;
import java.util.List;

@Service("LessonTimeService")
public class LessonTimeService extends CRUDService<LessonTime> implements ILessonTimeService {

    @Override
    public List<LessonTime> getLessonTimeList() throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<LessonTime> lessonTimes = (List<LessonTime>) session.createCriteria(LessonTime.class).addOrder(Order.asc("number")).list();
        transaction.commit();
        session.close();
        return lessonTimes;
    }


    @Override
    public LessonTime update(LessonTime lesson) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        LessonTime l = (LessonTime) session.get(LessonTime.class, lesson.getLessonId());
        List<LessonTime> lessons = (List<LessonTime>) session.createCriteria(LessonTime.class).list();
        checkLesson(l, lessons);
        removeSecondsFromTime(l);
        session.update(l);
        transaction.commit();
        session.close();
        return lesson;
    }

    private void removeSecondsFromTime(LessonTime lessonTime) {
        Time startTime = new Time(lessonTime.getStartTime().getTime() - lessonTime.getStartTime().getTime() % 60000);
        Time endTime = new Time(lessonTime.getEndTime().getTime() - lessonTime.getEndTime().getTime() % 60000);
        lessonTime.setStartTime(startTime);
        lessonTime.setEndTime(endTime);
    }

    private void checkLesson(LessonTime lessonTime, List<LessonTime> lessons) throws ServiceException {
        checkLessonExists(lessonTime);
        checkLessonStartBeforeEnd(lessonTime.getStartTime(), lessonTime.getEndTime());
        checkLessonLimits(lessonTime.getStartTime(), lessonTime.getEndTime());
        checkOverlapping(lessonTime, lessons);
    }

    private LessonTime getLessonFromListByNumber(List<LessonTime> lessons, int number) throws ServiceException {
        for (LessonTime lesson : lessons) {
            if (lesson.getNumber() == number) return lesson;
        }
        throw new ServiceException("Lesson not found");
    }

    private void checkOverlapping(LessonTime lesson, List<LessonTime> lessons) throws ServiceException {

        try {
            switch (lesson.getNumber()) {
                case 1:
                    checkFirstLesson(lesson, lessons);
                    break;
                case 10:
                    checkLastLesson(lesson, lessons);
                    break;
                default:
                    checkIntermediateLesson(lesson, lessons);
            }
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            throw exc;
        }
    }

    private void checkIntermediateLesson(LessonTime lesson, List<LessonTime> lessons) throws ServiceException {
        int lessonNumber = lesson.getNumber();
        LessonTime lessonAfter = getLessonFromListByNumber(lessons, lessonNumber + 1);
        LessonTime lessonBefore = getLessonFromListByNumber(lessons, lessonNumber - 1);
        if (lesson.getEndTime().after(lessonAfter.getStartTime())) {
            throw new ServiceException("Previous lesson is overlapped");
        }
        if (lesson.getStartTime().before(lessonBefore.getEndTime())) {
            throw new ServiceException("Next lesson is overlapped");
        }
    }

    private void checkFirstLesson(LessonTime lesson, List<LessonTime> lessons) throws ServiceException {
        final String SCHOOL_DAY_START = "07:00:00";
        LessonTime lessonAfter = getLessonFromListByNumber(lessons, 2);
        if (lesson.getStartTime().before(Time.valueOf(SCHOOL_DAY_START))) {
            throw new ServiceException("Lesson is before school day begin");
        }
        if (lesson.getEndTime().after(lessonAfter.getStartTime())) {
            throw new ServiceException("Next lesson is overlapped");
        }
    }

    private void checkLastLesson(LessonTime lesson, List<LessonTime> lessons) throws ServiceException {
        final String SCHOOL_DAY_END = "22:00:00";
        LessonTime lessonBefore = getLessonFromListByNumber(lessons, 9);
        if (lesson.getStartTime().before(lessonBefore.getEndTime()))
            throw new ServiceException("Previous lesson is overlapped");
        if (lesson.getEndTime().after(Time.valueOf(SCHOOL_DAY_END)))
            throw new ServiceException("Lesson is after school day end");
    }

    private void checkLessonLimits(Time start, Time end) throws ServiceException {
        final long FORTY_FIVE_MINUTES = 2700000;
        final long HOUR = 3600000;
        long diff = end.getTime() - start.getTime();
        if ((diff < FORTY_FIVE_MINUTES) || (diff > HOUR)) throw new ServiceException("Incorrect lesson duration");
    }

    private void checkLessonStartBeforeEnd(Time start, Time end) throws ServiceException {
        if (start.after(end)) throw new ServiceException("Start is after end");
    }

    private void checkLessonExists(LessonTime lessonTime) throws ServiceException {
        if (lessonTime == null) throw new ServiceException("LessonTime not found");
    }
}
