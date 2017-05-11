import {Injectable, Inject} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {Subject, Observable} from 'rxjs';
import {Http, Headers, RequestOptions, Response} from "@angular/http";
import {APP_CONFIG} from "../configs/app.config";
import {LoginData} from "../models/LoginData";
import {Token} from "../models/Token";
import {HttpUtil} from "./http.util";
import {AuthService} from "./auth.service";

@Injectable()
export class DocsService {

  constructor(@Inject(APP_CONFIG) private config: any,
              private authService: AuthService,
              private http: Http) {}

  download(link, type) {
    window.open(`${this.config.apiEndpoint}/docs${link}?token=${this.authService.token}`, '_blank');
  }
}
