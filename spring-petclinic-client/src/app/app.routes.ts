import { Routes } from '@angular/router';
import { WelcomeComponent } from './components/welcome/welcome';
import { OwnerListComponent } from './components/owner-list/owner-list';
import { OwnerDetailsComponent } from './components/owner-details/owner-details';
import { OwnerFormComponent } from './components/owner-form/owner-form';
import { PetFormComponent } from './components/pet-form/pet-form';
import { VisitFormComponent } from './components/visit-form/visit-form';
import { VetListComponent } from './components/vet-list/vet-list';

export const routes: Routes = [
  { path: '', redirectTo: '/welcome', pathMatch: 'full' },
  { path: 'welcome', component: WelcomeComponent },
  { path: 'owners', component: OwnerListComponent },
  { path: 'owners/new', component: OwnerFormComponent },
  { path: 'owners/:id', component: OwnerDetailsComponent },
  { path: 'owners/:id/edit', component: OwnerFormComponent },
  { path: 'owners/:ownerId/pets/new', component: PetFormComponent },
  { path: 'owners/:ownerId/pets/:petId/edit', component: PetFormComponent },
  { path: 'owners/:ownerId/pets/:petId/visits/new', component: VisitFormComponent },
  { path: 'vets', component: VetListComponent },
  { path: '**', redirectTo: '/welcome' }
];
