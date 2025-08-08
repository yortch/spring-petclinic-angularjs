import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

interface Pet {
  id: number;
  name: string;
}

interface Owner {
  id: number;
  firstName: string;
  lastName: string;
  address: string;
  city: string;
  telephone: string;
  pets: Pet[];
}

@Component({
  selector: 'app-owner-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './owner-list.component.html',
  styleUrl: './owner-list.component.scss'
})
export class OwnerListComponent implements OnInit {
  owners: Owner[] = [];
  query: string = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<Owner[]>('/owners/list').subscribe(
      data => this.owners = data,
      error => console.error('Error loading owners:', error)
    );
  }

  get filteredOwners(): Owner[] {
    if (!this.query) return this.owners;
    return this.owners.filter(owner => 
      owner.firstName.toLowerCase().includes(this.query.toLowerCase()) ||
      owner.lastName.toLowerCase().includes(this.query.toLowerCase()) ||
      owner.city.toLowerCase().includes(this.query.toLowerCase()) ||
      owner.telephone.includes(this.query) ||
      owner.address.toLowerCase().includes(this.query.toLowerCase())
    );
  }

  trackByOwnerId(index: number, owner: Owner): number {
    return owner.id;
  }
}
