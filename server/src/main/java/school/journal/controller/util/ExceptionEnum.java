package school.journal.controller.util;

public enum ExceptionEnum {
    wrong_id_number("wrong id number"),

    invalid_class_letter("invalid class letter"),
    invalid_class_number("invalid class number"),
    class_not_found("class not found"),

    not_found_lesson_time("lesson time not found"),
    previous_lesson_is_overlapped("previous lesson is overlapped"),
    next_lesson_is_overlapped("next lesson is overlapped"),
    lesson_is_before_school_day_begin("lesson is before school day begin"),
    lesson_is_after_school_day_end("lesson is after school day end"),
    lesson_starts_after_it_ends("lesson starts after it ends"),
    lesson_length_is_short("lesson is too short"),

    mark_has_wrong_pupil("wrong pupil in mark"),
    mark_has_wrong_subject("wrong subject in mark"),
    mark_has_wrong_teacher("wrong teacher in mark"),
    mark_not_found("mark not found"),
    mark_date_is_invalid("mark can't be set to that date"),
    mark_has_wrong_value("mark has wrong value"),

    pupil_not_found("pupil not found"),
    pupil_has_wrong_class("pupil has wrong class"),
    pupil_has_wrong_first_name("pupil has wrong first name"),
    pupil_has_wrong_last_name("pupil has wrong last name"),
    pupil_has_wrong_patronymic("pupil has wrong patronymic"),
    pupil_has_wrong_phone("pupil has wrong phone"),

    role_not_found("role not found"),
    role_has_wrong_name("role name is not valid"),
    role_has_wrong_level("role level is not valid"),

    subject_in_schedule_not_found("subject in schedule not found"),
    subject_in_schedule_has_wrong_teacher("subject in schedule has wrong teacher"),
    subject_in_schedule_has_wrong_subject("subject in schedule has wrong subject"),
    subject_in_schedule_has_wrong_class("subject in schedule has wrong class"),
    subject_in_schedule_has_wrong_time("subject in schedule has wrong time"),
    subject_in_schedule_has_wrong_day_of_week("subject in schedule has wrong day_of_week"),
    subject_in_schedule_has_wrong_place("subject in schedule has wrong day_of_week"),
    subject_in_schedule_teacher_dublicate("subject in schedule has wrong day_of_week"),
    subject_in_schedule_class_dublicate("subject in schedule has wrong day_of_week"),

    subject_not_found("subject not found"),
    subject_has_wrong_name("subject has wrong name"),
    subject_has_wrong_description("subject has wrong description"),

    teacher_not_found("teacher not found"),
    teacher_has_wrong_first_name("teacher has wrong first name"),
    teacher_has_wrong_last_name("teacher has wrong last name"),
    teacher_has_wrong_patronymic("teacher has wrong patronymic"),
    teacher_has_wrong_class("teacher has wrong class"),
    teacher_has_wrong_characteristic("teacher has wrong characteristic"),
    teacher_has_wrong_phone("pupil has wrong phone"),

    term_not_found("Term not found"),
    first_term_start_is_before_september_1st("First term start is before September 1st"),
    first_term_start_is_after_september_1st("First term start is after September 1st"),
    last_term_end_is_before_May_31st("Last term end is before May 31st"),
    last_term_end_is_after_May_31st("Last term end after May 31st"),
    too_long_year("too_long_year"),
    vacations_are_too_small("vacations_are_too_small"),
    term_start_is_after_its_end("term start is after it end"),
    term_is_overlapped_by_next_term("Term is overlapped by next term"),
    term_is_overlapped_by_previous_term("Term is overlapped by previous term"),

    user_not_found("user not found"),
    password_is_small("password is small"),
    username_not_valid("username not valid"),
    password_is_null("password is null"),
    email_not_valid("email not valid"),

    not_classified("not classified");

    String value;

    ExceptionEnum(String value){
        this.value = value;
    }

}
