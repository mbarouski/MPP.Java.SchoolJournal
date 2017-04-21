package school.journal.service.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import school.journal.entity.LessonTime;
import school.journal.entity.Term;
import school.journal.service.CRUDService;
import school.journal.service.ILessonTimeService;
import school.journal.service.exception.ServiceException;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

@Service("LessonTimeService")
public class LessonTimeService extends CRUDService<LessonTime> implements ILessonTimeService {

    @Override
    public List<LessonTime> getLessonTimeList() throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<LessonTime> lessonTimes = (List<LessonTime>)session.createCriteria(LessonTime.class).addOrder(Order.asc("number")).list();
        transaction.commit();
        session.close();
        return lessonTimes;
    }

    @Override
    public LessonTime update(LessonTime lesson) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        LessonTime l = (LessonTime) session.get(LessonTime.class, lesson.getLessonId());
        if (l == null) throw new ServiceException("LessonTime not found");
        checkTimes(lesson.getStartTime(), lesson.getEndTime());
        checkOverlapping(lesson, session);
        Time startTime = new Time(lesson.getStartTime().getTime() - lesson.getStartTime().getTime() % 60000);
        Time endTime = new Time(lesson.getEndTime().getTime() - lesson.getEndTime().getTime() % 60000);
        l.setStartTime(startTime);
        l.setEndTime(endTime);
        session.update(l);
        transaction.commit();
        session.close();
        return lesson;
    }

    private LessonTime getLessonByNumber(List<LessonTime> lessons, int number) {
        for(LessonTime lesson : lessons) {
            if(lesson.getNumber() == number) return lesson;
        }
        return null;
    }

    private void checkOverlapping(LessonTime lesson, Session session) throws ServiceException {
        List<LessonTime> lessons = (List<LessonTime>) session.createCriteria(LessonTime.class).list();
        try {
            Calendar calendar = Calendar.getInstance();
            if (lesson.getNumber() == 1) {
                LessonTime afterLesson = getLessonByNumber(lessons, 2);
                if (lesson.getEndTime().after(afterLesson.getStartTime())) {
                    throw new ServiceException("Time is overlapped");
                }
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.HOUR_OF_DAY,7);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                if (lesson.getStartTime().before(calendar.getTime())) {
                    throw new ServiceException("Time is early");
                }if (lesson.getEndTime().before(calendar.getTime())) {
                    throw new ServiceException("Time is early");
                }
            } else if (lesson.getNumber() == 10) {
                LessonTime beforeLesson = getLessonByNumber(lessons, 9);
                if (lesson.getStartTime().before(beforeLesson.getEndTime()))
                    throw new ServiceException("Time is overlapped");
                if (lesson.getStartTime().after(Time.valueOf("22:00:00")))
                    throw new ServiceException("Time is early");
                if (lesson.getEndTime().after(Time.valueOf("22:00:00")))
                    throw new ServiceException("Time is early");
            } else {
                int lessonNumber = lesson.getNumber();
                LessonTime afterLesson = getLessonByNumber(lessons, lessonNumber + 1);
                LessonTime beforeLesson = getLessonByNumber(lessons, lessonNumber - 1);
                if (lesson.getEndTime().after(afterLesson.getStartTime()))
                    throw new ServiceException("Time is overlapped");
                if (lesson.getStartTime().before(beforeLesson.getEndTime()))
                    throw new ServiceException("Time is overlapped");
            }
        } catch (ServiceException exc) {
            LOGGER.error(exc);
            throw exc;
        }
    }

    private void checkTimes(Time start, Time end) throws ServiceException {
        if(start.after(end)) throw new ServiceException("Start is after end");
        long diff = end.getTime() - start.getTime();
        if((diff < 2700000) || (diff > 3600000)) throw new ServiceException("Incorrect lesson duration");
    }
}
