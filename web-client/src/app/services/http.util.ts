import {RequestOptions, Headers, URLSearchParams} from "@angular/http";
import {AuthService} from "./auth.service";
import {Injectable} from "@angular/core";

@Injectable()
export class HttpUtil {

  constructor(private authService: AuthService){}

  static REQUEST_OPTIONS_WITH_CONTENT_TYPE_JSON = new RequestOptions({
    headers: new Headers({
      'Content-Type': 'application/json'
    })
  });

  createURLSearchParamsWithToken() {
    var params = new URLSearchParams();
    debugger;
    params.append('token', this.authService.token);
    return params;
  }
}
