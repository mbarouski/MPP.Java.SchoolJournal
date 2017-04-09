import {RequestOptions, Headers, URLSearchParams} from "@angular/http";
import {AuthService} from "./auth.service";
import {Injectable} from "@angular/core";

export class HttpUtil {

  static REQUEST_OPTIONS_WITH_CONTENT_TYPE_JSON = new RequestOptions({
    headers: new Headers({
      'Content-Type': 'application/json'
    })
  });
}
