import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import CustomerRegistrationRequest from 'src/app/models/CustomerRegistrationRequest';
@Component({
  selector: 'app-manage-customer',
  templateUrl: './manage-customer.component.html',
  styleUrls: ['./manage-customer.component.scss']
})
export class ManageCustomerComponent {
  @Input()
  customerReq:CustomerRegistrationRequest={};

  isVisible: boolean=true;
  
  @Input()
  operation: 'create' | 'update'='create';

  isValid():boolean
  {
    return this.minLength(this.customerReq.name) &&
           this.minLength(this.customerReq.email) &&
           this.customerReq.age !== undefined && this.customerReq.age > 0 &&
           (this.operation==='update' || this.minLength(this.customerReq.gender) && this.minLength(this.customerReq.password));
  }

  minLength(input:string | undefined): boolean
  {
    return input!==null && input!==undefined && input.length > 0
  }

  @Output()
  submit: EventEmitter<CustomerRegistrationRequest> = new EventEmitter<CustomerRegistrationRequest>;

  onSubmit()
  {
    this.submit.emit(this.customerReq);
  }

  @Output()
  cancel: EventEmitter<void> = new EventEmitter<void>;
 
  onCancel()
  {
    this.cancel.emit();
  }


}
