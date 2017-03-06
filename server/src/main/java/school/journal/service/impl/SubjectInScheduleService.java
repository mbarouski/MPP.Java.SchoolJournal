package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import school.journal.entity.SubjectInSchedule;
import school.journal.service.CRUDService;
import school.journal.service.ISubjectInScheduleService;
import school.journal.service.exception.ServiceException;

import java.sql.Time;
import java.util.List;

import static school.journal.utils.ValidateServiceUtils.validateId;
import static school.journal.utils.ValidateServiceUtils.validateNullableId;
import static school.journal.utils.ValidateServiceUtils.validateString;

@Component
public class SubjectInScheduleService extends CRUDService<SubjectInSchedule> implements ISubjectInScheduleService {


    public SubjectInScheduleService() {
        LOGGER = Logger.getLogger(SubjectInSchedule.class);
    }

    private void checkTime(Time time) throws ServiceException{
        if(time.before(new Time(7,0,0))
                || time.after(new Time(20,0,0)) )
            throw new ServiceException("Invalid begin time of subject");
    }

    @Override
    public SubjectInSchedule create(SubjectInSchedule subject) throws ServiceException {
        validateId(subject.getClassId(),"Class");
        validateNullableId(subject.getTeacherId(),"Teacher");
        validateString(subject.getPlace(),"Place");
        checkTime(subject.getBeginTime());
        return super.create(subject);
    }

    @Override
    public SubjectInSchedule update(SubjectInSchedule subject) throws ServiceException {
        validateId(subject.getSubectInScheduleId(),"SubjectInSchedule");
        validateId(subject.getClassId(),"Class");
        validateNullableId(subject.getTeacherId(),"Teacher");
        validateString(subject.getPlace(),"Place");
        checkTime(subject.getBeginTime());
        return super.update(subject);
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
