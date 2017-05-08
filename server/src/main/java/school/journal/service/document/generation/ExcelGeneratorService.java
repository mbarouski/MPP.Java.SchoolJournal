package school.journal.service.document.generation;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import school.journal.entity.*;
import school.journal.entity.enums.DayOfWeekEnum;
import school.journal.entity.enums.MarkType;
import school.journal.service.exception.ServiceException;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("ExcelGenerator")
public class ExcelGeneratorService implements IGenerator {

    short borderColor = IndexedColors.BLACK.getIndex();

    @Override
    public OutputStream generateClassPupilListDocument(OutputStream os, Teacher teacher, Clazz clazz, List<Pupil> pupilList) throws ServiceException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Список учеников "+clazz.getNumber().toString()+clazz.getLetterMark());
        int rowNum = 0;


        Row headerRow = sheet.createRow(rowNum++);
        headerRow.setHeightInPoints(40);
        Cell cell = headerRow.createCell(0);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$C$1"));

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 20);
        CellStyle style = workbook.createCellStyle();

        style.setFont(headerFont);
        setAligmentToCellStyle(HorizontalAlignment.CENTER,VerticalAlignment.CENTER,style);
        cell.setCellValue("Список учеников "+clazz.getNumber().toString()+clazz.getLetterMark());
        cell.setCellStyle(style);
        rowNum++;

        style = workbook.createCellStyle();
        setAligmentToCellStyle(HorizontalAlignment.CENTER,VerticalAlignment.CENTER,style);
        setBordersToCellStyle(true,true,true,true,style,borderColor);
        Row listRow = sheet.createRow(rowNum);
        createCellWithValue(listRow, "Номер", 0,style);
        createCellWithValue(listRow, "ФИО", 1,style);
        createCellWithValue(listRow, "Телефон", 2,style);

        style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        rowNum++;
        for(int i =0; i < pupilList.size(); i++){
            Row row = sheet.createRow(rowNum++);

            style = workbook.createCellStyle();
            setAligmentToCellStyle(HorizontalAlignment.CENTER,VerticalAlignment.CENTER,style);
            setLastTableCellBorder(i,pupilList.size()-1,style);
            createCellWithValue(row, String.valueOf(i+1),0,style);

            style = workbook.createCellStyle();
            setAligmentToCellStyle(HorizontalAlignment.LEFT,VerticalAlignment.CENTER,style);
            setLastTableCellBorder(i,pupilList.size()-1,style);
            createCellWithValue(row, pupilList.get(i).getLastName() + " " + pupilList.get(i).getFirstName() +" "+ pupilList.get(i).getPathronymic(), 1,style);

            style = workbook.createCellStyle();
            setAligmentToCellStyle(HorizontalAlignment.CENTER,VerticalAlignment.CENTER,style);
            setLastTableCellBorder(i,pupilList.size()-1,style);
            createCellWithValue(row, pupilList.get(i).getPhoneNumber(),2,style);
        }

        if(teacher != null){
            rowNum++;
            Row teacherRow = sheet.createRow(rowNum);
            style = workbook.createCellStyle();
            setAligmentToCellStyle(HorizontalAlignment.LEFT,VerticalAlignment.CENTER,style);
            createCellWithValue(teacherRow, "Классный руководитель", 1,style);
            createCellWithValue(teacherRow, teacher.getLastName() + " "+ teacher.getFirstName() + " "+ teacher.getPathronymic(), 2,style);
        }
        sheet.setColumnWidth(0, 256*8);
        sheet.setColumnWidth(1, 256*40);
        sheet.setColumnWidth(2, 256*35);
        String file = "C:\\Users\\Mr.Blacky\\Desktop\\pupilList.xls";
        output(file,workbook);
        return null;
    }



    @Override
    public OutputStream generateTeacherScheduleDocument(OutputStream os, Teacher teacher, List<SubjectInSchedule> subjectInScheduleList, List<LessonTime> lessonTimeList) throws ServiceException {

        List<DayOfWeekEnum> lessonDays = new ArrayList<>();
        for (SubjectInSchedule subject : subjectInScheduleList) {
            if(!lessonDays.contains(subject.getDayOfWeek())) {
                lessonDays.add(subject.getDayOfWeek());
            }
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(teacher.getLastName()+ " " +
                                                teacher.getFirstName().charAt(0) + ". " + teacher.getPathronymic().charAt(0)+".");
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);

        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum++);
        headerRow.setHeightInPoints(40);
        Cell cell = headerRow.createCell(0);
        String alphabet = "ABCDEFGHI";
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$" + alphabet.charAt(lessonDays.size()) + "$1"));

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 20);
        CellStyle style = workbook.createCellStyle();

        style.setFont(headerFont);
        setAligmentToCellStyle(HorizontalAlignment.LEFT,VerticalAlignment.CENTER,style);
        cell.setCellValue("Расписание учителя " + teacher.getLastName()+ " " +
                teacher.getFirstName() + " " + teacher.getPathronymic());
        cell.setCellStyle(style);
        rowNum++;

        style = workbook.createCellStyle();
        setAligmentToCellStyle(HorizontalAlignment.CENTER,VerticalAlignment.CENTER,style);
        setBordersToCellStyle(true,true,true,true,style,borderColor);
        Row titleRow = sheet.createRow(rowNum);
        createCellWithValue(titleRow, "Время", 0,style);
        for(int i = 0; i < lessonDays.size();i++){
            createCellWithValue(titleRow, lessonDays.get(i).name(), i + 1,style);
            sheet.setColumnWidth(i+1, 256*35);
        }
        rowNum++;
        SubjectInSchedule minSubjectTime = subjectInScheduleList.get(0);
        for (int i = 1; i < subjectInScheduleList.size(); i++ ){
            if(minSubjectTime.getBeginTime().getTime() > subjectInScheduleList.get(i).getBeginTime().getTime()){
                minSubjectTime = subjectInScheduleList.get(i);
            }
        }

        SubjectInSchedule maxSubjectTime = subjectInScheduleList.get(0);
        for (int i = 1; i < subjectInScheduleList.size(); i++ ){
            if(maxSubjectTime.getBeginTime().getTime() < subjectInScheduleList.get(i).getBeginTime().getTime()){
                maxSubjectTime = subjectInScheduleList.get(i);
            }
        }

        int startIndex = 0;
        int endIndex = 0;
        for(int i = 0; i < lessonTimeList.size(); i++){
            if(lessonTimeList.get(i).getStartTime().getTime() == minSubjectTime.getBeginTime().getTime()){
                startIndex = i;
            }
            if(lessonTimeList.get(i).getStartTime().getTime() == maxSubjectTime.getBeginTime().getTime()){
                endIndex = i;
            }
        }

        for (int i = startIndex; i < endIndex + 1; i++){
            style = workbook.createCellStyle();
            setAligmentToCellStyle(HorizontalAlignment.CENTER,VerticalAlignment.CENTER,style);
            setBordersToCellStyle(true,true,true,true,style,borderColor);
            style.setWrapText(true);
            Row timeRow = sheet.createRow(rowNum);
            createCellWithValue(timeRow,lessonTimeList.get(i).getStartTime().toString() + "-"+
                                        lessonTimeList.get(i).getEndTime().toString(),0,style);

            fillTableWithEmptyValue(workbook,lessonDays.size(),timeRow);

            List<SubjectInSchedule> scheduleList = new ArrayList<>();
            for(SubjectInSchedule subjectInSchedule : subjectInScheduleList){
                if(subjectInSchedule.getBeginTime().getTime() == lessonTimeList.get(i).getStartTime().getTime()){
                    scheduleList.add(subjectInSchedule);
                }
            }
            for (SubjectInSchedule subject : scheduleList){

                createCellWithValue(timeRow,
                                    subject.getSubject().getName() + ", " +
                                    subject.getPlace() + ", " +
                                    String.valueOf(subject.getClazz().getNumber()) +
                                    subject.getClazz().getLetterMark(),
                                    subject.getDayOfWeek().getValue() ,
                                    style);
            }
            rowNum++;
        }

        sheet.setColumnWidth(0, 256*16);
        String file = "C:\\Users\\Mr.Blacky\\Desktop\\teacherSchedule+"+teacher.getLastName()+".xls";
        output(file,workbook);
        return null;
    }

    @Override
    public OutputStream generateClassScheduleDocument(OutputStream os, Clazz clazz, List<SubjectInSchedule> subjectInScheduleList, List<LessonTime> lessonTimeList) throws ServiceException {
        List<DayOfWeekEnum> lessonDays = new ArrayList<>();
        for (SubjectInSchedule subject : subjectInScheduleList) {
            if(!lessonDays.contains(subject.getDayOfWeek())) {
                lessonDays.add(subject.getDayOfWeek());
            }
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(clazz.getNumber()+clazz.getLetterMark());
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);

        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum++);
        headerRow.setHeightInPoints(40);
        Cell cell = headerRow.createCell(0);
        String alphabet = "ABCDEFGHI";
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$" + alphabet.charAt(lessonDays.size()) + "$1"));

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 20);
        CellStyle style = workbook.createCellStyle();

        style.setFont(headerFont);
        setAligmentToCellStyle(HorizontalAlignment.LEFT,VerticalAlignment.CENTER,style);
        cell.setCellValue("Расписание " + clazz.getNumber()+clazz.getLetterMark()+ " класса");
        cell.setCellStyle(style);
        rowNum++;

        style = workbook.createCellStyle();
        setAligmentToCellStyle(HorizontalAlignment.CENTER,VerticalAlignment.CENTER,style);
        setBordersToCellStyle(true,true,true,true,style,borderColor);
        Row titleRow = sheet.createRow(rowNum);
        createCellWithValue(titleRow, "Время", 0,style);
        for(int i = 0; i < lessonDays.size();i++){
            createCellWithValue(titleRow, lessonDays.get(i).name(), i + 1,style);
            sheet.setColumnWidth(i+1, 256*35);
        }
        rowNum++;
        SubjectInSchedule minSubjectTime = subjectInScheduleList.get(0);
        for (int i = 1; i < subjectInScheduleList.size(); i++ ){
            if(minSubjectTime.getBeginTime().getTime() > subjectInScheduleList.get(i).getBeginTime().getTime()){
                minSubjectTime = subjectInScheduleList.get(i);
            }
        }

        SubjectInSchedule maxSubjectTime = subjectInScheduleList.get(0);
        for (int i = 1; i < subjectInScheduleList.size(); i++ ){
            if(maxSubjectTime.getBeginTime().getTime() < subjectInScheduleList.get(i).getBeginTime().getTime()){
                maxSubjectTime = subjectInScheduleList.get(i);
            }
        }

        int startIndex = 0;
        int endIndex = 0;
        for(int i = 0; i < lessonTimeList.size(); i++){
            if(lessonTimeList.get(i).getStartTime().getTime() == minSubjectTime.getBeginTime().getTime()){
                startIndex = i;
            }
            if(lessonTimeList.get(i).getStartTime().getTime() == maxSubjectTime.getBeginTime().getTime()){
                endIndex = i;
            }
        }

        for (int i = startIndex; i < endIndex + 1; i++){
            style = workbook.createCellStyle();
            setAligmentToCellStyle(HorizontalAlignment.CENTER,VerticalAlignment.CENTER,style);
            setBordersToCellStyle(true,true,true,true,style,borderColor);
            style.setWrapText(true);
            Row timeRow = sheet.createRow(rowNum);
            createCellWithValue(timeRow,lessonTimeList.get(i).getStartTime().toString() + "-"+
                    lessonTimeList.get(i).getEndTime().toString(),0,style);
            fillTableWithEmptyValue(workbook,lessonDays.size(),timeRow);

            List<SubjectInSchedule> scheduleList = new ArrayList<>();
            for(SubjectInSchedule subjectInSchedule : subjectInScheduleList){
                if(subjectInSchedule.getBeginTime().getTime() == lessonTimeList.get(i).getStartTime().getTime()){
                    scheduleList.add(subjectInSchedule);
                }
            }
            for (SubjectInSchedule subject : scheduleList){

                createCellWithValue(timeRow,
                                    subject.getSubject().getName() + ", " +
                                    subject.getPlace() + ", " +
                                    subject.getTeacher().getLastName() + " " +
                                    subject.getTeacher().getFirstName().charAt(0) + ". " +
                                    subject.getTeacher().getPathronymic().charAt(0) + ".",
                                    subject.getDayOfWeek().getValue() ,
                                    style);
            }
            rowNum++;
        }

        sheet.setColumnWidth(0, 256*16);
        String file = "C:\\Users\\Mr.Blacky\\Desktop\\classSchedule+"+clazz.getNumber()+clazz.getLetterMark()+".xls";
        output(file,workbook);
        return null;
    }

    @Override
    public OutputStream generateFullScheduleDocument(OutputStream os, List<SubjectInSchedule> subjectInScheduleList, List<LessonTime> lessonTimeList,List<Teacher> teacherList) throws ServiceException {

        List<DayOfWeekEnum> lessonDays = new ArrayList<>();
        lessonDays.add(DayOfWeekEnum.mon);
        lessonDays.add(DayOfWeekEnum.tue);
        lessonDays.add(DayOfWeekEnum.wed);
        lessonDays.add(DayOfWeekEnum.thu);
        lessonDays.add(DayOfWeekEnum.fri);
        lessonDays.add(DayOfWeekEnum.sat);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Full schedule");
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);

        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum++);
        headerRow.setHeightInPoints(40);
        Cell cell = headerRow.createCell(0);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$BI$1"));
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 20);
        CellStyle style = workbook.createCellStyle();

        style.setFont(headerFont);
        setAligmentToCellStyle(HorizontalAlignment.LEFT,VerticalAlignment.CENTER,style);
        cell.setCellValue("Полное расписание");
        cell.setCellStyle(style);
        rowNum++;

        style = workbook.createCellStyle();
        setAligmentToCellStyle(HorizontalAlignment.CENTER,VerticalAlignment.CENTER,style);
        setBordersToCellStyle(true,true,true,true,style,borderColor);
        style.setWrapText(true);

        CellStyle dayTitleStyle = workbook.createCellStyle();
        setAligmentToCellStyle(HorizontalAlignment.LEFT,VerticalAlignment.CENTER,dayTitleStyle);
        setBordersToCellStyle(true,true,true,true,dayTitleStyle,borderColor);
        dayTitleStyle.setWrapText(true);

        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$3:$A$4"));
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Row titleRow = sheet.createRow(rowNum++);
        Row timeRow = sheet.createRow(rowNum++);
        createCellWithValue(timeRow,"",0,style);
        createCellWithValue(titleRow, "Учитель", 0,style);
        for (int i = 0; i < lessonDays.size(); i++){
            int startPosition = 10 * i + 1;
            int lastPosition = 10 * (i + 1);
            String firstLetter = getCellAphabetPositionByNumber(startPosition);
            String secondLetter = getCellAphabetPositionByNumber(lastPosition);

            sheet.addMergedRegion(CellRangeAddress.valueOf("$" + firstLetter +"$3:$"+secondLetter+"$3"));
            for (int k=10*i+1; k < 10*i + 11; k++){
                createCellWithValue(titleRow,"",k,style);
            }
            createCellWithValue(titleRow,lessonDays.get(i).name(),10 * i + 1,dayTitleStyle);
            for(int j = 0 ;j < lessonTimeList.size(); j++){
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                createCellWithValue(timeRow,format.format(lessonTimeList.get(j).getStartTime()) + "-" +
                                    format.format(lessonTimeList.get(j).getEndTime()),j+1+10*i,style);
                sheet.setColumnWidth(j+1+10*i, 256*20);
            }
        }

        for(Teacher teacher:teacherList){
            List<SubjectInSchedule> teacherSchedule = new ArrayList<>();
            for(SubjectInSchedule subject: subjectInScheduleList){
                if(Objects.equals(subject.getTeacher().getUserId(), teacher.getUserId())){
                    teacherSchedule.add(subject);
                }
            }
            Row teacherRow = sheet.createRow(rowNum++);
            createCellWithValue(teacherRow,teacher.getLastName()+ " " +
                            teacher.getFirstName().charAt(0) + ". " + teacher.getPathronymic().charAt(0),
                    0,style);
            for(int i = 0; i < 60; i++){
                createCellWithValue(teacherRow,"",i+1,style);
            }

            for(SubjectInSchedule subject: teacherSchedule){
                int cellPosition = (subject.getDayOfWeek().getValue() - 1) * 10;
                for(int i = 0; i < lessonTimeList.size();i++){
                    if(subject.getBeginTime().getTime() == lessonTimeList.get(i).getStartTime().getTime()){
                        cellPosition += i + 1;
                    }
                }
                createCellWithValue(teacherRow,subject.getSubject().getName() + ", " +
                        subject.getClazz().getNumber() + subject.getClazz().getLetterMark()+ ", "+
                        subject.getPlace(),cellPosition,style);
            }

        }

        sheet.setColumnWidth(0, 256*20);
        String file = "C:\\Users\\Mr.Blacky\\Desktop\\fullSchedule.xls";
        output(file,workbook);
        return null;
    }

    @Override
    public OutputStream generateMarksDocument(OutputStream os, Subject subject, List<Mark> markList, List<Pupil> pupilList, Clazz clazz, List<Date> lessonDateList) throws ServiceException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Marks " + clazz.getNumber() + clazz.getLetterMark());
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);

        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum++);
        headerRow.setHeightInPoints(40);
        Cell cell = headerRow.createCell(0);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$" + getCellAphabetPositionByNumber(lessonDateList.size() + 5) + "$1"));
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 20);
        CellStyle style = workbook.createCellStyle();

        style.setFont(headerFont);
        setAligmentToCellStyle(HorizontalAlignment.LEFT,VerticalAlignment.CENTER,style);
        cell.setCellValue("Оценки " + clazz.getNumber() + clazz.getLetterMark()+ " класса");
        cell.setCellStyle(style);
        rowNum++;

        style = workbook.createCellStyle();
        setAligmentToCellStyle(HorizontalAlignment.CENTER,VerticalAlignment.CENTER,style);
        setBordersToCellStyle(true,true,true,true,style,borderColor);
        style.setWrapText(true);

        Row titleRow = sheet.createRow(rowNum++);
        createCellWithValue(titleRow,"Ученик",0,style);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");
        for(int i = 0; i < lessonDateList.size(); i++){
            createCellWithValue(titleRow,dateFormat.format(lessonDateList.get(i)),i + 1,style);
            sheet.setColumnWidth(0, 256*5);
        }
        createCellWithValue(titleRow,"I четв.",lessonDateList.size()+1,style);
        createCellWithValue(titleRow,"II четв.",lessonDateList.size()+2,style);
        createCellWithValue(titleRow,"III четв.",lessonDateList.size()+3,style);
        createCellWithValue(titleRow,"VI четв.",lessonDateList.size()+4,style);
        createCellWithValue(titleRow,"Год оценка",lessonDateList.size()+5,style);

        sheet.setColumnWidth(0, 256*30);
        sheet.setColumnWidth(lessonDateList.size()+1, 256*7);
        sheet.setColumnWidth(lessonDateList.size()+2, 256*8);
        sheet.setColumnWidth(lessonDateList.size()+3, 256*9);
        sheet.setColumnWidth(lessonDateList.size()+4, 256*8);
        sheet.setColumnWidth(lessonDateList.size()+5, 256*10);

        for(Pupil pupil:pupilList) {
            Row pupilRow = sheet.createRow(rowNum++);
            createCellWithValue(pupilRow, pupil.getLastName() + " " +
                    pupil.getFirstName().charAt(0) + ". " + pupil.getPathronymic().charAt(0), 0, style);
            for (int i = 0; i < lessonDateList.size() + 5; i++) {
                createCellWithValue(pupilRow, "", i + 1, style);
            }
            List<Mark> pupilMarks = new ArrayList<>();
            for(Mark mark: markList){
                if(mark.getPupil().getUserId().equals(pupil.getUserId())){
                    pupilMarks.add(mark);
                }
            }

            for(Mark pupilMark: pupilMarks){
                CellStyle pupilMarkCellStyle = workbook.createCellStyle();
                setAligmentToCellStyle(HorizontalAlignment.CENTER,VerticalAlignment.CENTER,pupilMarkCellStyle);
                setBordersToCellStyle(true,true,true,true,pupilMarkCellStyle,borderColor);
                setMarkFont(pupilMarkCellStyle,pupilMark.getType());
                switch (pupilMark.getType().getMeaning()){
                    case 1: {
                        int pos =getPositionOfMarkCellOnDate(lessonDateList,pupilMark.getDate());
                        if(pos != -1){
                            createCellWithValue(pupilRow,pupilMark.getValue().toString(),pos,pupilMarkCellStyle);
                        }
                        break;
                    }
                    case 2: {
                        int pos =getPositionOfMarkCellOnDate(lessonDateList,pupilMark.getDate());
                        if(pos != -1){
                            createCellWithValue(pupilRow,"-",pos,pupilMarkCellStyle);
                        }
                        break;
                    }
                    case 3: {
                        int pos =getPositionOfMarkCellOnDate(lessonDateList,pupilMark.getDate());
                        if(pos != -1){
                            createCellWithValue(pupilRow,pupilMark.getValue().toString(),pos,pupilMarkCellStyle);
                        }
                        break;
                    }
                    case 4: {
                        int pos =getPositionOfMarkCellOnDate(lessonDateList,pupilMark.getDate());
                        if(pos != -1){
                            createCellWithValue(pupilRow,pupilMark.getValue().toString(),pos,pupilMarkCellStyle);
                        }
                        break;
                    }
                    case 5: {
                        createCellWithValue(pupilRow,pupilMark.getValue().toString(),lessonDateList.size()+pupilMark.getTermNumber(),pupilMarkCellStyle);
                        break;
                    }
                    case 6: {
                        createCellWithValue(pupilRow,pupilMark.getValue().toString(),lessonDateList.size() + 5,pupilMarkCellStyle);
                        break;
                    }
                }
            }

        }

        String file = "C:\\Users\\Mr.Blacky\\Desktop\\Marks.xls";
        output(file,workbook);
        return null;
    }

    private int getPositionOfMarkCellOnDate(List<Date> lessonDateList, Date markDate){
        for(int i = 0; i < lessonDateList.size(); i++){
            if(lessonDateList.get(i).getTime() == markDate.getTime()){
                return i + 1;
            }
        }
        return -1;
    }

    private String getCellAphabetPositionByNumber(int position){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String result;
        if(position < 25){
            result = alphabet.substring(position,position+1);
        }
        else {
            result = alphabet.substring(position / 26 - 1 ,position / 26 )+alphabet.substring(position % 26, position % 26 + 1) ;
        }
        return result;
    }

    private void setMarkFont(CellStyle style, MarkType markType){
        switch (markType.getMeaning()){
            case 1: {
                break;
            }
            case 2: {
                style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                break;
            }
            case 3: {
                style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                break;
            }
            case 4: {
                style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                break;
            }
            case 5: {
                style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                break;
            }
            case 6: {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                break;
            }
        }
    }
    private void fillTableWithEmptyValue(XSSFWorkbook workbook, int tableSize, Row row){
        for (int j = 0; j < tableSize; j++){
            CellStyle emptyStyle = workbook.createCellStyle();
            setAligmentToCellStyle(HorizontalAlignment.CENTER,VerticalAlignment.CENTER,emptyStyle);
            setBordersToCellStyle(true,true,true,true,emptyStyle,borderColor);
            createCellWithValue(row,"",j + 1,emptyStyle);
        }
    }
    private void setLastTableCellBorder(int i, int size,CellStyle style){
        if(i != size){
            setBordersToCellStyle(false,false,true,true,style,borderColor);
        }else {
            setBordersToCellStyle(false,true,true,true,style,borderColor);
        }
    }

    private void setBordersToCellStyle(boolean top,boolean bottom, boolean left,boolean right, CellStyle style,short borderColor){
        if(top){
            style.setBorderTop(BorderStyle.THIN);
            style.setTopBorderColor(borderColor);
        }
        if(bottom){
            style.setBorderBottom(BorderStyle.THIN);
            style.setBottomBorderColor(borderColor);
        }
        if(left){
            style.setBorderLeft(BorderStyle.THIN);
            style.setLeftBorderColor(borderColor);
        }
        if(right){
            style.setBorderRight(BorderStyle.THIN);
            style.setRightBorderColor(borderColor);
        }
    }

    private void setAligmentToCellStyle(HorizontalAlignment horizontalAlignment,VerticalAlignment verticalAlignment, CellStyle style){
        style.setAlignment(horizontalAlignment);
        style.setVerticalAlignment(verticalAlignment);
    }

    private void createCellWithValue(Row row, String title, int cellNum, CellStyle style){
        Cell cell = row.createCell(cellNum);
        cell.setCellValue(title);
        cell.setCellStyle(style);
    }

    private void output(String fileName,XSSFWorkbook workbook){
        if(workbook instanceof XSSFWorkbook) fileName += "x";
        try {
            FileOutputStream out = new FileOutputStream(fileName);
            workbook.write(out);
            out.close();
            workbook.close();
        }
        catch (Exception e){

        }
    }
}
