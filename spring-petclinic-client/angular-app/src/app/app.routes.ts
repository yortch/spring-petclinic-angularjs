import { Routes } from '@angular/router';
import { WelcomeComponent } from './welcome/welcome.component';
import { OwnerListComponent } from './owner-list/owner-list.component';
import { VetListComponent } from './vet-list/vet-list.component';

export const routes: Routes = [
  { path: '', redirectTo: '/welcome', pathMatch: 'full' },
  { path: 'welcome', component: WelcomeComponent },
  { path: 'owners', component: OwnerListComponent },
  { path: 'vets', component: VetListComponent }
];
