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

  createNeed(name:string, description: string, type: string, targetQuantity: number){
    return this.http.post<Need>(this.needsURL, String.raw`{"name": "`+name+`", "description": "`+description+`", "type": "`+type+`", "targetQuantity": "`+targetQuantity+`}`, this.httpOptions)
  }

  updateNeed(need: Need): Observable<any> {
    return this.http.put(this.needsURL, need, this.httpOptions);
  }

}
