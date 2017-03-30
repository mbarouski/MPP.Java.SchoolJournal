export class LoginData {
  constructor(
    public username: string,
    public password: string,
  ) {  }

  isValid() {
    return true;
  }
}
