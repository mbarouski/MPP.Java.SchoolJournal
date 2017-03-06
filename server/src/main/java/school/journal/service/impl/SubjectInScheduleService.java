package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import school.journal.entity.SubjectInSchedule;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.service.CRUDService;
import school.journal.service.ISubjectInScheduleService;
import school.journal.service.ServiceAbstractClass;
import school.journal.service.exception.ServiceException;
import static school.journal.utils.ValidateServiceUtils.*;

import java.sql.Time;
import java.util.List;

@Component
public class SubjectInScheduleService extends CRUDService<SubjectInSchedule> implements ISubjectInScheduleService {


    public SubjectInScheduleService() {
        LOGGER = Logger.getLogger(SubjectInSchedule.class);
    }

    private void checkTime(Time time) throws ServiceException{
        if(time.before(new Time(7,0,0)) || time.after(new Time(20,0,0)) ){
            throw new ServiceException("Invalid begin time of subject");
        }
    }

    @Override
    public SubjectInSchedule create(SubjectInSchedule subjectInSchedule) throws ServiceException {
        validateId(subjectInSchedule.getClassId(),"Class");
        validateId(subjectInSchedule.getTeacherId(),"Teacher");
        validateString(subjectInSchedule.getPlace(),"Place");
        checkTime(subjectInSchedule.getBeginTime());
        return super.create(subjectInSchedule);
    }

    @Override
    public SubjectInSchedule update(SubjectInSchedule subjectInSchedule) throws ServiceException {
        validateId(subjectInSchedule.getSubectInScheduleId(),"SubjectInSchedule");
        validateId(subjectInSchedule.getClassId(),"Class");
        validateId(subjectInSchedule.getTeacherId(),"Teacher");
        validateString(subjectInSchedule.getPlace(),"Place");
        checkTime(subjectInSchedule.getBeginTime());
        return super.update(subjectInSchedule);
    }

    @Override
    public void delete(int id) throws ServiceException {
        validateId(id, "SubjectInSchedule");
        SubjectInSchedule subject = new SubjectInSchedule();
        subject.setSubectInScheduleId(id);
        super.delete(subject);
    }

    @Override
    public List<SubjectInSchedule> read() throws ServiceException {
        return super.read();
    }
    
}
