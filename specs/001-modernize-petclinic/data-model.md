# Data Model: Spring Petclinic

**Feature**: 001-modernize-petclinic
**Date**: 2025-01-20
**Purpose**: Document entity relationships, validation rules, and state transitions

## Entity Relationship Diagram

```
┌─────────────────┐       ┌─────────────────┐
│     Owner       │       │    PetType      │
├─────────────────┤       ├─────────────────┤
│ id: Integer     │       │ id: Integer     │
│ firstName: Str  │       │ name: String    │
│ lastName: Str   │       └─────────────────┘
│ address: Str    │               │
│ city: Str       │               │
│ telephone: Str  │               │ type
└─────────────────┘               │
        │                         │
        │ owner                   │
        │ 1                       │
        ▼                         ▼
┌─────────────────────────────────────┐
│                Pet                   │
├─────────────────────────────────────┤
│ id: Integer                         │
│ name: String                        │
│ birthDate: LocalDate                │
│ type: PetType                       │
│ owner: Owner                        │
└─────────────────────────────────────┘
        │
        │ pet
        │ 1
        ▼
┌─────────────────┐       ┌─────────────────┐
│     Visit       │       │    Specialty    │
├─────────────────┤       ├─────────────────┤
│ id: Integer     │       │ id: Integer     │
│ date: LocalDate │       │ name: String    │
│ description:Str │       └─────────────────┘
│ pet: Pet        │               │
└─────────────────┘               │
                                  │ specialties
                                  │ *
                                  ▼
                          ┌─────────────────┐
                          │      Vet        │
                          ├─────────────────┤
                          │ id: Integer     │
                          │ firstName: Str  │
                          │ lastName: Str   │
                          │ specialties:Set │
                          └─────────────────┘
```

## Entity Definitions

### 1. Owner

**Purpose**: Represents a pet owner in the clinic

| Field | Type | Constraints | Notes |
|-------|------|-------------|-------|
| id | Integer | PK, auto-generated | BaseEntity inherited |
| firstName | String | @NotEmpty, max 255 | |
| lastName | String | @NotEmpty, max 255 | |
| address | String | @NotEmpty, max 255 | |
| city | String | @NotEmpty, max 255 | |
| telephone | String | @NotEmpty, @Digits(10) | Format: 10 digits |
| pets | Set<Pet> | @OneToMany, cascade ALL | Ordered by name |

**Validation Rules**:
- All fields required (non-empty)
- Telephone must be exactly 10 numeric digits
- Owner can have multiple pets

**JPA Annotations** (Post-Migration - jakarta.*):
```java
@Entity
@Table(name = "owners")
public class Owner extends Person {
    
    @Column(name = "address")
    @NotEmpty
    private String address;
    
    @Column(name = "city")
    @NotEmpty
    private String city;
    
    @Column(name = "telephone")
    @NotEmpty
    @Digits(fraction = 0, integer = 10)
    private String telephone;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<Pet> pets;
}
```

---

### 2. Pet

**Purpose**: Represents a pet owned by a clinic customer

| Field | Type | Constraints | Notes |
|-------|------|-------------|-------|
| id | Integer | PK, auto-generated | BaseEntity inherited |
| name | String | @NotEmpty, max 255 | |
| birthDate | LocalDate | Optional | Column: birth_date |
| type | PetType | @ManyToOne, required | FK to pet_types |
| owner | Owner | @ManyToOne | FK to owners |
| visits | Set<Visit> | @OneToMany, cascade ALL | Ordered by date desc |

**Validation Rules**:
- Name is required
- Birth date is optional
- Must belong to exactly one owner
- Must have exactly one pet type

---

### 3. PetType

**Purpose**: Lookup table for pet categories

| Field | Type | Constraints | Notes |
|-------|------|-------------|-------|
| id | Integer | PK, auto-generated | NamedEntity inherited |
| name | String | @NotEmpty, max 80 | Unique |

**Predefined Values**:
- cat, dog, lizard, snake, bird, hamster

---

### 4. Visit

**Purpose**: Represents a clinic visit for a pet

| Field | Type | Constraints | Notes |
|-------|------|-------------|-------|
| id | Integer | PK, auto-generated | BaseEntity inherited |
| date | LocalDate | Optional, defaults to today | |
| description | String | @NotEmpty, max 255 | |
| pet | Pet | @ManyToOne | FK to pets |

**Validation Rules**:
- Description is required
- Date defaults to current date if not provided

---

### 5. Vet

**Purpose**: Represents a veterinarian at the clinic

| Field | Type | Constraints | Notes |
|-------|------|-------------|-------|
| id | Integer | PK, auto-generated | Person inherited |
| firstName | String | @NotEmpty, max 255 | Person inherited |
| lastName | String | @NotEmpty, max 255 | Person inherited |
| specialties | Set<Specialty> | @ManyToMany, fetch EAGER | |

**Business Logic**:
- A vet with no specialties is a "general practitioner"
- Specialties ordered by name

---

### 6. Specialty

**Purpose**: Lookup table for veterinarian specializations

| Field | Type | Constraints | Notes |
|-------|------|-------------|-------|
| id | Integer | PK, auto-generated | NamedEntity inherited |
| name | String | @NotEmpty, max 80 | Unique |

**Predefined Values**:
- radiology, surgery, dentistry

---

## Abstract Base Classes

### BaseEntity
```java
@MappedSuperclass
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    public boolean isNew() {
        return this.id == null;
    }
}
```

### NamedEntity extends BaseEntity
```java
@MappedSuperclass
public class NamedEntity extends BaseEntity {
    @Column(name = "name")
    @NotEmpty
    @Size(max = 80)
    private String name;
}
```

### Person extends BaseEntity
```java
@MappedSuperclass
public class Person extends BaseEntity {
    @Column(name = "first_name")
    @NotEmpty
    private String firstName;
    
    @Column(name = "last_name")
    @NotEmpty
    private String lastName;
}
```

---

## State Transitions

### Owner Lifecycle
```
┌─────────┐     create      ┌─────────┐     update      ┌─────────┐
│  None   │ ──────────────► │ Active  │ ──────────────► │ Active  │
└─────────┘                 └─────────┘                 └─────────┘
                                 │
                                 │ add pet
                                 ▼
                            ┌─────────────┐
                            │ Active with │
                            │    Pets     │
                            └─────────────┘
```

### Pet Lifecycle
```
┌─────────┐     create      ┌─────────┐     schedule     ┌───────────────┐
│  None   │ ──────────────► │ Active  │ ───────────────► │ Active with   │
└─────────┘                 └─────────┘      visit       │    Visits     │
                                                         └───────────────┘
```

### Visit Lifecycle
```
┌─────────┐     create      ┌───────────┐
│  None   │ ──────────────► │ Scheduled │
└─────────┘                 └───────────┘
```
*Note: Current implementation has no status field - visits are created and persist*

---

## Database Schema

### Tables

```sql
CREATE TABLE owners (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    address VARCHAR(255),
    city VARCHAR(255),
    telephone VARCHAR(255)
);

CREATE TABLE types (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(80)
);

CREATE TABLE pets (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255),
    birth_date DATE,
    type_id INTEGER NOT NULL REFERENCES types(id),
    owner_id INTEGER NOT NULL REFERENCES owners(id)
);

CREATE TABLE visits (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pet_id INTEGER REFERENCES pets(id),
    visit_date DATE,
    description VARCHAR(255)
);

CREATE TABLE vets (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

CREATE TABLE specialties (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(80)
);

CREATE TABLE vet_specialties (
    vet_id INTEGER NOT NULL REFERENCES vets(id),
    specialty_id INTEGER NOT NULL REFERENCES specialties(id),
    PRIMARY KEY (vet_id, specialty_id)
);
```

---

## Indexes (Recommended)

```sql
CREATE INDEX idx_owners_last_name ON owners(last_name);
CREATE INDEX idx_pets_owner_id ON pets(owner_id);
CREATE INDEX idx_pets_type_id ON pets(type_id);
CREATE INDEX idx_visits_pet_id ON visits(pet_id);
CREATE INDEX idx_vet_specialties_vet_id ON vet_specialties(vet_id);
```
