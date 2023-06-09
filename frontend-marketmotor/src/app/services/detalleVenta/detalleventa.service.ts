import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import baserUrl from '../globalurl/UrlApi';
import { DetalleVenta } from 'src/app/models/dtos/DetalleVenta';

@Injectable({
  providedIn: 'root'
})
export class DetalleventaService {

  private readonly apiUrl = baserUrl+"/detalleventa";

  constructor(private http: HttpClient) {  }

  getAllByPaginable(pageNo: number = 0,pageSize: number = 10, sortBy: string= "id",sortDir: string="asc"){
    return this.http.get(`${this.apiUrl}/pagination`,{
      params: new HttpParams().set('pageNo', pageNo).set('pageSize',pageSize)
    })
  }

  getAll(){
    return this.http.get<DetalleVenta[]>(this.apiUrl);
  }

  
  getAllByEmpleadoIdPaginable(id : number,pageNo: number = 0,pageSize: number = 10, sortBy: string= "id",sortDir: string="asc"){
    return this.http.get(`${this.apiUrl}/empleado/pagination/${id}`,{
      params: new HttpParams().set('pageNo', pageNo).set('pageSize',pageSize)
    })
  }


  getAllByProductoIdPaginable(id : number,pageNo: number = 0,pageSize: number = 10, sortBy: string= "id",sortDir: string="asc"){
    return this.http.get(`${this.apiUrl}/producto/pagination/${id}`,{
      params: new HttpParams().set('pageNo', pageNo).set('pageSize',pageSize)
    })
  }

  getAllByProductoId(id:number){
    return this.http.get<DetalleVenta[]>(this.apiUrl+"/producto/"+id);
  }

  guardarDetalleVenta(detalleVenta: any){
    return this.http.post(this.apiUrl, detalleVenta);
  }


}
