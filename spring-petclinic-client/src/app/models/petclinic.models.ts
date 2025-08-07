export interface Owner {
  id?: number;
  firstName: string;
  lastName: string;
  address: string;
  city: string;
  telephone: string;
  pets?: Pet[];
}

export interface Pet {
  id?: number;
  name: string;
  birthDate: Date;
  type: PetType;
  visits?: Visit[];
}

export interface PetType {
  id: number;
  name: string;
}

export interface Visit {
  id?: number;
  date: Date;
  description: string;
}

export interface Vet {
  id: number;
  firstName: string;
  lastName: string;
  specialties?: Specialty[];
}

export interface Specialty {
  id: number;
  name: string;
}
