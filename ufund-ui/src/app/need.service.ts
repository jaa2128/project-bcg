import { Injectable } from '@angular/core';
import { Need } from './need'; 
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class NeedService {

  constructor(private http: HttpClient,) { }

  private needsURL = 'http://localhost:8080/needs';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  

  getNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(this.needsURL);
  }

  getNeed(id: number): Observable<Need> {
    return this.http.get<Need>(this.needsURL + '/' + id);
  }

  searchNeeds(containsText: string): Observable<Need[]>{
    return this.http.get<Need[]>(this.needsURL + '/?name=' +containsText)
  }

  updateNeed(need: Need): Observable<any> {
    return this.http.put(this.needsURL, need, this.httpOptions);
  }

}
