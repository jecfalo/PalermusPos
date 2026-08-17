import { Injectable, inject } from '@angular/core';
import {environment} from '../../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ReferenceProfile} from '../models/profile/ReferenceProfile';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private apiUrl = `${environment.apiUrl}/profiles`;

  private http = inject(HttpClient);

  getProfileByDocument(document: string): Observable<ReferenceProfile>{
    return this.http.get<ReferenceProfile>(`${this.apiUrl}/document/${document}`);
  }
  getProfileByUsername(username: string): Observable<ReferenceProfile>{
    return this.http.get<ReferenceProfile>(`${this.apiUrl}/username/${username}`);
  }
}
