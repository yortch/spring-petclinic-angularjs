import { Routes } from '@angular/router';
import { WelcomeComponent } from './components/welcome/welcome';
import { OwnerListComponent } from './components/owner-list/owner-list';
import { VetListComponent } from './components/vet-list/vet-list';

export const routes: Routes = [
  { path: '', redirectTo: '/welcome', pathMatch: 'full' },
  { path: 'welcome', component: WelcomeComponent },
  { path: 'owners', component: OwnerListComponent },
  { path: 'vets', component: VetListComponent },
  { path: '**', redirectTo: '/welcome' }
];
