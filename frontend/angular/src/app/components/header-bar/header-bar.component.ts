import { Component, Input } from '@angular/core';
import { MenuItem } from 'primeng/api';
import CustomerDTO from '../customer/CustomerDTO';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header-bar',
  templateUrl: './header-bar.component.html',
  styleUrls: ['./header-bar.component.scss']
})
export class HeaderBarComponent {

  constructor(private router:Router){}

  @Input()
  customer:CustomerDTO={
    roles: []
  }

  items:Array<MenuItem>=[
    {label: 'Profile', icon: 'pi pi-user'},
    {label: 'Settings', icon: 'pi pi-cog'},
    {separator: true},
    {label: 'Sign Out', icon: 'pi pi-signout',
      command: ()=>
      {
        localStorage.removeItem("id");
        localStorage.removeItem("token");
        this.router.navigate(["login"]);
      }
    }
  ]

}
