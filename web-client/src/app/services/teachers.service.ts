import {Injectable, Inject} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {Subject, Observable} from 'rxjs';
import {Http, Headers, RequestOptions, URLSearchParams} from "@angular/http";
import {APP_CONFIG} from "../configs/app.config";
import {AuthService} from "./auth.service";
import {HttpUtil} from "./http.util";

const _ = require('lodash');

@Injectable()
export class TeachersService {

  teachers = [];
  teachersSubject: ReplaySubject<any> = new ReplaySubject(1);

  constructor(@Inject(APP_CONFIG) private config: any, private http: Http, private authService: AuthService){
    this.fetchTeachers();
  }

  fetchTeachers(){
    let params = new URLSearchParams();
    params.append('token', this.authService.token);
    return this.http.get(`${this.config.apiEndpoint}/teachers/`, {search: params})
      .map(res => {
        return res.json();
      })
      .catch((err) => {
        this.teachersSubject.next([]);
        return Observable.throw(err);
      })
      .subscribe((teachers) => {
        teachers = _.sortBy(teachers, (teacher) => this.getTeacherFullName(teacher));
        this.teachers = teachers;
        this.teachersSubject.next(teachers);
      });
  }

  fetchTeacher(teacherId) {
    let params = new URLSearchParams();
    params.append('token', this.authService.token);
    return new Promise((resolve, reject) => {
      return this.http.get(`${this.config.apiEndpoint}/teachers/${teacherId}`, {search: params})
        .map(res => {
          return res.json();
        })
        .catch((err) => {
          reject(err);
          return Observable.throw(err);
        })
        .subscribe((teacher) => {
          resolve(teacher);
        });
    });
  }

  getTeacherByFullName(fullName) {
    return this.teachers.find((teacher) => {
      return this.getTeacherFullName(teacher) === fullName;
    });
  }

  getTeacherFullName(teacher): string {
    return teacher ? `${teacher.lastName} ${teacher.firstName} ${teacher.pathronymic}` : '';
  }

  getTeacherNameList() {
    return this.teachers.map(teacher => {
      return this.getTeacherFullName(teacher);
    });
  }

  addTeacher(teacher) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.post(`${this.config.apiEndpoint}/teachers`, teacher, {search: params})
        .map(res => res.json())
        .catch((err) => {
          reject(err);
          return Observable.throw(err);
        })
        .subscribe((teacher) => {
          resolve(teacher);
        });
    });
  }

  updateTeacher(teacher) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.put(`${this.config.apiEndpoint}/teachers`, teacher, {search: params})
        .map(res => res.json())
        .catch((err) => {
          reject(err);
          return Observable.throw(err);
        })
        .subscribe((teacher) => {
          resolve(teacher);
        });
    });
  }

  deleteTeacher(id) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      this.http.delete(`${this.config.apiEndpoint}/teachers/${id}`, {search: params})
        .catch((err) => {
          reject(err);
          return Observable.throw(err);
        })
        .subscribe((data) => {
          resolve({});
        });
    });
  }

  setAsClassTeacher(teacherId, classId) {
    return new Promise((resolve, reject) => {
      let params = new URLSearchParams();
      params.append('token', this.authService.token);
      params.append('teacherId', teacherId);
      params.append('classId', classId);
      this.http.post(`${this.config.apiEndpoint}/teachers/changeClassOfTeacher`, {}, {search: params})
        .map(res => res.json())
        .catch((err) => {
          reject(err);
          return Observable.throw(err);
        })
        .subscribe((data) => {
          resolve({});
        });
    });
  }
}
