import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavigationComponent } from './navigation/navigation';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavigationComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('spring-petclinic-client');
}
