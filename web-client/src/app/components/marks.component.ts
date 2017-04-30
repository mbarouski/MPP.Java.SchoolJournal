import {Component, AfterViewInit, OnInit, ViewChild} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router, Params, ActivatedRoute} from "@angular/router";
import {MarksService} from "../services/marks.service";
import {PupilsService} from "../services/pupils.service";
import {TeachersService} from "../services/teachers.service";
import {ReplaySubject} from "rxjs";
import {SubjectsService} from "../services/subjects.service";
import {ScheduleService} from "../services/schedule.service";
import {Mark} from "../models/Mark";
import {SchoolInfoService} from "../services/school-info.service";
import MARK_TYPES from "./constants/marks.constants";
import {ModalDirective} from "ng2-bootstrap";
import {ContextMenuComponent, ContextMenuService} from "angular2-contextmenu";

declare let moment: any;

@Component({
  moduleId: module.id,
  selector: 'schedule-component',
  templateUrl: './templates/marks.component.html',
  styleUrls: ['./styles/marks.component.css']
})
export class MarksComponent implements OnInit, AfterViewInit{
  marks = [];
  subject: any = null;
  subjects = [];
  pupils = [];

  currentMarkType = '';
  currentMark: Mark;
  times = [];
  lessons = [];
  term: any;
  terms = [];
  currentTerm: number;
  markTypes = MARK_TYPES;
  selectedDate: any;

  datePickerOptions = {
    dateFormat: 'yyyy-mm-dd',
  };

  @ViewChild('markModal') public markModal: ModalDirective;
  @ViewChild(ContextMenuComponent) public deleteMenu: ContextMenuComponent;

  errorMessage = '';

  validationError = Object.assign({}, ...[
    'type',
    'value',
    'pupil',
    'date'
  ].map((field) => {
    return {
      [field]: {
        status: false,
        message: '',
      },
    };
  }));

  constructor(private authService: AuthService,
              private router: Router,
              private marksService: MarksService,
              private pupilsService: PupilsService,
              private scheduleService: ScheduleService,
              private activatedRoute: ActivatedRoute,
              private schoolInfoService: SchoolInfoService,
              private contextMenuService: ContextMenuService) {
    marksService.marksSubject.subscribe(marks => {
      this.marks = marks;
      if(!this.subject) {
        this.activatedRoute.params.subscribe((params: Params) => {
          if(params['id']) {
            this.scheduleService.fetchSubject(params['id'])
              .then((subject: any) => {
                this.subject = subject;
                this.pupilsService.fetchPupilsByClass(subject.clazz.classId)
                  .then((pupils: any) => {
                    this.pupils = pupils;
                  })
                  .catch((err) => {
                    if(err.status === 500) {
                      this.errorMessage = 'Извините, ошибка на сервере';
                    } else {
                      this.errorMessage = err._body;
                    }
                  });
                this.marksService.fetchMarksForSubjectInClass(subject.subject.subjectId, subject.clazz.classId);
              })
              .catch((err) => {
                if(err.status === 500) {
                  this.errorMessage = 'Извините, ошибка на сервере';
                } else {
                  this.errorMessage = err._body;
                }
              });
          }
        });
        return;
      }
      scheduleService.getSubjectsForTeacherClassSubject(this.subject.teacher.userId,
        this.subject.clazz.classId,
        this.subject.subject.subjectId)
        .then((subjects: any) => {
          this.subjects = subjects;
          this.generateLessons();
        })
        .catch((err) => {
          if(err.status === 500) {
            this.errorMessage = 'Извините, ошибка на сервере';
          } else {
            this.errorMessage = err._body;
          }
        });
    });
    schoolInfoService.timesSubject.subscribe(times => {
      this.times = times;
    });
    schoolInfoService.termSubject.subscribe(term => {
      this.term = term;
      this.currentTerm = term.number;
    });
    schoolInfoService.termsSubject.subscribe(terms => {
      this.terms = terms;
    });
    schoolInfoService.fetchLessonTimes();
    schoolInfoService.fetchTerms();
  }

  generateLessons() {
    let lessonDates = [];
    let date = moment(this.term.start);
    let endDate = moment(this.term.end);
    let days = this.subjects.map(subject => subject.dayOfWeek);
    while(!date.isAfter(endDate)) {
      let day = date.format('ddd').toLowerCase();
      if(days.includes(day)) {
        lessonDates.push(date.format('DD.MM'));
      }
      date = date.add(1, 'days');
    }
    this.lessons = lessonDates;
  }

  ngOnInit() {
    this.activatedRoute.params.subscribe((params: Params) => {
      if(params['id']) {
        this.scheduleService.fetchSubject(params['id'])
          .then((subject: any) => {
            this.subject = subject;
            this.pupilsService.fetchPupilsByClass(subject.clazz.classId)
              .then((pupils: any) => {
                this.pupils = pupils;
              })
              .catch((err) => {
                if(err.status === 500) {
                  this.errorMessage = 'Извините, ошибка на сервере';
                } else {
                  this.errorMessage = err._body;
                }
              });
            this.marksService.fetchMarksForSubjectInClass(subject.subject.subjectId, subject.clazz.classId);
          })
          .catch((err) => {
            if(err.status === 500) {
              this.errorMessage = 'Извините, ошибка на сервере';
            } else {
              this.errorMessage = err._body;
            }
          });
      }
    });
  }

  ngAfterViewInit() {
    // this.activatedRoute.params.subscribe((params: Params) => {
    //   if(params['id']) {
    //     debugger;
    //     this.scheduleService.fetchSubject(params['id'])
    //       .then((subject: any) => {
    //         this.subject = subject;
    //         this.pupilsService.fetchPupilsByClass(subject.clazz.classId)
    //           .then((pupils: any) => {
    //             this.pupils = pupils;
    //           });
    //         this.marksService.fetchMarksForSubjectInClass(subject.subject.subjectId, subject.clazz.classId);
    //       });
    //   }
    // });
  }

  getMarkForLessonAndPupil(date, pupilId) {
    let mark = this.marks.find(mark => {
      return mark.pupil.userId == pupilId &&
          moment(mark.date).format('DD.MM') === date;
    });
    mark = mark ? mark : '';
    return mark;
  }

  openMarkModal() {
    this.markModal.show();
  }

  getPupilByFullName(fullname) {
    return this.pupils.find(pupil => {
      return this.pupilsService.getPupilFullName(pupil) === fullname;
    });
  }

  cellForEdit: any;

  getMarkForTermAndPupil(term, pupilId) {
    if(!this.marks.length) return;
    if(!this.terms.length) return;
    return this.marks.find(m => m.pupil.userId == +pupilId && m.type === 'term'
      && moment(m.date).isBetween(moment(this.terms[term-1].start), moment(this.terms[term-1].end)));
  }

  getMarkForYearAndPupil(pupilId) {
    return this.marks.find(m => m.pupilId == +pupilId && m.type === 'year');
  }

  getDate(td) {
    const index = td.parent().index();
    let date = this.lessons[index - 1];
    if(!date) {
      const text = td.parent().parent().parent().prev().children().first().children()[index].textContent;
      if(text === 'Год') {
        date = this.terms[this.terms.length - 1].end;
      } else {
        const term_number = text.split(' ')[0];
        date = this.terms[(+term_number) - 1].end;
      }
      date = moment(date, "YYYY-MM-DD").format("DD.MM.YYYY")
    } else {
      date = `${date}.${this.terms[this.currentTerm - 1].start.split('-')[0]}`
    }
    return date;
  }

  setCurrentMark(mark, event, initType) {
    if(!['director', 'director_of_studies', 'teacher'].includes(this.authService.role)) return;
    if(mark) return;
    this.cellForEdit = $(event.currentTarget);
    let currentMark = new Mark(0);
    currentMark.subjectId = this.subject.subject.subjectId;
    currentMark.teacherId = this.subject.teacher.userId;
    const date = this.getDate(this.cellForEdit);
    currentMark.date = date;
    currentMark.pupilId = this.getPupilByFullName($(event.currentTarget.parentElement.parentElement).children()[0].innerText.trim()).userId;
    if(initType) {
      currentMark.type = initType;
      this.currentMarkType = initType;
    }
    this.currentMark = currentMark;
    this.openMarkModal();
  }

  validateMarkInfo() {
    const date = moment(this.selectedDate);
    const value = this.currentMark.value;
    const pupilId = this.currentMark.pupilId;
    const type = this.currentMark.type;
    if(!type) {
      this.validationError.type.status = true;
      this.validationError.type.message = 'Выберите тип оценки';
    } else if(!this.markTypes.find(t => t.short === type)) {
      this.validationError.type.status = true;
      this.validationError.type.message = 'Некорректный тип оценки';
    } else {
      this.validationError.type.status = false;
    }
    if(!pupilId) {
      this.validationError.pupil.status = true;
      this.validationError.pupil.message = 'Выберите ученика';
    } else if(!this.pupils.find(p => p.userId === pupilId)) {
      this.validationError.pupil.status = true;
      this.validationError.pupil.message = 'Ученик не найден';
    } else {
      this.validationError.pupil.status = false;
    }
    if(type === 'apsent' && value) {
      this.validationError.value.status = true;
      this.validationError.value.message = 'При данном типе оценки значение не требуется';
    } else if(!type && type ! == 'apsent') {
      this.validationError.value.status = true;
      this.validationError.value.message = 'Установите значение оценки';
    } else {
      this.validationError.value.status = false;
    }
  }

  isMarkInfoValid() {
    return !this.validationError.type.status &&
      !this.validationError.value.status &&
      !this.validationError.pupil.status &&
      !this.validationError.date.status;
  }

  submitMarkForm() {
    this.validateMarkInfo();
    if(!this.isMarkInfoValid()) return;
    this.currentMark.date = moment(`${this.currentMark.date}`, 'DD-MM-YYYY').format('YYYY-MM-DD');
    this.marksService.setMark(this.currentMark)
      .then((mark: any) => {
        this.closeMarkModal();
        this.currentMarkType = '';
        this.currentMark = null;
        this.marks.push(mark);
        // this.cellForEdit.text(mark.value);
        // this.cellForEdit.addClass(this.generateClass(mark));
      })
      .catch((err) => {
        if(err.status === 500) {
          this.errorMessage = 'Извините, ошибка на сервере';
        } else {
          this.errorMessage = err._body;
        }
      });
  }

  generateClass(mark) {
    if(!mark || mark.type === 'simple') return '';
    return `${mark.type}-mark`;
  }

  decorateMark(mark) {
    if(!mark) return '';
    switch(mark.type) {
      case 'apsent':
        return 'Н';
      default:
        return mark.value;
    }
  }

  closeMarkModal() {
    this.markModal.hide();
    this.currentMarkType = '';
  }

  openContextMenu(event, markId) {
    if(!markId) return;
    this.cellForEdit = $(event.currentTarget);
    let mark = this.marks.find(mark => mark.markId == markId);
    this.contextMenuService.show.next({
      contextMenu: this.deleteMenu,
      event,
      item: mark ? mark : {}
    });
    event.preventDefault();
  }

  deleteMark(event) {
    this.marksService.deleteMark(event.item.markId)
      .then(() => {
        this.cellForEdit.text('');
        const index = this.marks.indexOf(this.marks.find(mark => mark.markId == event.item.markId));
        this.marks.splice(index, 1);
      })
      .catch((err) => {
        if(err.status === 500) {
          this.errorMessage = 'Извините, ошибка на сервере';
        } else {
          this.errorMessage = err._body;
        }
      });
  }

}
