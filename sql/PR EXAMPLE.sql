use school_journal_db;
INSERT INTO `role` (`name`,`level`)
Value ('test',1);

INSERT INTO `user` (`role_id`,`username`,`pass_hash`,`locked`,`email`)
VALUES 	(1,'v','v',false,'v'),
		(1,'a','a',false,'a');
		(1,'a','a',false,'a'),
        (1,'anton','vanton',false,'vanton@mail.com'),
		(1,'avlera','avlera',false,'avlera@mail.com');
		
INSERT INTO `school_journal_db`.`user` (`role_id`, `username`, `pass_hash`, `locked`, `email`) VALUES ('1', 'pupil3', 'pupil3', '0', 'pupil3');
INSERT INTO `school_journal_db`.`user` (`role_id`, `username`, `pass_hash`, `locked`, `email`) VALUES ('1', 'pupil4', 'pupil4', '0', 'pupil4');

		

INSERT INTO `class` (`number`,`letter_mark`)
VALUES 	(1,'v'),
		(2,'a');
	
		
INSERT INTO `pupil` (`pupil_id`,`class_id`,`first_name`,`pathronymic`,`last_name`)
VALUES 	(1,1,'v','v','v'),
		(2,2,'a','a','a');
		
INSERT INTO `school_journal_db`.`pupil` (`pupil_id`, `class_id`, `first_name`, `pathronymic`, `last_name`) VALUES ('8', '3', 'q', 'q', 'q');
INSERT INTO `school_journal_db`.`pupil` (`pupil_id`, `class_id`, `first_name`, `pathronymic`, `last_name`) VALUES ('9', '4', 'w', 'w', 'w');
		

INSERT INTO `school_journal_db`.`subject` (`name`, `description`) VALUES ('Биология', 'Наука о живой природе, о закономерностях органической жизни.');
INSERT INTO `school_journal_db`.`subject` (`name`, `description`) VALUES ('Математика', 'Наука о структурах, порядке и отношениях, исторически сложившаяся на основе операций подсчёта, измерения и описания формы объектов.');
INSERT INTO `school_journal_db`.`subject` (`name`, `description`) VALUES ('Физика', 'Область естествознания: наука о простейших и, вместе с тем, наиболее общих законах природы, о материи, её структуре и движении. Законы физики лежат в основе всего естествознания.');
INSERT INTO `school_journal_db`.`subject` (`name`, `description`) VALUES ('География', 'Объединенный под названием физическая география комплекс наук, изучающих географическую оболочку Земли. Основными объектами изучения географических наук являются геосферы (биосфера, атмосфера, литосфера, гидросфера и почвенный покров) и геосистемы (ландшафты, природные зоны, биогеоценозы…);');
INSERT INTO `school_journal_db`.`subject` (`name`, `description`) VALUES ('Химия', 'Одна из важнейших и обширных областей естествознания, наука о веществах, их составе и строении, их свойствах, зависящих от состава и строения, их превращениях, ведущих к изменению состава — химических реакциях, а также о законах и закономерностях, которым эти превращения подчиняются.');

INSERT INTO `school_journal_db`.`role` (`name`, `level`) VALUES ('TEACHER', '2');
INSERT INTO `school_journal_db`.`role` (`name`, `level`) VALUES ('PUPIL', '1');
INSERT INTO `school_journal_db`.`role` (`name`, `level`) VALUES ('CLASS_TEACHER', '3');
INSERT INTO `school_journal_db`.`role` (`name`, `level`) VALUES ('DIRECTOR_OF_STUDIES', '4');
INSERT INTO `school_journal_db`.`role` (`name`, `level`) VALUES ('DIRECTOR', '5');
INSERT INTO `school_journal_db`.`role` (`name`, `level`) VALUES ('ADMIN', '6');

/*УЧИТЕЛЯ*/

INSERT INTO `school_journal_db`.`user` (`role_id`, `username`, `pass_hash`, `locked`, `email`) VALUES ('2', 'q', 'q', '0', 'q');
INSERT INTO `school_journal_db`.`user` (`role_id`, `username`, `pass_hash`, `locked`, `email`) VALUES ('2', 'w', 'w', '0', 'w');
INSERT INTO `school_journal_db`.`user` (`role_id`, `username`, `pass_hash`, `locked`, `email`) VALUES ('2', 'e', 'e', '0', 'e');
INSERT INTO `school_journal_db`.`user` (`role_id`, `username`, `pass_hash`, `locked`, `email`) VALUES ('2', 'r', 'r', '0', 'r');
INSERT INTO `school_journal_db`.`user` (`role_id`, `username`, `pass_hash`, `locked`, `email`) VALUES ('2', 't', 't', '0', 't');


INSERT INTO `school_journal_db`.`teacher` (`teacher_id`, `phone_number`, `first_name`, `pathronymic`, `last_name`, `description`) VALUES ('3', '+375291111111', 'Ольга', 'Петровна', 'Иванова', 'Учитель предметов');
INSERT INTO `school_journal_db`.`teacher` (`teacher_id`, `phone_number`, `first_name`, `pathronymic`, `last_name`, `description`) VALUES ('4', '+375292222222', 'Александра', 'Игоревна', 'Петрова', 'Учитель школы со стажем');
INSERT INTO `school_journal_db`.`teacher` (`teacher_id`, `phone_number`, `first_name`, `pathronymic`, `last_name`, `description`) VALUES ('5', '+375293333333', 'Татьяна ', 'Ивановна', 'Сидорова', 'Учитель наверное');
INSERT INTO `school_journal_db`.`teacher` (`teacher_id`, `phone_number`, `first_name`, `pathronymic`, `last_name`, `description`) VALUES ('6', '+375294444444', 'Татьяна ', 'Николаевна', 'Кузнецова', 'Учитель школы');
INSERT INTO `school_journal_db`.`teacher` (`teacher_id`, `phone_number`, `first_name`, `pathronymic`, `last_name`, `description`) VALUES ('7', '+375295555555', 'Алла', 'Владимировна', 'Какая-то', 'Директор или нет');



/*insert into `mark` (`pupil_id`,`type`,`date`,`subject_id`)
INSERT INTO `pupil` (`pupil_id`,`class_id`,`first_name`,`pathronymic`,`last_name`)
VALUES 	(1,1,'v','v','v'),
		(2,2,'a','a','a');

insert into `subject` (`name`,`description`)
values ('math','mathematics'),('supermath','high math');

insert into `mark` (`pupil_id`,`type`,`date`,`subject_id`)
values 	(1,'simple',str_to_date('01-01-2017','%d-%m-%Y'),1),
		(1,'simple',str_to_date('01-01-2017','%d-%m-%Y'),2),
        (2,'simple',str_to_date('01-01-2017','%d-%m-%Y'),1),
        (2,'simple',str_to_date('01-01-2017','%d-%m-%Y'),2);
        */
        
INSERT INTO `school_journal_db`.`subject_in_schedule` (`subject_id`, `teacher_id`, `day_of_week`, `begin_time`, `place`, `class_id`) VALUES ('1', '3', '1', '08:00', 'Аудитория 3', '1');
INSERT INTO `school_journal_db`.`subject_in_schedule` (`subject_id`, `teacher_id`, `day_of_week`, `begin_time`, `place`, `class_id`) VALUES ('1', '3', '1', '8:55', 'Аудитория 3', '3');
INSERT INTO `school_journal_db`.`subject_in_schedule` (`subject_id`, `teacher_id`, `day_of_week`, `begin_time`, `place`, `class_id`) VALUES ('2', '4', '1', '8:00', 'Аудитория 6', '2');
INSERT INTO `school_journal_db`.`subject_in_schedule` (`subject_id`, `teacher_id`, `day_of_week`, `begin_time`, `place`, `class_id`) VALUES ('2', '4', '1', '8:55', 'Аудитория 6', '4');
INSERT INTO `school_journal_db`.`subject_in_schedule` (`subject_id`, `teacher_id`, `day_of_week`, `begin_time`, `place`, `class_id`) VALUES ('3', '5', '1', '8:00', 'Аудитория 7', '3');
INSERT INTO `school_journal_db`.`subject_in_schedule` (`subject_id`, `teacher_id`, `day_of_week`, `begin_time`, `place`, `class_id`) VALUES ('3', '5', '1', '8:55', 'Аудитория 7', '1');
INSERT INTO `school_journal_db`.`subject_in_schedule` (`subject_id`, `teacher_id`, `day_of_week`, `begin_time`, `place`, `class_id`) VALUES ('4', '6', '1', '8:00', 'Аудитория 8', '4');
INSERT INTO `school_journal_db`.`subject_in_schedule` (`subject_id`, `teacher_id`, `day_of_week`, `begin_time`, `place`, `class_id`) VALUES ('4', '6', '1', '8:55', 'Аудитория 8 ', '2');

