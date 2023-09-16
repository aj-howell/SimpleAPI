import { Component, EventEmitter, Input, Output } from '@angular/core';
import CustomerDTO from '../customer/CustomerDTO';
import { CustomerService } from 'src/app/services/customer/customer.service';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-customer-card',
  templateUrl: './customer-card.component.html',
  styleUrls: ['./customer-card.component.scss']
})
export class CustomerCardComponent 
{
 
  constructor(private customerService:CustomerService, private confirmationService: ConfirmationService, private messageService: MessageService){}

  @Input()
  customer: CustomerDTO = {
    name: '',
    email: '',
    gender: '',
    age: 0,
    id: 0,
    roles: []
  };
  
  @Input()
  customerIndex = 0;

  customerImage():string
  {
    const sex:string = this.customer.gender==='Male' ? 'men':'women';

    return`https://randomuser.me/api/portraits/${sex}/${this.customerIndex}.jpg`; 
  }

  @Output()
  submit: EventEmitter<number> = new EventEmitter<number>;

  private onDelete():void
  {
      this.submit.emit(this.customer.id); // pass the id to the parent
  }

  
  Show():void
  {
    this.confirmationService.confirm({
      message: 'Are you sure that you want to proceed?',
      header: 'Delete',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.onDelete();
        this.messageService.add({ severity: 'info', summary: 'Confirmed', detail: 'Customer deleted' });
      },
       reject: ()=>
       {
        this.messageService.add({ severity: 'info', summary: 'Confirmed', detail: 'Customer not deleted' });
       }
    });
  }

  @Output()
  update: EventEmitter<CustomerDTO> = new EventEmitter<CustomerDTO>;

  onUpdate()
  {
    this.update.emit(this.customer)
  }

}
