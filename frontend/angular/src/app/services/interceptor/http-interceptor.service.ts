import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpEvent, HttpHandler, HttpRequest , HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs'; 
import { JwtHelperService } from '@auth0/angular-jwt';
import { Router } from '@angular/router';

@Injectable()
export class HttpInterceptorService implements HttpInterceptor {
  

  constructor(private router: Router){}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('request intercepted');
    const jwtToken = localStorage.getItem("token");
    
    if(jwtToken)
    {
      const authRequest = req.clone(
        {
          headers: new HttpHeaders(
            {
                Authorization: 'Bearer '+jwtToken
            })
          
        });
        return next.handle(authRequest);
    }
    
    return next.handle(req);
  }
}
