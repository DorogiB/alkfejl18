import { User } from 'src/app/classes/user';
import { HttpService } from './http.service';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  public currentUser: User = null;

  constructor(
    private httpService: HttpService,
    private router: Router
  ) { }

  public async login(username: string, password: string): Promise<User> {
    try {
      const token = btoa(`${username}:${password}`);
      window.localStorage.setItem('token', token);
      this.currentUser = await this.httpService.post<User>('users/login', username);
      // this.currentUser = await this.httpService.get<User>('users/1');
      return Promise.resolve(this.currentUser);
    } catch (e) {
      console.log('LOGIN ERROR:', e);
      this.currentUser = null;
      window.localStorage.setItem('token', '');
      return Promise.reject();
    }
  }

  public logout() {
    this.currentUser = null;
    window.localStorage.setItem('token', '');
    this.router.navigate(['']);
  }
}
