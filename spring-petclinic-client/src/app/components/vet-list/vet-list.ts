import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClinicService } from '../../services/clinic.service';
import { Vet } from '../../models/models';

@Component({
  selector: 'app-vet-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './vet-list.html',
  styleUrl: './vet-list.scss'
})
export class VetListComponent implements OnInit {
  vets: Vet[] = [];

  constructor(private clinicService: ClinicService) {}

  ngOnInit(): void {
    this.clinicService.getVets().subscribe({
      next: (data) => this.vets = data,
      error: (err) => console.error('Error loading vets:', err)
    });
  }
}
