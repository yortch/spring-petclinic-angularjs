import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ClinicService } from '../../services/clinic.service';
import { Owner } from '../../models/models';

@Component({
  selector: 'app-owner-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './owner-list.html',
  styleUrl: './owner-list.scss'
})
export class OwnerListComponent implements OnInit {
  owners: Owner[] = [];
  query: string = '';

  constructor(private clinicService: ClinicService) {}

  ngOnInit(): void {
    this.clinicService.getOwners().subscribe({
      next: (data) => this.owners = data,
      error: (err) => console.error('Error loading owners:', err)
    });
  }

  filterOwners(): Owner[] {
    if (!this.query) {
      return this.owners;
    }
    const lowerQuery = this.query.toLowerCase();
    return this.owners.filter(owner =>
      owner.firstName.toLowerCase().includes(lowerQuery) ||
      owner.lastName.toLowerCase().includes(lowerQuery) ||
      owner.address?.toLowerCase().includes(lowerQuery) ||
      owner.city?.toLowerCase().includes(lowerQuery) ||
      owner.telephone?.includes(this.query) ||
      owner.pets?.some(pet => pet.name.toLowerCase().includes(lowerQuery))
    );
  }
}
