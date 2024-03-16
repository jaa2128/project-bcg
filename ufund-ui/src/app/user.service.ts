import { Injectable } from '@angular/core';
import { Need } from './need'; 
import { User } from './user'; 
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  currentUser: User | null;

  constructor(private http: HttpClient,
    private router: Router) {
    this.currentUser = null;
   }

  private usersURL = 'http://localhost:8080/users';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  getCurrentUser(): User | null {
    return this.currentUser;
  }

  setCurrentUser(user: User | null): void {
    this.currentUser = user;
  }

  validate(): void {
    if(this.currentUser == null) {
      this.router.navigateByUrl("login");
    }
  }

  getUser(username: string, password: string): Observable<HttpResponse<User>> {
    return this.http.get<User>(this.usersURL + '/' + username + "?password=" + password, { observe: 'response' });
  }

  createUser(username: string, password: string): Observable<HttpResponse<User>> {
    return this.http.post<User>(this.usersURL + '/' + username, password, { observe: 'response' });
  }
}
