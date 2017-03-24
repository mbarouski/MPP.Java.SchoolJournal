package school.journal.service;

import school.journal.entity.Clazz;
import school.journal.service.exception.ServiceException;

public interface IClassService extends IService<Clazz> {

    Clazz getOne(int classId) throws ServiceException;

}
