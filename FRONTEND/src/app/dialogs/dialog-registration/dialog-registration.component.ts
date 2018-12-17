import { Component, OnInit } from '@angular/core';
import { MatDialogRef, MatSnackBar } from '@angular/material';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/classes/user';

export interface RegistrationData {
  name: string;
  email: string;
}

@Component({
  selector: 'app-dialog-registration',
  templateUrl: './dialog-registration.component.html',
  styleUrls: [
    '../common-dialog-styles.scss',
    './dialog-registration.component.scss'
  ]
})
export class DialogRegistrationComponent implements OnInit {

  private name: string;
  private userName: string;
  private passwd: string;
  private passwd2: string;

  constructor(
    private dialogRef: MatDialogRef<DialogRegistrationComponent>,
    private snackBar: MatSnackBar,
    private userService: UserService
  ) { }

  ngOnInit() {
    this.name = this.userName = this.passwd = this.passwd2 = '';
  }

  private async validateRegForm() {

    if (!this.name.length || !this.userName.length || !this.passwd.length) {
      this.snackBar.open('Minden adatot meg kell adni!', 'HIBA', { duration: 2000 });
      return;
    } else if (this.passwd !== this.passwd2) {
      this.snackBar.open('A két jelszó nem egyezik meg!', 'HIBA', { duration: 2000 });
      return;
    }

    const _user = new User(this.name, this.userName, this.passwd);

    try {
      await this.userService.registerUser(_user);
      this.snackBar.open('Sikeres regisztráció.', '', { duration: 2000 });
      this.dialogRef.close();
    } catch {
      this.snackBar.open('Sikertelen regisztráció!', 'HIBA', { duration: 2000 });
    }
  }
}
