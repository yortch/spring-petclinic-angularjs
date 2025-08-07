import { Routes } from '@angular/router';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { OwnerListComponent } from './components/owner-list/owner-list.component';
import { OwnerDetailsComponent } from './components/owner-details/owner-details.component';
import { OwnerFormComponent } from './components/owner-form/owner-form.component';
import { VetListComponent } from './components/vet-list/vet-list.component';

export const routes: Routes = [
  { path: '', component: WelcomeComponent },
  { path: 'welcome', redirectTo: '', pathMatch: 'full' },
  { path: 'owners', component: OwnerListComponent },
  { path: 'owners/new', component: OwnerFormComponent },
  { path: 'owners/:id', component: OwnerDetailsComponent },
  { path: 'owners/:id/edit', component: OwnerFormComponent },
  { path: 'vets', component: VetListComponent },
  { path: '**', redirectTo: '' }
];
