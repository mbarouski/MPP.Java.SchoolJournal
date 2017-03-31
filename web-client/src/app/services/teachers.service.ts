import {Injectable, Inject} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {Subject, Observable} from 'rxjs';
import {Http, Headers, RequestOptions} from "@angular/http";
import {APP_CONFIG} from "../configs/app.config";
import {AuthService} from "./auth.service";
import {HttpUtil} from "./http.util";


@Injectable()
export class TeachersService {

  teachersSubject: ReplaySubject<any> = new ReplaySubject(1);

  constructor(@Inject(APP_CONFIG) private config: any, private http: Http, private httpUtil: HttpUtil){  }

  fetchTeachers(){
    return this.http.get(`${this.config.apiEndpoint}/teachers`, this.httpUtil.createURLSearchParamsWithToken())
      .map(res => {
        return res.json();
      })
      .subscribe((teachers) => {
        this.teachersSubject.next(teachers);
      });
  }

  fetchTeacher(teacherId) {
    return new Promise((resolve, reject) => {
      this.http.get(`${this.config.apiEndpoint}/teachers/${teacherId}`, {search: this.httpUtil.createURLSearchParamsWithToken()})
        .map(res => {
          return res.json();
        })
        .subscribe((teacher) => {
          resolve(teacher);
        });
    });
  }

}
