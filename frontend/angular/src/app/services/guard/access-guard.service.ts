import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AccessGuardService implements CanActivate {
  
  constructor(private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot, 
    state: RouterStateSnapshot
  ): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    const jwtToken = localStorage.getItem("token");
    
    const jwthelper = new JwtHelperService();
    const isTokenExpired: boolean = jwthelper.isTokenExpired(jwtToken);
  
    if (isTokenExpired) {
        this.router.navigate(['login']);
      return false;
    } else {
      return true;
    }
  }
}
