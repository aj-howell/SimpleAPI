import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import CustomerDTO from 'src/app/components/customer/CustomerDTO';
import { environment } from 'src/environments/environment';
import CustomerRegistrationRequest from 'src/app/models/CustomerRegistrationRequest';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private readonly customer_url = `${environment.api.base_url}/${environment.api.customer_url}`;
  
  constructor(private http: HttpClient) { }
  
  customers: Array<CustomerDTO>=[];

  getCustomers():Observable<CustomerDTO[]> // we did not define the type within the request
  {
    return this.http
    .get<CustomerDTO[]>( this.customer_url)
  }

  getCustomer(id:number):Observable<CustomerDTO> // we did not define the type within the request
  {
    return this.http
    .get<CustomerDTO>( this.customer_url+'/'+id)
  }


  saveCustomer( req: CustomerRegistrationRequest): Observable<void>
  {
    return this.http.post<void>(this.customer_url, req);
  }

  deleteCustomer(id: number): Observable<void> 
  {
    return this.http.delete<void>(this.customer_url+'/'+id);
  }

  updateCustomer(id:number | undefined, req: CustomerRegistrationRequest):Observable<void>
  {
    return this.http.put<void>(this.customer_url+'/'+id, req);
  }

}
