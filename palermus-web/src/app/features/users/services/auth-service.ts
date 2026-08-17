import { Injectable, inject } from '@angular/core';
import {environment} from '../../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {jwtDecode, JwtPayload} from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}`;
  private  tokenKey: string = 'tokenKey';

  private http = inject(HttpClient);
  private router = inject(Router);
  
  login(username: string, password: string) {
    return this.http.post<{token: string}>(environment.apiUrl + '/login', {username, password})
  }
  saveToken(token: string) {
    localStorage.setItem(this.tokenKey, token);
  }
  getToken() : string | null{
    return localStorage.getItem(this.tokenKey);
  }
  logout():void {
    localStorage.removeItem(this.tokenKey);
    this.router.navigate(['/login']);
  }
  isLooggedIn():boolean{
    return !!this.getToken();
  }
  isTokenValid(): boolean{
    const token = this.getToken();
    if(!token) return false;
    try {
      const decoded: JwtPayload = jwtDecode(token);
      if(!decoded.exp){
        return false;
      }
      const expirationDate: number = decoded.exp * 1000;
      const now = new Date().getTime();
      return expirationDate > now;
    }catch(err){
      return false;
    }
  }
  getUsername():string | null{
    const token = this.getToken();
    if(!token) return null;
    try {
      const decoded: JwtPayload = jwtDecode(token);
      return decoded.sub || null;
    }catch(err){
      return null;
    }
  }
  getUserRole():string | null{
    const token = this.getToken();
    if(!token) return null;
    try {
      const decoded: any = jwtDecode(token);
      return decoded.role || null;
    }catch(err){
      return null;
    }
  }
}
