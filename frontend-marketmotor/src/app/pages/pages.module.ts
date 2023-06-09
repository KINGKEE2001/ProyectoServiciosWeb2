import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuariolistComponent } from './usuario/usuariolist/usuariolist.component';
import { ProveedorlistComponent } from './proveedor/proveedorlist/proveedorlist.component';
import { ProveedorinsertComponent } from './proveedor/proveedorinsert/proveedorinsert.component';
import { ProveedorupdateComponent } from './proveedor/proveedorupdate/proveedorupdate.component';
import { ProductoinsertComponent } from './producto/productoinsert/productoinsert.component';
import { ProductoupdateComponent } from './producto/productoupdate/productoupdate.component';
import { ProductolistComponent } from './producto/productolist/productolist.component';
import { EmpleadoupdateComponent } from './empleado/empleadoupdate/empleadoupdate.component';
import { EmpleadoinsertComponent } from './empleado/empleadoinsert/empleadoinsert.component';
import { EmpleadolistComponent } from './empleado/empleadolist/empleadolist.component';
import { LoginComponent } from './login/login.component';
import { InicioComponent } from './inicio/inicio.component';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { GenerarventaComponent } from './venta/generarventa/generarventa.component';
import { ReporteordencompraComponent } from './ordencompra/reporteordencompra/reporteordencompra.component';
import { GenerarordencompraComponent } from './ordencompra/generarordencompra/generarordencompra.component';
import { ReporteventaComponent } from './venta/reporteventa/reporteventa.component';
import { PaginationComponent } from '../components/pagination/pagination.component';
import { ComponentsModule } from '../components/components.module';
import { NgxPaginationModule } from 'ngx-pagination';
import { ReportetemplateComponent } from './reportetemplate/reportetemplate.component';
import { NgxPrintModule } from 'ngx-print';
import { ValidarordenComponent } from './proveedor/validar/validarorden/validarorden.component';
import { ReportetemplateventaComponent } from './reportetemplateventa/reportetemplateventa.component';



@NgModule({
  declarations: [
    UsuariolistComponent,
    ProveedorlistComponent,
    ProveedorinsertComponent,
    ProveedorupdateComponent,
    ProductoinsertComponent,
    ProductoupdateComponent,
    ProductolistComponent,
    EmpleadoupdateComponent,
    EmpleadoinsertComponent,
    EmpleadolistComponent,
    LoginComponent,
    InicioComponent,
    GenerarventaComponent,
    ReporteordencompraComponent,
    GenerarordencompraComponent,
    ReporteventaComponent,
    ReportetemplateComponent,
    ValidarordenComponent,
    ReportetemplateventaComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    FormsModule,
    ComponentsModule,
    NgxPaginationModule,
    NgxPrintModule
  ]
})
export class PagesModule { }
