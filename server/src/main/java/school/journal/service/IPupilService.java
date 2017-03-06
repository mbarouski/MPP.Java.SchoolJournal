package school.journal.service;

import school.journal.entity.Pupil;
import school.journal.service.exception.ServiceException;

import java.util.List;

public interface IPupilService extends IService<Pupil>{

    List<Pupil> getListOfPupils(int clazzId) throws ServiceException;

    Pupil movePupilToAnotherClass(Pupil pupil, int classId) throws ServiceException;

    Pupil getOne(int pupilId) throws ServiceException;


}
