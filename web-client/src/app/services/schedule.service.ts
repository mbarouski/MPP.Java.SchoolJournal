import {Injectable, Inject} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {Subject, Observable} from 'rxjs';
import {Http, Headers, RequestOptions} from "@angular/http";
import {APP_CONFIG} from "../configs/app.config";
import {AuthService} from "./auth.service";
import {HttpUtil} from "./http.util";


@Injectable()
export class ScheduleService {

  constructor(@Inject(APP_CONFIG) private config: any, private http: Http, private httpUtil: HttpUtil){  }

  fetchSchedule(role) {
    return new Promise((resolve, reject) => {
      this.http.get(`${this.config.apiEndpoint}/schedule/${role}`, {search: this.httpUtil.createURLSearchParamsWithToken()})
        .map(res => {
          debugger;
          return res.json();
        })
        .subscribe((schedule) => {
          debugger;
          resolve(schedule);
        });
    });
  }

}
