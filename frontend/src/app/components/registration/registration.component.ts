import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { StudentService } from '../../services/student.service';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  providers: [StudentService],
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {
  registrationForm: FormGroup;
  isSubmitting = false;
  successMessage = '';
  errorMessage = '';

  courses = [
    'Java Full Stack',
    'Python Full Stack'
  ];

  constructor(private fb: FormBuilder, private studentService: StudentService) {
    this.registrationForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      course: ['', Validators.required]
    });
  }

  get f() {
    return this.registrationForm.controls;
  }

  onSubmit() {
    this.successMessage = '';
    this.errorMessage = '';

    if (this.registrationForm.invalid) {
      this.registrationForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    
    this.studentService.registerStudent(this.registrationForm.value).subscribe({
      next: (response) => {
        this.isSubmitting = false;
        this.successMessage = 'Registration successful! Welcome to SkillJobbie.';
        this.registrationForm.reset();
        this.registrationForm.markAsPristine();
        this.registrationForm.markAsUntouched();
        
        // Hide success message after 5 seconds
        setTimeout(() => {
          this.successMessage = '';
        }, 5000);
      },
      error: (error) => {
        this.isSubmitting = false;
        console.error('Registration error', error);
        
        if (error.error && error.error.details) {
            this.errorMessage = error.error.message;
        } else if (error.error && typeof error.error === 'object') {
             // Handle validation errors from backend
            let msgs = Object.values(error.error).join(', ');
            this.errorMessage = msgs ? msgs : 'Registration failed. Please try again later.';
        } else {
            this.errorMessage = 'Registration failed. Please check your connection and try again.';
        }
      }
    });
  }
}
