import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Empleado } from 'src/app/models/dtos/Empleado';
import { EmpleadoService } from 'src/app/services/empleado/empleado.service';


@Component({
  selector: 'app-empleadolist',
  templateUrl: './empleadolist.component.html',
  styleUrls: ['./empleadolist.component.css']
})
export class EmpleadolistComponent {

  constructor(private empleadoService: EmpleadoService,private router:Router){}

  empleados: Empleado[] = [];
  ngOnInit():void{
    this.empleadoService.getEmpleado().subscribe( {
      next: (empleados : any) => this.empleados = empleados,
      error: (e) => console.log(e),
      complete: () => console.log("Completado")
  });
  }

  getAll(){
    this.empleadoService.getAll()
    .subscribe((usuarios : any) => {
      console.log(usuarios)
      this.empleados = usuarios
    })
  }

  eliminar(empleado: Empleado):void {
    this.empleadoService.deleteEmpleado(empleado).subscribe(data=>{
      this.empleados=this.empleados!.filter(e=>e!==empleado);
    });
  }

}
