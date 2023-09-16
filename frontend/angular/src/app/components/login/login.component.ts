import { Component } from '@angular/core';
import { Router } from '@angular/router';
import CustomerRegistrationRequest from 'src/app/models/CustomerRegistrationRequest';
import { AuthenticationRequest } from 'src/app/models/authentication-request';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  errMsg: string="";

  constructor(private authService:AuthenticationService, private router:Router) 
  {

  }

  isVisible: boolean = false;
  
  authenticationRequest: AuthenticationRequest = {username: "", password:""}

  customerReq:CustomerRegistrationRequest={}

  login():void
  {
    this.errMsg="";
    this.authService.login(this.authenticationRequest)
    .subscribe(
      {
        next:(authenticationResponse)=>
        {
          if(authenticationResponse.token){
            localStorage.setItem("token", authenticationResponse.token);
            localStorage.setItem("id", ""+authenticationResponse.id);
          this.router.navigate(["customers"]);
          }
        },

        error: (err)=>
        {
          if(err.error.statusCode===500)
          {
              this.errMsg='Login and/or password is incorrect';
          }
        }
      });
  }


  register(): void {
    this.errMsg = '';
  
    // First, attempt to register the user
    this.authService.register(this.customerReq).subscribe({
      next: () => {
        // Registration was successful, now create an AuthenticationRequest using the registration data
        const authRequest: AuthenticationRequest = {
          username: this.customerReq.email, // Map the username from the registration request
          password: this.customerReq.password, // Map the password from the registration request
        };
  
        // Attempt to log in using the mapped AuthenticationRequest
        this.authService.login(authRequest).subscribe({
          next: (authenticationResponse) => {
            if (authenticationResponse.token) {
              // Save the token and perform any other authentication-related tasks
              localStorage.setItem('token', authenticationResponse.token);
              localStorage.setItem('id', '' + authenticationResponse.id);
              this.router.navigate(['customers']);
            }
          },
          error: (loginError) => {
            // Handle login error here, if needed
            console.error('Login error:', loginError);
          },
        });
      },
      error: (registrationError) => {
        // Handle registration error here, if needed
        console.error('Registration error:', registrationError);
        this.errMsg = 'Registration failed. Please try again.';
      },
    });
  }
  
}
