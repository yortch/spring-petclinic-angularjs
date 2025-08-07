import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { OwnerService } from '../../services/owner.service';
import { Owner } from '../../models/petclinic.models';

@Component({
  selector: 'app-owner-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './owner-list.component.html',
  styleUrl: './owner-list.component.css'
})
export class OwnerListComponent implements OnInit {
  owners: Owner[] = [];
  filteredOwners: Owner[] = [];
  searchQuery = '';
  loading = true;
  error: string | null = null;

  constructor(private ownerService: OwnerService) {}

  ngOnInit(): void {
    this.loadOwners();
  }

  loadOwners(): void {
    this.loading = true;
    this.error = null;
    
    this.ownerService.getAllOwners().subscribe({
      next: (owners) => {
        this.owners = owners;
        this.filteredOwners = owners;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load owners. Please try again later.';
        this.loading = false;
        console.error('Error loading owners:', error);
      }
    });
  }

  onSearchChange(): void {
    if (!this.searchQuery.trim()) {
      this.filteredOwners = this.owners;
    } else {
      const query = this.searchQuery.toLowerCase();
      this.filteredOwners = this.owners.filter(owner =>
        owner.firstName.toLowerCase().includes(query) ||
        owner.lastName.toLowerCase().includes(query) ||
        owner.address.toLowerCase().includes(query) ||
        owner.city.toLowerCase().includes(query) ||
        owner.telephone.includes(query)
      );
    }
  }

  trackByOwner(index: number, owner: Owner): number {
    return owner.id || index;
  }
}
