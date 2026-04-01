import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegistrationComponent } from './components/registration/registration.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RegistrationComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'SkillJobbie Frontend';
}
