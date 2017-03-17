package school.journal.service;

import school.journal.entity.Pupil;
import school.journal.service.exception.ServiceException;

import java.util.List;

public interface IPupilService extends IService<Pupil>{

    List<Pupil> getListOfPupils(int clazzId) throws ServiceException;

    Pupil movePupilToAnotherClass(int pupilId, Integer classId) throws ServiceException;

    Pupil getPupilInfo(int pupilId) throws ServiceException;


}
