package school.journal.service;

import school.journal.entity.Clazz;
import school.journal.entity.Pupil;
import school.journal.service.exception.ServiceException;

import java.util.List;

public interface IPupilService extends IService<Pupil>{

    List<Pupil> getListOfPupils(int clazzId) throws ServiceException;

    Pupil movePupulToAnotherClass(Pupil pupil, Clazz clazz) throws ServiceException;

    Pupil getOne(int pupilId) throws ServiceException;


}
