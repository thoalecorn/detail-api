# User Registration and Logout

## Overview

This document describes the user registration and logout functionality
added to the Dental Clinic Management API.

---

# User Registration

Only administrators can register new receptionist accounts.

## Flow

1. Administrator sends a POST request to `/auth/register` with a valid JWT.
2. The system validates the request fields and checks for duplicate email.
3. The password is hashed using BCrypt before being persisted.
4. The user is assigned the RECEPTIONIST role automatically.
5. The user is associated with one or more clinics via UserClinic.

## Validations

* `username`: required, max 65 characters.
* `email`: required, valid email format.
* `password`: required, minimum 8 characters.
* `clinicIds`: required, at least one clinic must be provided.

---

# Logout

## Flow

1. The user sends a POST request to `/auth/logout` with their JWT.
2. The system validates the Authorization header format.
3. The token is added to the blacklist with its expiration timestamp.
4. Any subsequent request using that token will be rejected.

## Token Blacklist

* Backed by a ConcurrentHashMap for thread safety.
* Tokens are stored with their expiration timestamp.
* Expired tokens are automatically evicted on access
  to prevent unbounded memory growth.

---

# Endpoints

| Method | Path | Access | Description |
|--------|------|--------|-------------|
| POST | /auth/register | ADMIN only | Register a receptionist user |
| POST | /auth/logout | Authenticated | Invalidate current JWT |

---

# Design Decisions

* Registration is wrapped in @Transactional to ensure user creation
  and clinic associations are committed or rolled back together.
* ResponseStatusException is used instead of RuntimeException
  to return semantically correct HTTP status codes (409 for
  duplicate email, 404 for clinic not found).
* Input validation is handled via Bean Validation annotations
  on the RegisterRequest DTO, failing fast before reaching
  the service layer.