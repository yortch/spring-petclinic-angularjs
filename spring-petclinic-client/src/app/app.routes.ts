import { Routes } from '@angular/router';
import { HomeComponent } from './home/home';
import { OwnersComponent } from './owners/owners';
import { VetsComponent } from './vets/vets';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'owners', component: OwnersComponent },
  { path: 'owners/new', component: OwnersComponent },
  { path: 'vets', component: VetsComponent },
  { path: '**', redirectTo: '' }
];
