import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import CustomerDTO from './CustomerDTO';
import { CustomerService } from 'src/app/services/customer/customer.service';
import CustomerRegistrationRequest from 'src/app/models/CustomerRegistrationRequest';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss']
})
export class CustomerComponent implements OnInit {

  isVisible: boolean = false;

  operation: 'create' | 'update' = 'create';

  customer:CustomerDTO={
    roles: []
  };

  customerReq: CustomerRegistrationRequest = {};

  customers: Array<CustomerDTO> = [];

  constructor(private customerService: CustomerService, private messageService: MessageService){};

  ngOnInit(): void {
    this.getCustomers();
    this.getCustomer();
  }
  
  private getCustomer() {
    
    const id = Number(localStorage.getItem("id"));
    this.customerService.getCustomer(id)
    .subscribe({
      next: (customerData)=>
      {
        this.customer=customerData;
      }
    });

  }

  private getCustomers(): void {
      this.customerService.getCustomers()
        .subscribe(
          {
            next: (data: CustomerDTO[])=>
            {
              
              this.customers=data;
              console.log(this.customers);
            }
          })
  }

  private showSuccess(msg: string) 
  {
    this.messageService.add({ severity: 'success', summary: 'Success', detail: msg });
  }

  private showError(msg: string) 
  {
    this.messageService.add({ severity: 'error', summary: 'Error', detail: msg });
  }

  saveCustomer(request: CustomerRegistrationRequest): void
  {
    if(this.operation==='create')
    {
      this.customerService.saveCustomer(request)
      .subscribe({
        //use next if you want to do something after the request was made or need data from request
        next: ()=>
        {
          this.getCustomers();
          this.isVisible=false;
          this.showSuccess("Customer: "+this.customerReq.name+" has been saved");
          this.customerReq={};
          
  
        },
  
        error:(err)=>
        {
          this.showError("Customer has not been saved")
        }
      });
    }
    
    else {
    this.customerService.updateCustomer(this.customerReq.id,request)
    .subscribe({
      //use next if you want to do something after the request was made or need data from request
      next: ()=>
      {
        this.getCustomers();
        this.isVisible=false;
        console.log("this is your update request: ",this.customerReq);
        this.showSuccess("Customer: "+this.customerReq.name+" has been updated");
        this.customerReq={};
      },

      error:(err)=>
      {
        this.showError("Customer has not been saved")
      }
    });

  }

  }

  deleteCustomer(id: number):void
  {
    if(id)
    {
      this.customerService.deleteCustomer(id)
      .subscribe({
          next: ()=>
          {
            this.getCustomers();
          }
      });

    }
  }

  updateCustomer(customerDTO:CustomerDTO):void
  {
    this.operation='update';
      this.isVisible=true;
      this.customerReq=customerDTO;
  }

  onCreate()
  {
    this.operation='create';
    this.customerReq={};
    this.isVisible=true;
  }

  onCancel()
  {
    this.customerReq={};
    this.isVisible=false;
    this.operation='create';
  }

}

