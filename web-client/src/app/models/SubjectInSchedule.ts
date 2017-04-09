

export class SubjectInSchedule {
  subectInSchedule: number;
  dayOfWeek: string;
  beginTime: string;
  place: string;
  subjectId: number;
  teacherId: number;
  clazzId: number;

  constructor(id: number) {
    this.subectInSchedule = id;
  }
}
