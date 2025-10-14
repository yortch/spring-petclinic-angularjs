import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ClinicService } from '../../services/clinic.service';
import { Owner } from '../../models/models';

@Component({
  selector: 'owner-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './owner-form.html',
  styleUrls: ['./owner-form.scss']
})
export class OwnerFormComponent implements OnInit {
  ownerForm: FormGroup;
  ownerId: number = 0;
  isEditMode: boolean = false;
  submitted: boolean = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private clinicService: ClinicService
  ) {
    this.ownerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      address: ['', Validators.required],
      city: ['', Validators.required],
      telephone: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]]
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.ownerId = +params['id'];
        this.isEditMode = true;
        this.loadOwner();
      }
    });
  }

  loadOwner(): void {
    this.clinicService.getOwner(this.ownerId).subscribe({
      next: (owner) => {
        this.ownerForm.patchValue({
          firstName: owner.firstName,
          lastName: owner.lastName,
          address: owner.address,
          city: owner.city,
          telephone: owner.telephone
        });
      },
      error: (err) => {
        console.error('Error loading owner:', err);
      }
    });
  }

  onSubmit(): void {
    this.submitted = true;
    
    if (this.ownerForm.invalid) {
      return;
    }

    const ownerData: Owner = {
      id: this.ownerId || 0,
      ...this.ownerForm.value,
      pets: []
    };

    const request = this.isEditMode
      ? this.clinicService.updateOwner(this.ownerId, ownerData)
      : this.clinicService.createOwner(ownerData);

    request.subscribe({
      next: (savedOwner) => {
        this.router.navigate(['/owners', savedOwner.id || this.ownerId]);
      },
      error: (err) => {
        console.error('Error saving owner:', err);
      }
    });
  }

  get f() {
    return this.ownerForm.controls;
  }
}
