import { FormControl, FormGroup, Validators } from '@angular/forms';
import { DialogRegistrationComponent } from './../dialogs/dialog-registration/dialog-registration.component';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatSnackBar } from '@angular/material';
import { AuthenticationService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit {

  private loginForm: FormGroup;

  constructor(
    public dialog: MatDialog,
    private authService: AuthenticationService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required])
    });
  }

  ngOnInit() { }

  async onSubmit() {
    // console.log(await this.authService.login('', ''));
    // this.router.navigate([`/users/${this.authService.currentUser.id}/projects`]);

    // if (this.loginForm.valid) {
      const username = this.loginForm.get('username').value;
      const password = this.loginForm.get('password').value;

      try {
        await this.authService.login(username, password);
        // await this.authService.login('atesz', 'a');
        this.router.navigate([`/users/${this.authService.currentUser.id}/projects`]);
      } catch {
        this.snackBar.open('SIKERTELEN BEJELENTKEZÉS!', 'HIBA', { duration: 2000 });
      }
    // }
  }

  private openRegDialog(): void {
    const dialogRef = this.dialog.open(DialogRegistrationComponent, {
      width: '350px'
    });
  }
}
