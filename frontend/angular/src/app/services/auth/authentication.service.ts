import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthenticationRequest } from 'src/app/models/authentication-request';
import { Observable } from 'rxjs';
import { AuthenticatonResponse } from 'src/app/models/authentication-response';
import { environment } from 'src/environments/environment';
import CustomerRegistrationRequest from 'src/app/models/CustomerRegistrationRequest';
@Injectable({providedIn: 'root'})
export class AuthenticationService {

  private readonly auth_url = `${environment.api.base_url}/${environment.api.auth_url}`;
  private readonly customer_url = `${environment.api.base_url}/${environment.api.customer_url}`;
  constructor(private http:HttpClient)
  {

  }

  login(request:AuthenticationRequest):Observable<AuthenticatonResponse>
  {
    return this.http.post<AuthenticatonResponse>(this.auth_url,request);
  }

  register(request:CustomerRegistrationRequest): Observable<void>
  {
    return this.http.post<void>(this.customer_url,request);
  }
}
