package school.journal.service;

import school.journal.entity.Clazz;
import school.journal.entity.Pupil;
import school.journal.service.exception.ServiceException;

import java.util.List;

public interface ICLassService {


    List<Clazz> getClasses() throws ServiceException;

    Clazz createClass(Clazz clazz) throws ServiceException;

    Clazz updateClass(Clazz clazz) throws ServiceException;

    Clazz deleteClass(int classId) throws ServiceException;

    Clazz getOne(int classId) throws ServiceException;
}
