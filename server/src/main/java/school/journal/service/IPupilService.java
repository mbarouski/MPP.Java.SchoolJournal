package school.journal.service;

import school.journal.entity.Clazz;
import school.journal.entity.Pupil;
import school.journal.service.exception.ServiceException;

import java.util.List;

public interface IPupilService {

    List<Pupil> getPupils() throws ServiceException;

    List<Pupil> getListOfPupils(int clazzId) throws ServiceException;

    Pupil createPupil(Pupil pupil) throws ServiceException;

    Pupil updatePupil(Pupil pupil) throws ServiceException;

    Pupil movePupulToAnotherClass(Pupil pupil, Clazz clazz) throws ServiceException;

    Pupil deletePupil(int pupilId) throws ServiceException;

    Pupil getOne(int pupilId) throws ServiceException;


}
