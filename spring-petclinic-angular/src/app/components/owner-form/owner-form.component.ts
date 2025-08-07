import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { OwnerService } from '../../services/owner.service';
import { Owner } from '../../models/petclinic.models';

@Component({
  selector: 'app-owner-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './owner-form.component.html',
  styleUrl: './owner-form.component.css'
})
export class OwnerFormComponent implements OnInit {
  owner: Owner = {
    firstName: '',
    lastName: '',
    address: '',
    city: '',
    telephone: ''
  };
  
  isEditMode = false;
  ownerId: number | null = null;
  loading = false;
  error: string | null = null;

  constructor(
    private ownerService: OwnerService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.ownerId = +id;
      this.loadOwner(this.ownerId);
    }
  }

  loadOwner(id: number): void {
    this.loading = true;
    this.error = null;
    
    this.ownerService.getOwnerById(id).subscribe({
      next: (owner) => {
        this.owner = owner;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load owner. Please try again.';
        this.loading = false;
        console.error('Error loading owner:', error);
      }
    });
  }

  onSubmit(): void {
    if (!this.owner.firstName || !this.owner.lastName || !this.owner.address || !this.owner.city || !this.owner.telephone) {
      this.error = 'All fields are required.';
      return;
    }

    this.loading = true;
    this.error = null;

    const operation = this.isEditMode 
      ? this.ownerService.updateOwner(this.ownerId!, this.owner)
      : this.ownerService.createOwner(this.owner);

    operation.subscribe({
      next: (owner) => {
        this.loading = false;
        if (this.isEditMode) {
          this.router.navigate(['/owners', owner.id]);
        } else {
          this.router.navigate(['/owners']);
        }
      },
      error: (error) => {
        this.error = 'Failed to save owner. Please try again.';
        this.loading = false;
        console.error('Error saving owner:', error);
      }
    });
  }

  onCancel(): void {
    if (this.isEditMode && this.ownerId) {
      this.router.navigate(['/owners', this.ownerId]);
    } else {
      this.router.navigate(['/owners']);
    }
  }
}
