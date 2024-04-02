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
    headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' })
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

  searchNeeds(keyword: string, type: string, min: string, max: string, availability: Boolean[]): Observable<Need[]>{
    var sun = availability[0];
    var mon = availability[1];
    var tues = availability[2];
    var wed = availability[3];
    var thurs = availability[4];
    var fri = availability[5];
    var sat = availability[6];
     return this.http.get<Need[]>(this.needsURL + '/?name=' +keyword+ '&type=' +type+ '&min=' +min+ '&max=' +max+ '&sun=' +sun+ '&mon=' +mon+ '&tues=' +tues+ '&wed=' +wed+ '&thurs=' +thurs+ '&fri=' +fri+ '&sat=' +sat);
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
