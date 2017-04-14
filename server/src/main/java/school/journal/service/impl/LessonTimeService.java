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
        if(l == null) throw new ServiceException("LessonTime not found");
        checkTimes(lesson.getStartTime(), lesson.getEndTime());
        checkOverlapping(lesson, session);
        l.setStartTime(lesson.getStartTime());
        l.setEndTime(lesson.getEndTime());
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
            if (lesson.getNumber() == 1) {
                LessonTime afterLesson = getLessonByNumber(lessons, 2);
                if (lesson.getEndTime().after(afterLesson.getStartTime()))
                    throw new ServiceException("Time is overlapped");
                if (lesson.getStartTime().before(Time.valueOf("07:00:00")))
                    throw new ServiceException("Time is early");
            } else if (lesson.getNumber() == 10) {
                LessonTime beforeLesson = getLessonByNumber(lessons, 9);
                if (lesson.getEndTime().before(beforeLesson.getStartTime()))
                    throw new ServiceException("Time is overlapped");
                if (lesson.getStartTime().after(Time.valueOf("22:00:00")))
                    throw new ServiceException("Time is early");
            } else {
                int lessonNumber = lesson.getNumber();
                LessonTime afterLesson = getLessonByNumber(lessons, lessonNumber + 1);
                LessonTime beforeLesson = getLessonByNumber(lessons, lessonNumber - 1);
                if (lesson.getEndTime().after(afterLesson.getStartTime()))
                    throw new ServiceException("Time is overlapped");
                if (lesson.getStartTime().before(beforeLesson.getStartTime()))
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
