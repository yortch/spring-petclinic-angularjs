import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

interface Specialty {
  name: string;
}

interface Vet {
  firstName: string;
  lastName: string;
  specialties: Specialty[];
}

@Component({
  selector: 'app-vet-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './vet-list.component.html',
  styleUrl: './vet-list.component.scss'
})
export class VetListComponent implements OnInit {
  vetList: Vet[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<Vet[]>('/vets').subscribe(
      data => this.vetList = data,
      error => console.error('Error loading vets:', error)
    );
  }
}
