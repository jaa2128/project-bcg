import { Injectable } from '@angular/core';
import { Need } from './need'; 
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class NeedService {

  constructor(private http: HttpClient,) { }

  private needsURL = 'localhost:8080';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  getNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(this.needsURL);
  }

}
