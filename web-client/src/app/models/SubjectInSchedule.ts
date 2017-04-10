

export class SubjectInSchedule {
  subectInScheduleId: number;
  dayOfWeek: string;
  time: string;
  beginTime: string;
  place: string;
  subjectId: number;
  teacherId: number;
  clazzId: number;

  constructor(id: number) {
    this.subectInScheduleId = id;
  }
}
