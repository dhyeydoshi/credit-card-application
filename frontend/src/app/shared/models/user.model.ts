export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
}

export interface UserRegistration {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phoneNumber: string;
  socialSecurityNumber: string;
  dateOfBirth: string;
  streetAddress: string;
  city: string;
  state: string;
  postalCode: string;
}

export interface CreditApplication {
  annualIncome: number;
  employmentStatus: string;
  employerName: string;
  yearsAtJob: number;
  monthlyRent: number;
  housingStatus: string;
}

export interface AuthResponse {
  message: string;
  token: string;
  user: User;
}
