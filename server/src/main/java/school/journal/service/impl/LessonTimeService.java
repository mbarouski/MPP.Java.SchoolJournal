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
        l.setStartTime(lesson.getStartTime());
        l.setEndTime(lesson.getEndTime());
        session.update(l);
        transaction.commit();
        session.close();
        return lesson;
    }
}
