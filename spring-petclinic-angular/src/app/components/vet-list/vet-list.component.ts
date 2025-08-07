import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VetService } from '../../services/vet.service';
import { Vet } from '../../models/petclinic.models';

@Component({
  selector: 'app-vet-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './vet-list.component.html',
  styleUrl: './vet-list.component.css'
})
export class VetListComponent implements OnInit {
  vets: Vet[] = [];
  loading = true;
  error: string | null = null;

  constructor(private vetService: VetService) {}

  ngOnInit(): void {
    this.loadVets();
  }

  loadVets(): void {
    this.loading = true;
    this.error = null;
    
    this.vetService.getAllVets().subscribe({
      next: (vets) => {
        this.vets = vets;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load veterinarians. Please try again later.';
        this.loading = false;
        console.error('Error loading vets:', error);
      }
    });
  }

  trackByVet(index: number, vet: Vet): number {
    return vet.id || index;
  }
}
