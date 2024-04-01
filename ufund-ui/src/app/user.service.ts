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

  logOut(): void {
    this.setCurrentUser(null);
    this.router.navigateByUrl("login");
  }

  validate(): void {
    if(this.currentUser == null) {
      this.router.navigateByUrl("login");
    }
  }

  getUser(username: string, password: string): Observable<HttpResponse<User>> {
    return this.http.get<User>(this.usersURL + '/' + username + "?password=" + password, { observe: 'response' });
  }

  getUserNeeds(username: string): Observable<HttpResponse<number[]>>{
    return this.http.get<number[]>(this.usersURL + '/' + username + '/needs', { observe: 'response'});
  }

  getUserContributions(username: string): Observable<HttpResponse<number[]>>{
    return this.http.get<number[]>(this.usersURL + '/' + username + '/contributions', { observe: 'response'});
  }

  createUser(username: string, password: string): Observable<HttpResponse<User>> {
    return this.http.post<User>(this.usersURL + '/' + username, password, { observe: 'response' });
  }

  addNeed(id: number, quantity: number): Observable<HttpResponse<number>> {
    return this.http.put<number>(this.usersURL + "/" + this.currentUser?.username + "/" + id + "/" + quantity, null, { observe: 'response'});
  }
  
  editNeed(id: number, quantity: number): Observable<HttpResponse<number>> {
    return this.http.put<number>(this.usersURL + "/" + this.currentUser?.username + "/" + id, quantity, { observe: 'response'});
  }

  removeNeed(id: number): Observable<HttpResponse<any>> {
    return this.http.delete(this.usersURL + "/" + this.currentUser?.username + "/" + id, { observe: 'response'});
  }

  clearBasket(username: string): Observable<HttpResponse<any>> {
    return this.http.delete(this.usersURL + "/" + username + "/clear", { observe: 'response'} );
  }

  changePassword(username: string, newPassword: string): Observable<HttpResponse<any>> {
    const url = `${this.usersURL}/${username}/newPassword/${newPassword}`;
    return this.http.put(url, {}, { observe: 'response' });
  } 

  changeUsername(username: string, newUsername: string): Observable<HttpResponse<any>> {
    const url = `${this.usersURL}/${username}/newUsername/${newUsername}`;
    return this.http.put(url, {}, { observe: 'response' });
  } 
}
