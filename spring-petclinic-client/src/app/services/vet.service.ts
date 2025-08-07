import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vet } from '../models/petclinic.models';

@Injectable({
  providedIn: 'root'
})
export class VetService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getAllVets(): Observable<Vet[]> {
    return this.http.get<Vet[]>(`${this.baseUrl}/vets`);
  }
}
