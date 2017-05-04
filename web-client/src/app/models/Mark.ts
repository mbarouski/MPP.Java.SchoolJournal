export class Mark {
  markId: number;
  value: number;
  type: string;
  date: string;
  pupilId: number;
  subjectId: number;
  teacherId: number;
  termNumber: number;

  constructor(id) {
    this.markId = id;
  }
}
