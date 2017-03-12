package school.journal.service;

import school.journal.entity.Clazz;
import school.journal.entity.Pupil;
import school.journal.service.exception.ServiceException;

import java.util.List;

public interface IClassService extends IService<Clazz> {

    Clazz getOne(int classId) throws ServiceException;

}
