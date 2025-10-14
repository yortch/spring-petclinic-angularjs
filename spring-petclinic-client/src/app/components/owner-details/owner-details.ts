import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ClinicService } from '../../services/clinic.service';
import { Owner } from '../../models/models';

@Component({
  selector: 'owner-details',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './owner-details.html',
  styleUrls: ['./owner-details.scss']
})
export class OwnerDetailsComponent implements OnInit {
  owner: Owner | null = null;
  ownerId: number = 0;

  constructor(
    private route: ActivatedRoute,
    private clinicService: ClinicService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.ownerId = +params['id'];
      this.loadOwner();
    });
  }

  loadOwner(): void {
    this.clinicService.getOwner(this.ownerId).subscribe({
      next: (owner) => {
        this.owner = owner;
      },
      error: (err) => {
        console.error('Error loading owner:', err);
      }
    });
  }
}
