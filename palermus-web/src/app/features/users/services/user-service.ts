import { Injectable, inject } from '@angular/core';
import {environment} from '../../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {RegisterUser} from '../models/user/registerUser';
import {Observable} from 'rxjs';
import {ReferenceUser} from '../models/user/referenceUser';
import {ReferenceProfile} from '../models/profile/ReferenceProfile';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;
  private profileUrl = `${environment.apiUrl}/profiles`;

  private http = inject(HttpClient);

  addUser(user: RegisterUser): Observable<ReferenceUser>{
    return this.http.post<ReferenceUser>(this.apiUrl, user);
  }

  getProfileByUsername(username: string): Observable<ReferenceProfile> {
    return this.http.get<ReferenceProfile>(`${this.profileUrl}/username/${username}`);
  }

  updateProfileEmail(profileId: number, email: string): Observable<ReferenceProfile> {
    return this.http.put<ReferenceProfile>(`${this.profileUrl}/email/${profileId}`, { email });
  }

  updateProfileUsername(profileId: number, username: string): Observable<ReferenceProfile> {
    return this.http.put<ReferenceProfile>(`${this.profileUrl}/username/${profileId}`, { username });
  }

  updateProfilePassword(profileId: number, payload: { currentPassword?: string, password?: string }): Observable<any> {
    return this.http.put(`${this.profileUrl}/palermuspass/${profileId}`, payload);
  }
}
