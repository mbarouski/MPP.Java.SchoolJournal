import {Pipe, PipeTransform} from "@angular/core";
import DAYS from './../components/constants/schedule.constants';
import {SchoolInfoService} from "../services/school-info.service";
import {TeachersService} from "../services/teachers.service";

@Pipe({name: 'dayTimePipe'})
export class DayTimePipe implements PipeTransform {
  constructor(private schoolInfoService: SchoolInfoService) {}

  transform(value, args:string[]) : any {
    let keys = [];
    DAYS.forEach(day => {
      this.schoolInfoService.timesForSubjects.forEach(time => {
        keys.push({ day, time });
      });
    });
    return keys;
  }
}
