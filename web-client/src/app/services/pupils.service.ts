import {Injectable, Inject} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {Subject, Observable} from 'rxjs';
import {Http} from "@angular/http";
import {APP_CONFIG} from "../configs/app.config";


@Injectable()
export class PupilsService {

  constructor(@Inject(APP_CONFIG) private config: any, private http: Http){

  }

  fetchPupil(pupilId) {
    return new Promise((resolve, reject) => {
      this.http.get(`${this.config.apiEndpoint}/pupils/${pupilId}`)
        .map(res => {
          return res.json();
        })
        .subscribe((pupil) => {
          resolve(pupil);
        });
    });
  }

}
