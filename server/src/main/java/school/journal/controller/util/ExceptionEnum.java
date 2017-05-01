package school.journal.controller.util;

public enum ExceptionEnum {
    wrong_id_number("Некорректно задан идентификатор."),

    invalid_class_letter("Неверно задана буква класса."),
    invalid_class_number("Номер класса находится вне допустимого диапазона (1 - 11)."),
    class_not_found("Искомый класс не существует."),

    not_found_lesson_time("Данный урок не найдет."),
    previous_lesson_is_overlapped("Ошибка. Перекрыто время предыдущего урока."),
    next_lesson_is_overlapped("Ошибка. Перекрыто время последующего урока."),
    lesson_is_before_school_day_begin("Ошибка. Невозможно задать время урока раньше начала возможного рабочего дня (7:00)."),
    lesson_is_after_school_day_end("Ошибка. Последний урок должен завершаться до конца возможного рабочего дня (22:00)."),
    lesson_starts_after_it_ends("Ошибка. Начало урока оказалось после его окончания."),
    lesson_length_is_short("Урок слишком короткий (Менее академического часа)."),

    mark_has_wrong_pupil("Невозможно поставить оценку несуществующему ученику."),
    mark_has_wrong_subject("Не существует предмета для заданной оценки."),
    mark_has_wrong_teacher("Учитель, от имени которого ставится оценка не существует."),
    mark_not_found("Данной оценки не существует"),
    mark_date_is_invalid("Некорректно задана дата оценки. Ошибка в формате или в несоответствии оценки данной четверти."),
    mark_has_wrong_value("Значение оценки неверно. Возможные значения: числа от 1 до 10"),

    pupil_not_found("Искомый ученик не существует"),
    pupil_has_wrong_class("Не существует класса для этого ученика"),
    pupil_has_wrong_first_name("Неверно введено имя. Оно не должно быть пустым."),
    pupil_has_wrong_last_name("Неверно введена фамилия. Она не должна быть пустой."),
    pupil_has_wrong_patronymic("Неверно введено отчество."),
    pupil_has_wrong_phone("Телефон не соответствует международному формату: +XXXAAAAAAAAA, где X - код страны, A - знчаение номера."),

    role_not_found("Роль не найдена."),
    role_has_wrong_name("Название роли не сооответствует параметрам."),
    role_has_wrong_level("Уровень роли не корректен. (Возможно следует вводить положительные значения)."),

    subject_in_schedule_not_found("Не существует предмета в расписании."),
    subject_in_schedule_has_wrong_teacher("Не существует учителя для данного предмета в расписании."),
    subject_in_schedule_has_wrong_subject("Предмету в расписании не соответсвует реальный предмет."),
    subject_in_schedule_has_wrong_class("Предмет в расписании приписан к несуществующему классу."),
    subject_in_schedule_has_wrong_time("Предмет в расписании записан на недопустимое время"),
    subject_in_schedule_has_wrong_day_of_week("Неверно задан день недели для заданного урока в расписании"),
    subject_in_schedule_has_wrong_place("Неверно задано место проведения урока."),
    subject_in_schedule_teacher_dublicate("Учитель уже ведет урок в данное время."),
    subject_in_schedule_class_dublicate("Предмет в данном классе уже проводится в это время."),

    subject_not_found("Предмет не найден"),
    subject_has_wrong_name("Неверно задано имя предмета. Возможно введена пустая строка."),
    subject_has_wrong_description("Описание предмета не соответствует правилам."),

    teacher_not_found("Учитель не найден."),
    teacher_has_wrong_first_name("Неверно введено имя. Оно не должно быть пустым."),
    teacher_has_wrong_last_name("Неверно введена фамилия. Она не должна быть пустой."),
    teacher_has_wrong_patronymic("Неверно введено отчество."),
    teacher_has_wrong_class("Неверно задан класс для классного руководства"),
    teacher_has_wrong_characteristic("Описание учителя не задано корректно"),
    teacher_has_wrong_phone("Телефон не соответствует международному формату: +XXXAAAAAAAAA, где X - код страны, A - знчаение номера."),

    term_not_found("Четверть не найдена"),
    first_term_start_is_before_september_1st("Первая четверть не может начинаться раньше 1 сентября"),
    first_term_start_is_after_september_1st("Первая четверть не может начинаться позже 1 сентября"),
    last_term_end_is_before_May_31st("Последняя четверть не может заканчиваться раньше 31 мая"),
    last_term_end_is_after_May_31st("Последняя четверть не может заканчиваться позже 31 мая"),
    too_long_year("Учебный год длится слишком много. Возможно следует изменить календарный год"),
    vacations_are_too_small("Суммарная длина каникул слишком мала. Минимальное количество дней для каникул - 30"),
    term_start_is_after_its_end("Четверть не может заканчиваться перед тем как начаться"),
    term_is_overlapped_by_next_term("Четверть перекрывается следующей четвертью"),
    term_is_overlapped_by_previous_term("Четверть перекрывается предыдущей четвертью"),

    user_not_found("ПОльзователь не найден."),
    password_is_small("Слишком короткий пароль"),
    username_not_valid("Имя пользователя некорректно."),
    password_is_null("Пароль пуст"),
    email_not_valid("Неверно задан email-адрес"),

    not_classified("Не классифицируемая ошибка");

    String value;

    ExceptionEnum(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
