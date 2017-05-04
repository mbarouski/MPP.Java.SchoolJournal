package school.journal.service.impl;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.log4j.Logger;
import school.journal.service.ISVCService;
import school.journal.service.exception.ServiceException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

public class SVCService implements ISVCService {

    private static final Logger LOGGER = Logger.getLogger(SVCService.class);
    private static final Character SEPARATOR = ';';
    private static final Character QUOTECHAR = '\"';

    {
        StringWriter stringWriter = new StringWriter();
        CSVWriter writer = null;
        writer = new CSVWriter(stringWriter, SEPARATOR, QUOTECHAR);
        writer.writeNext(new String[]{"Abra,", "\\kadabra", "\tdas\td", "\'", "das;", "\"", "\"dsads\""});
        LOGGER.error(stringWriter.getBuffer().toString());
    }

    @Override
    public String getClassWithFormerTeacher(int classId) throws ServiceException {



        return null;
    }

    @Override
    public String getTeacherSchedule(int teacherId) throws ServiceException {
        return null;
    }

    @Override
    public String getClassSchedule(int classId) throws ServiceException {
        return null;
    }

    @Override
    public String getFullSchedule() throws ServiceException {
        return null;
    }

    @Override
    public String getMarks() throws ServiceException {
        return null;
    }
}
