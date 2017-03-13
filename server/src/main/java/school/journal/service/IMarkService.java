package school.journal.service;

import school.journal.entity.Mark;
import school.journal.service.exception.ServiceException;

import java.util.Date;
import java.util.List;

public interface IMarkService extends IService<Mark>{

    Mark getOne(int id) throws ServiceException ;
    List<Mark> getMarksForSubjectInClass(int subjectId,int classId) throws ServiceException;
    List<Mark> getMarksForTermOrder(int classId, Date startTerm,Date endTerm) throws ServiceException;
    List<Mark> getMarksForPupil(int pupilId,Date startTerm,Date endTerm) throws ServiceException;
}
