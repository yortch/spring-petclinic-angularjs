import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ClinicService } from '../../services/clinic.service';
import { Visit } from '../../models/models';

@Component({
  selector: 'visit-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './visit-form.html',
  styleUrls: ['./visit-form.scss']
})
export class VisitFormComponent implements OnInit {
  visitForm: FormGroup;
  ownerId: number = 0;
  petId: number = 0;
  submitted: boolean = false;
  previousVisits: Visit[] = [];
  petName: string = '';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private clinicService: ClinicService
  ) {
    this.visitForm = this.fb.group({
      date: [new Date().toISOString().split('T')[0], Validators.required],
      description: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.ownerId = +params['ownerId'];
      this.petId = +params['petId'];
      this.loadPet();
    });
  }

  loadPet(): void {
    this.clinicService.getPet(this.ownerId, this.petId).subscribe({
      next: (pet) => {
        this.petName = pet.name;
        this.previousVisits = pet.visits || [];
      },
      error: (err) => {
        console.error('Error loading pet:', err);
      }
    });
  }

  onSubmit(): void {
    this.submitted = true;
    
    if (this.visitForm.invalid) {
      return;
    }

    const visitData: Visit = {
      id: 0,
      date: this.visitForm.value.date,
      description: this.visitForm.value.description
    };

    this.clinicService.createVisit(this.ownerId, this.petId, visitData).subscribe({
      next: () => {
        this.router.navigate(['/owners', this.ownerId]);
      },
      error: (err) => {
        console.error('Error creating visit:', err);
      }
    });
  }

  get f() {
    return this.visitForm.controls;
  }
}
