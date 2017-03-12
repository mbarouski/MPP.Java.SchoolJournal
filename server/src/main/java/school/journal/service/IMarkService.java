package school.journal.service;

import school.journal.entity.Mark;
import school.journal.service.exception.ServiceException;

public interface IMarkService extends IService<Mark>{

    public Mark getOne(int id) throws ServiceException ;
}
