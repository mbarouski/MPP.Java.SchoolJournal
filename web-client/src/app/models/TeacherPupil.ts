export class TeacherPupil {
  userId: number;
  firstName: string;
  pathronymic: string;
  lastName: string;
  phoneNumber: string;
  characteristic: string;
  description: string;
  classId: number;

  constructor(id: number) {
    this.userId = id;
  }
}
