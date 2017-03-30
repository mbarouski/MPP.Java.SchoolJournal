import {Injectable, Inject} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {Subject, Observable} from 'rxjs';
import {Http, Headers, RequestOptions} from "@angular/http";
import {APP_CONFIG} from "../configs/app.config";
import {LoginData} from "../models/LoginData";
import {Token} from "../models/Token";

@Injectable()
export class AuthService {
  private AUTH_ROUTE: string = `${this.config.apiEndpoint}/auth`;

  private token: string;
  private _user: any;

  get user() {
    return this._user;
  }

  userSubject: ReplaySubject<any> = new ReplaySubject(1);

  constructor(@Inject(APP_CONFIG) private config: any, private http: Http){}

  login(loginData: LoginData) {
    return new Promise((resolve, reject) => {
      let headers = new Headers({
        'Content-Type': 'application/json',
        'Authorization': this.token || ''
      });
      let options = new RequestOptions({ headers: headers });

      return this.http.post(`${this.AUTH_ROUTE}/login`, loginData, options)
        .map(res => {
          return res.json();
        })
        .subscribe((user) => {
          this.token = user.token;
          this._user = user;
          this.userSubject.next(this._user);
          resolve();
        });
    });
  }

  logout() {
    return new Promise((resolve, reject) => {
      let headers = new Headers({
        'Content-Type': 'application/json',
        'Authorization': this.token || ''
      });
      let options = new RequestOptions({ headers: headers });

      this.http.post(`${this.AUTH_ROUTE}/logout`, {}, options)
        .map(res => res.json())
        .subscribe(() => {
          this.token = '';
          this._user = null;
          this.userSubject.next(this._user);
          resolve();
        });

    });
  }

}
