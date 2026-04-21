## Plan: Align User entity with account and invitation use cases

TL;DR: The current `backend/src/main/java/edu/tcu/projectpulse/user/User.java` only contains login fields and lacks the explicit email, names, invitation, and status data required by UC-11, UC-18, UC-25, UC-26, and UC-30. Update the entity to support exact account setup and invite workflows.

**Steps**
1. Modify `User.id` to use generated primary key behavior with `@GeneratedValue(strategy = GenerationType.IDENTITY)`.
2. Add explicit profile fields: `firstName`, `lastName`, and `email`.
3. Add validation and unique constraints on `email`:
   - `@NotEmpty(message = "email is required.")`
   - `@Email(message = "email must be valid.")`
   - `@Column(unique = true)`
4. Decide whether `username` should remain or be replaced by `email` as the login identifier. The use cases strongly favor `email` as the primary account identifier for registration and invitation.
5. Add invitation/account-state support fields:
   - `String invitationToken`
   - `LocalDateTime invitationTokenExpiry` or `Instant invitationTokenExpiry`
   - `String accountStatus` or a small enum like `INVITED`, `ACTIVE`, `DEACTIVATED`
6. Improve role storage by using the existing `UserRole` type or a cleaner representation, instead of a space-separated `roles` string.

**Relevant files**
- `backend/src/main/java/edu/tcu/projectpulse/user/User.java` — primary entity to modify
- `backend/src/main/java/edu/tcu/projectpulse/user/UserRepository.java` — may need `findByEmail` if `email` becomes login
- `backend/src/main/java/edu/tcu/projectpulse/user/UserRole.java` — currently empty; can be used for role enums
- `backend/src/main/java/edu/tcu/projectpulse/user/UserService.java` — will likely need methods for invitations/account creation, though not directly requested

**Verification**
1. Confirm `User` compiles after adding new fields and annotations.
2. Ensure `UserRepository` query methods still work or are updated for `email` lookup.
3. Run backend unit tests if available, especially any user/auth tests.
4. Manually review the new fields against use cases: first name, last name, email, invitation flow, and active/deactivated account behavior.

**Decisions**
- `User` should include explicit email and name fields because the use cases require account setup, invitation email, and profile edits.
- `enabled` currently exists and aligns with UC-23/UC-24, but an invitation/account status enum is more precise than just `enabled`.
- `username` should either be deprecated or mapped to `email`; the use cases do not describe a separate arbitrary username.

**Further Considerations**
1. If the app uses a separate `Student`/`Instructor` profile later, consider keeping only identity/auth fields in `User` and moving roles-specific data elsewhere.
2. Add `@Email` validation and unique email constraints before user creation to prevent duplicate invitation/account records.
3. Use the empty `UserRole.java` to define roles such as `ADMIN`, `STUDENT`, and `INSTRUCTOR` in a type-safe way.
