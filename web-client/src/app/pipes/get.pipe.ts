import {Pipe, PipeTransform} from "@angular/core";
import * as _ from "lodash";

@Pipe({name: 'get'})
export class GetPipe implements PipeTransform {
  transform(value, args:string) : any {
    return _.get(value, `${args}`, undefined);
  }
}
