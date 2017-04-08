import {Pipe, PipeTransform} from "@angular/core";
import DAYS from './../components/constants/schedule.constants';

@Pipe({name: 'dayTimePipe'})
export class DayTimePipe implements PipeTransform {
  transform(value, args:string[]) : any {
    let keys = [];
    DAYS.forEach(day => {
      value.forEach(time => {
        keys.push({ day, time });
      });
    });
    return keys;
  }
}
