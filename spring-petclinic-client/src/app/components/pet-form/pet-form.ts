import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ClinicService } from '../../services/clinic.service';
import { Pet, PetType } from '../../models/models';

@Component({
  selector: 'pet-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './pet-form.html',
  styleUrls: ['./pet-form.scss']
})
export class PetFormComponent implements OnInit {
  petForm: FormGroup;
  ownerId: number = 0;
  petId: number = 0;
  isEditMode: boolean = false;
  submitted: boolean = false;
  ownerName: string = '';
  petTypes: PetType[] = [];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private clinicService: ClinicService
  ) {
    this.petForm = this.fb.group({
      name: ['', Validators.required],
      birthDate: ['', Validators.required],
      typeId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.ownerId = +params['ownerId'];
      
      // Load pet types first
      this.clinicService.getPetTypes().subscribe({
        next: (types) => {
          this.petTypes = types;
          
          if (params['petId']) {
            this.petId = +params['petId'];
            this.isEditMode = true;
            this.loadPet();
          } else {
            this.loadOwner();
            // Set default pet type if available
            if (this.petTypes.length > 0) {
              this.petForm.patchValue({ typeId: this.petTypes[0].id });
            }
          }
        },
        error: (err) => {
          console.error('Error loading pet types:', err);
        }
      });
    });
  }

  loadOwner(): void {
    this.clinicService.getOwner(this.ownerId).subscribe({
      next: (owner) => {
        this.ownerName = `${owner.firstName} ${owner.lastName}`;
      },
      error: (err) => {
        console.error('Error loading owner:', err);
      }
    });
  }

  loadPet(): void {
    this.clinicService.getPet(this.ownerId, this.petId).subscribe({
      next: (pet) => {
        this.ownerName = `${pet.owner?.firstName || ''} ${pet.owner?.lastName || ''}`.trim();
        this.petForm.patchValue({
          name: pet.name,
          birthDate: pet.birthDate,
          typeId: pet.type.id
        });
      },
      error: (err) => {
        console.error('Error loading pet:', err);
      }
    });
  }

  onSubmit(): void {
    this.submitted = true;
    
    if (this.petForm.invalid) {
      return;
    }

    const petData: Pet = {
      id: this.petId || 0,
      name: this.petForm.value.name,
      birthDate: this.petForm.value.birthDate,
      type: { id: this.petForm.value.typeId, name: '' },
      visits: []
    };

    const request = this.isEditMode
      ? this.clinicService.updatePet(this.ownerId, this.petId, petData)
      : this.clinicService.createPet(this.ownerId, petData);

    request.subscribe({
      next: () => {
        this.router.navigate(['/owners', this.ownerId]);
      },
      error: (err) => {
        console.error('Error saving pet:', err);
      }
    });
  }

  get f() {
    return this.petForm.controls;
  }
}
