export class User {
  userId: number;
  username: string;
  password: string;
  locked: number;
  email: string;
  roleId: number;

  constructor(id: number) {
    this.userId = id;
  }
}
