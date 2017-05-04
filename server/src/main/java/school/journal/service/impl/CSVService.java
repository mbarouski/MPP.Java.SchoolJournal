package school.journal.service.impl;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import school.journal.entity.Pupil;
import school.journal.entity.Teacher;
import school.journal.repository.impl.TeacherRepository;
import school.journal.repository.specification.teacher.TeacherSpecificationByClassId;
import school.journal.service.IDocumentGenerationService;
import school.journal.service.exception.ServiceException;

import java.io.StringWriter;
import java.util.List;

public class CSVService implements IDocumentGenerationService {

    private static final Logger LOGGER = Logger.getLogger(CSVService.class);
    private static final Character SEPARATOR = ';';
    private static final Character QUOTECHAR = '\"';

    @Autowired
    ClassService classService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    SubjectInScheduleService subjectInScheduleService;
    @Autowired
    PupilService pupilService;
    @Autowired
    TeacherRepository teacherRepository;


    static {
        StringWriter stringWriter = new StringWriter();
        CSVWriter writer = null;
        writer = new CSVWriter(stringWriter, SEPARATOR, QUOTECHAR);
        writer.writeNext(new String[]{"Abra,", "\\kadabra", "\tdas\td", "\'", "das;", "\"", "\"dsads\""});
        LOGGER.error(stringWriter.getBuffer().toString());
    }


    @Override
    public String createPupilClassListWithFormerTeacherDocument(int classId) throws ServiceException {
        List<Pupil> pupils = pupilService.getListOfPupils(classId);
//        Teacher teacher = teacherRepository.query(new TeacherSpecificationByClassId(classId));




        return null;
    }

    @Override
    public String createTeacherScheduleDocument(int teacherId) throws ServiceException {
        return null;
    }

    @Override
    public String createClassScheduleDocument(int classId) throws ServiceException {
        return null;
    }

    @Override
    public String createFullScheduleDocument() throws ServiceException {
        return null;
    }

    @Override
    public String createMarksDocument() throws ServiceException {
        return null;
    }
}
