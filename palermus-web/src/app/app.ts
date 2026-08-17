import { Component, signal, inject, OnInit } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from './features/users/services/auth-service';
import { UserService } from './features/users/services/user-service';
import { ReferenceProfile } from './features/users/models/profile/ReferenceProfile';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  private authService = inject(AuthService);
  private userService = inject(UserService);

  userMenuOpen = signal(false);
  profile = signal<ReferenceProfile | null>(null);

  ngOnInit(): void {
    if (this.isLoggedIn()) {
      const username = this.authService.getUsername();
      if (username) {
        this.userService.getProfileByUsername(username).subscribe({
          next: (data) => this.profile.set(data),
          error: (err) => console.error('Error fetching profile', err)
        });
      }
    }
  }

  isLoggedIn(): boolean {
    return this.authService.isTokenValid();
  }

  username(): string {
    return this.authService.getUsername() || 'Usuario';
  }

  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }
  
  isClient(): boolean {
    return this.authService.getUserRole() === 'CLIENT';
  }

  toggleUserMenu(): void {
    this.userMenuOpen.update(v => !v);
  }

  closeUserMenu(): void {
    this.userMenuOpen.set(false);
  }

  onLogout(): void {
    this.closeUserMenu();
    this.authService.logout();
  }
}
