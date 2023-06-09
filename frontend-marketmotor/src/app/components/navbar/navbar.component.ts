import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AccessService } from 'src/app/services/access/access.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { ShortinfouserService } from 'src/app/services/shortinfouser/shortinfouser.service';
import { StorageService } from 'src/app/services/storage/storage.service';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  cargo = "";
  usuario = "";

  ngOnInit():void{
    this.showCargo();
    this.showUsuario();
  }

  constructor(private authService: AuthService,private sesionService:StorageService,private shortInfo:ShortinfouserService,private accessServices: AccessService,private router:Router){
    
  }

  logout():void{
    this.authService.logout()
    sessionStorage.clear()
    this.router.navigate(['login'])
  }

  showCargo(){
    this.cargo = this.shortInfo.getNombreCargo()
  }

  showUsuario(){
    this.usuario = this.shortInfo.getAlias()
  }

  isAdmin(): boolean{

    return this.accessServices.accessAdmin()
  }


  isEmployee(): boolean{
    return this.accessServices.accessAsist()|| this.accessServices.accessAdmin();
  }

  isProveedor(): boolean{
    return this.accessServices.accessProv();
  }

}
