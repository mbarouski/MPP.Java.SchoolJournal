package school.journal.service;

<<<<<<< 28488bca656d04fc5395b1769047d0039713e80e
import school.journal.entity.Subject;
import school.journal.service.exception.ServiceException;

=======
>>>>>>> 60d5112bc89062b0dd824662e1516bbfec9be6ad
public interface ISubjectService extends IService<Subject> {

    Subject getOne(int subjectId) throws ServiceException;
}
