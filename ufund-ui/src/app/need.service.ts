import { Injectable } from '@angular/core';
import { Need } from './need'; 
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
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

  createNeed(need:Need): Observable<Need>{
    return this.http.post<Need>(this.needsURL, need, this.httpOptions);
  }

  searchNeeds(keyword: string, type: string, min: string, max: string): Observable<Need[]>{
     return this.http.get<Need[]>(this.needsURL + '/?name=' +keyword+ '&type=' +type+ '&min=' +min+ '&max=' +max)
  }
  

  updateNeed(need: Need): Observable<any> {
    return this.http.put(this.needsURL, need, this.httpOptions);
  }

  /** DELETE: delete the need from the server */
  deleteNeed(id: number): Observable<Need> {
    const url = `${this.needsURL}/${id}`;

    return this.http.delete<Need>(url, this.httpOptions);
  }

  contribute(id: number, quantity: number): Observable<HttpResponse<Need>> {
    return this.http.put<Need>(this.needsURL + "/" + id, quantity, { observe: 'response'});
  }

}
