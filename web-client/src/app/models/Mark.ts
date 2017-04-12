export class Mark {
  markId: number;
  value: number;
  type: string;
  date: string;
  pupilId: number;
  subjectId: number;
  teacherId: number;

  constructor(id) {
    this.markId = id;
  }
}
