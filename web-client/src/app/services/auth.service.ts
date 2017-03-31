import {Injectable, Inject} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {Subject, Observable} from 'rxjs';
import {Http, Headers, RequestOptions} from "@angular/http";
import {APP_CONFIG} from "../configs/app.config";
import {LoginData} from "../models/LoginData";
import {Token} from "../models/Token";
import {HttpUtil} from "./http.util";

@Injectable()
export class AuthService {
  private AUTH_ROUTE: string = `${this.config.apiEndpoint}/auth`;

  private _token: string;
  private _user: any;

  get token() {
    return this._token;
  }

  get user() {
    return this._user;
  }

  userSubject: ReplaySubject<any> = new ReplaySubject(1);

  constructor(@Inject(APP_CONFIG) private config: any, private http: Http){}

  login(loginData: LoginData) {
    return new Promise((resolve, reject) => {

      return this.http.post(`${this.AUTH_ROUTE}/login`, loginData, HttpUtil.REQUEST_OPTIONS_WITH_CONTENT_TYPE_JSON)
        .map(res => {
          return res.json();
        })
        .subscribe((user) => {
          this._token = user.value;
          this._user = user;
          this.userSubject.next(this._user);
          resolve();
        });
    });
  }

  logout() {
    return new Promise((resolve, reject) => {
      this.http.post(`${this.AUTH_ROUTE}/logout?token=${this.token}`, null, HttpUtil.REQUEST_OPTIONS_WITH_CONTENT_TYPE_JSON)
        .map(res => {
          if(res.status != 200) reject();
          res.json();
        })
        .subscribe((data) => {
          this._token = '';
          this._user = null;
          this.userSubject.next(this._user);
          resolve();
        });

    });
  }

}
