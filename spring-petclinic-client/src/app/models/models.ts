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
  birthDate: string;
  type: PetType;
  visits?: Visit[];
  owner?: Owner;
}

export interface PetType {
  id: number;
  name: string;
}

export interface Visit {
  id?: number;
  date: string;
  description: string;
  petId?: number;
}

export interface Vet {
  id: number;
  firstName: string;
  lastName: string;
  specialties: Specialty[];
}

export interface Specialty {
  id: number;
  name: string;
}
