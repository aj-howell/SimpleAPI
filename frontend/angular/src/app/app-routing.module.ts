import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerComponent } from './components/customer/customer.component';
import { LoginComponent } from './components/login/login.component';
import { AccessGuardService } from './services/guard/access-guard.service';

const routes: Routes = [
  {
    path:"",
    redirectTo:"login",
    pathMatch:"full",
  },
  {
    path: "customers",
    component: CustomerComponent,
    canActivate:[AccessGuardService]
  },
  {
    path:"login",
    component:LoginComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
