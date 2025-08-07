import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Owner, Pet, PetType } from '../models/petclinic.models';

@Injectable({
  providedIn: 'root'
})
export class OwnerService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getAllOwners(): Observable<Owner[]> {
    return this.http.get<Owner[]>(`${this.baseUrl}/owners/list`);
  }

  getOwnerById(id: number): Observable<Owner> {
    return this.http.get<Owner>(`${this.baseUrl}/owners/${id}`);
  }

  createOwner(owner: Owner): Observable<Owner> {
    return this.http.post<Owner>(`${this.baseUrl}/owners`, owner);
  }

  updateOwner(id: number, owner: Owner): Observable<Owner> {
    return this.http.put<Owner>(`${this.baseUrl}/owners/${id}`, owner);
  }

  deleteOwner(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/owners/${id}`);
  }

  getPetTypes(): Observable<PetType[]> {
    return this.http.get<PetType[]>(`${this.baseUrl}/petTypes`);
  }

  createPet(ownerId: number, pet: Pet): Observable<Pet> {
    return this.http.post<Pet>(`${this.baseUrl}/owners/${ownerId}/pets`, pet);
  }

  updatePet(ownerId: number, petId: number, pet: Pet): Observable<Pet> {
    return this.http.put<Pet>(`${this.baseUrl}/owners/${ownerId}/pets/${petId}`, pet);
  }
}
