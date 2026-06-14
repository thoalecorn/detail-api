# Entity-Relationship Model

## Overview

This document describes the data model used in the Dental Clinic Management API.

The model was designed to support appointment scheduling, patient management, clinic administration, financial tracking, and the reporting requirements defined in the technical challenge.


# Main Entities

## User

Represents authenticated system users.

Supported Roles:

* ADMINISTRATOR
* RECEPTIONIST

Responsibilities:

* Authentication and authorization.

* Clinic assignment.

* Appointment management based on permissions.

## Clinic

Represents a dental clinic belonging to the healthcare network.

Responsibilities:

* Manage offices.

* Manage appointments.

* Associate dentists and receptionists.

* Generate operational and financial indicators.


## Consulting Room

Represents a consultation room within a clinic.

Responsibilities:

* Schedule appointments.

* Manage the number of simultaneous appointments.

Relationship:

* A clinic can have multiple branches.


## Dentist

Represents a dentist who can provide services at one or more clinics.

Responsibilities:

* Schedule appointments.

* Perform dental procedures.

Relationship:

* Many-to-many with clinics through DentistClinic.


## Procedure

Represents a dental procedure offered by the organization.

Attributes:

* Name
* Description
* Cost
* Estimated Duration

Responsibilities:

* Define the appointment cost.

* Define the appointment duration used for scheduling validations.


## Patient

Represents a patient receiving dental services.

Responsibilities:

* Store personal information.

* Record attendance history.

* Record absences and temporary blocks.


## Appointment

Represents a scheduled appointment.

Responsibilities:

* Associate patients, clinic resources, and procedures.

* Record lifecycle statuses.

* Validate availability.

Possible statuses:

* SCHEDULED
* IN PROGRESS
* BEING SEEN
* CANCELLED
* REQUESTED
* PENDING APPROVAL
* REJECTED

## Appointment History

Stores the history of completed appointments.

Responsibilities:

* Maintain operational history.

* Maintain financial history.

* Support the preparation of reports and indicators.

<br>

# Association Entities

## User Clinic

Represents the assignment of receptionists to clinics.

Reason:

A receptionist can belong to several clinics, and a clinic can have several receptionists.

## Dental Clinic

Represents the assignment of dentists to clinics.

Reason:

A dentist can work in several clinics, and a clinic can have several dentists.

<br>

# Main Relationships

Clinic (1) → (N) Office

Clinic (N) ↔ (N) Receptionist
via UserClinic

Clinic (N) ↔ (N) Dentist
via DentistClinic

Patient (1) → (N) Appointment

Office (1) → (N) Appointment

Dentist (1) → (N) Appointment

Procedure (1) → (N) Appointment

Appointment (1) → (N) Appointment History

<br>

# Design Decisions

* The join tables were modeled as entities (UserClinic and DentistClinic) to allow for future extensions.

* Enumerations are persisted using EnumType.STRING.

* The use of Lombok @Data was intentionally avoided to prevent issues with the recursive functions equals(), hashCode(), and toString() in bidirectional relationships.

* Long identifiers were chosen instead of UUIDs for greater simplicity and readability in a centralized relational database architecture.

## Patient Blocking Strategy

Patient blocking is represented through the `blockedUntil` attribute in the Patient entity.

A dedicated counter field for no-show occurrences was intentionally not persisted.

Reason:

The technical requirement specifies that blocking must be evaluated based on no-show occurrences within the previous 90 days.

Because this value can be calculated from appointment records, storing a persistent counter could introduce data inconsistency and synchronization issues.

Instead, the system:

1. Queries appointments marked as `INASISTENCIA`.
2. Evaluates occurrences within the last 90 days.
3. Applies a 30-day block when the threshold is reached.
4. Stores only the block expiration date (`blockedUntil`) in the Patient entity.

Benefits:

* Avoids duplicated data.
* Keeps business rules centralized.
* Prevents counter synchronization problems.
* Allows historical no-show analysis directly from appointment records.
