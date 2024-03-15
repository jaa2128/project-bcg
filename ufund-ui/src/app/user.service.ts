import { Injectable } from '@angular/core';
import { Need } from './need'; 
import { User } from './user'; 
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient,) { }

  private usersURL = 'http://localhost:8080/users';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  getUser(username: string, password: string): Observable<HttpResponse<User>> {
    return this.http.get<User>(this.usersURL + '/' + username + "?password=" + password, { observe: 'response' });
  }

  createUser(username: string, password: string): Observable<HttpResponse<User>> {
    return this.http.post<User>(this.usersURL + '/' + username, password, { observe: 'response' });
  }
}
