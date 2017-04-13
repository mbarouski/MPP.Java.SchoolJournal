package school.journal.service;

import school.journal.entity.LessonTime;
import school.journal.service.exception.ServiceException;

import java.util.List;

public interface ILessonTimeService {
    List<LessonTime> getLessonTimeList() throws ServiceException;
    LessonTime update(LessonTime lesson) throws ServiceException;
}
