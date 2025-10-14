import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Owner, Pet, PetType, Visit, Vet } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class ClinicService {
  private apiUrl = '';

  constructor(private http: HttpClient) { }

  // Owner operations
  getOwners(lastName: string = ''): Observable<Owner[]> {
    let params = new HttpParams();
    if (lastName) {
      params = params.set('lastName', lastName);
    }
    return this.http.get<Owner[]>(`${this.apiUrl}/owners/list`, { params });
  }

  getOwner(id: number): Observable<Owner> {
    return this.http.get<Owner>(`${this.apiUrl}/owners/${id}`);
  }

  createOwner(owner: Owner): Observable<Owner> {
    return this.http.post<Owner>(`${this.apiUrl}/owners`, owner);
  }

  updateOwner(id: number, owner: Owner): Observable<Owner> {
    return this.http.put<Owner>(`${this.apiUrl}/owners/${id}`, owner);
  }

  // Pet operations
  getPetTypes(): Observable<PetType[]> {
    return this.http.get<PetType[]>(`${this.apiUrl}/pettypes`);
  }

  getPet(ownerId: number, petId: number): Observable<Pet> {
    return this.http.get<Pet>(`${this.apiUrl}/owners/${ownerId}/pets/${petId}`);
  }

  createPet(ownerId: number, pet: Pet): Observable<Pet> {
    return this.http.post<Pet>(`${this.apiUrl}/owners/${ownerId}/pets`, pet);
  }

  updatePet(ownerId: number, petId: number, pet: Pet): Observable<Pet> {
    return this.http.put<Pet>(`${this.apiUrl}/owners/${ownerId}/pets/${petId}`, pet);
  }

  // Visit operations
  createVisit(ownerId: number, petId: number, visit: Visit): Observable<Visit> {
    return this.http.post<Visit>(`${this.apiUrl}/owners/${ownerId}/pets/${petId}/visits`, visit);
  }

  // Vet operations
  getVets(): Observable<Vet[]> {
    return this.http.get<Vet[]>(`${this.apiUrl}/vets`);
  }
}
