package school.journal.service;

import school.journal.entity.Clazz;
import school.journal.entity.Mark;
import school.journal.service.exception.ServiceException;

import java.util.List;

public interface IMarkService {


    List<Mark> getMarks() throws ServiceException;

    Mark createMark(Mark mark) throws ServiceException;

    Mark updateMark(Mark mark) throws ServiceException;

    Mark deleteMark(int markId) throws ServiceException;

    Mark getOne(int markId) throws ServiceException;

}
