## Application Overview

This RESTful application is designed to manage **users** and **policies** in a dynamic, rule-based environment. The system supports full **CRUD** (Create, Read, Update, Delete) operations for both users and policies, and **automatically evaluates** which policies apply to each user based on their attributes.

---

## Architecture Overview

The application is divided into the following main layers:

### 1. Controller Layer
- **Purpose**: Handles the REST API endpoints.
- **Responsibilities**:
    - Maps HTTP requests to corresponding service methods.
    - Handles request validation and response formatting.
    - Delegates business logic processing to the business layer.

### 2. Business Layer
- **Purpose**: Encapsulates all business logic.
- **Responsibilities**:
    - Implements the core functionality and rule evaluation logic.
    - Validates and transforms data according to business rules.
    - Acts as an intermediary between the controller and persistence layers.

### 3. Persistence Layer
- **Purpose**: Manages all database interactions.
- **Responsibilities**:
    - Handles data access, storage, and retrieval.
    - Communicates with the database using repositories.
    - Ensures data consistency and integrity.

## Features

- RESTful endpoints for user and policy management.
- Custom policy and rule evaluation engine.
- Layered and modular code structure for better testability and maintenance.

---

## Users

Each user object includes:

- **Basic identity data**:
    - Name
    - First name
    - Last name
    - A unique email address
    - Organization units
    - Birth date
    - Registered on

- A list of **automatically evaluated policy IDs** that apply to the user.

---

## Policies

Policies are **declarative rules** that determine which users they apply to. Each policy includes:

- A **unique ID**
- A **name**
- A **type** defining the rule logic 
  *(e.g., `youngerThan`, `emailDomainIs`, `isMemberOf`)*
- A **condition value(s)**  
  *(e.g., age limit, email domain, or department)*

---

## Dynamic Policy Evaluation

Policies are applied **dynamically**:

- When a **user is created or updated**, applicable policies are **re-evaluated**.
- When a **policy is added or modified**, **all users** are rechecked against it.
- Matching **policy IDs** are listed in the user's `policies` field.

---

## Build prerequisites

Before you build and run the application, make sure you have the following installed:

- [Java 21 JDK](https://jdk.java.net/21/)
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [Lombok plugin](https://projectlombok.org/setup/) installed and annotation processing enabled in your IDE (e.g., IntelliJ IDEA, Eclipse)

> **Important:**  
> This project uses Lombok to reduce boilerplate code.  
> You **must**:
> - Install the Lombok plugin for your IDE
> - **Enable annotation processing** (e.g., in IntelliJ IDEA: `Settings > Build, Execution, Deployment > Compiler > Annotation Processors > Enable annotation processing`)

## Building the Application

To build the project, run:

```bash
mvn clean install
```
# Users & Policies API Tutorial

This document provides a step-by-step guide on how to manage **Users** and **Policies** using REST API endpoints.

---

# Built-in Policies Types

This document outlines the built-in policy types and their structure.

---

## `youngerThan` Type

Matches users whose age is **strictly less** than the given value.

```json
{
  "id": "underaged",
  "name": "Underaged User",
  "condition": {
    "type": "youngerThan",
    "value": 18
  }
}
```

> Note that administrator must define unique id that has not yet been used

> Fields **name**, **value** can be modified to suits rule needs.

---

## `emailDomainIs` Type

Matches users whose email ends with a **specific domain**.

```json
{
  "id": "internal-user",
  "name": "Internal User",
  "condition": {
    "type": "emailDomainIs",
    "value": "evolveum.com"
  }
}
```

> Note that administrator must define unique id that has not yet been used

> Fields **name**, **value** can be modified to suits rule needs.

---

## `isMemberOf` Type

Matches users who are members of a specific **organization unit**.

```json
{
  "id": "developer-access",
  "name": "Developer (Full Access)",
  "condition": {
    "type": "isMemberOf",
    "value": "Software Development"
  }
}
```
> Note that administrator must define unique id that has not yet been used

> Fields **name**, **value** can be modified to suits rule needs.

---

## `ageBetween` Type

Matches users whose age falls within an **inclusive age range**.

Example of definition of rule:
```json
{
  "id": "regular-working",
  "name": "Working Age Group",
  "condition": {
    "type": "ageBetween",
    "min": 18,
    "max": 64
  }
}
```
> Note that administrator must define unique id that has not yet been used

> Fields **name**, *min**, **max** can be modified to suits rule needs.
---

### POST /policies

**Description:** Create a new policy.  
Triggers re-evaluation for affected users.

**Request Body:**
```json
{
  "id": "underaged",
  "name": "Underaged User",
  "condition": {
    "type": "youngerThan",
    "value": 18
  }
}
```

**Responses:**

- `200 OK` – Created policy
```json
{
  "id": "underaged",
  "name": "Underaged User",
  "condition": {
    "type": "youngerThan",
    "value": 18
  }
}
```
- `400 Bad Request` – Request contains validation errors. More details are in the response JSON object.
- `409 Conflict` – Policy with id already exists

> You may also create policies of types: `emailDomainIs` `isMemberOf` and `ageBetween`.

---


### GET /policies

**Description:** Retrieve all defined policies.

**Example:**
```http
GET /policies
```

**Response:**
```json
[
  {
    "id": "underaged",
    "name": "Underaged User",
    "condition": {
      "type": "youngerThan",
      "value": 18
    }
  },
  {
    "id": "internal-user",
    "name": "Internal User",
    "condition": {
      "type": "emailDomainIs",
      "value": "evolveum.com"
    }
  },
  {
    "id": "developer-access",
    "name": "Developer (Full Access)",
    "condition": {
      "type": "isMemberOf",
      "value": "Software Development"
    }
  },
  {
    "id": "regular-working",
    "name": "Working Age Group",
    "condition": {
      "type": "ageBetween",
      "min": 18,
      "max": 64
    }
  }
]
```

---

### GET /policies/{id}

**Description:** Retrieve a specific policy by ID.

**Example:**
```http
GET /policies/developer-access
```

**Responses:**

- `200 OK` – Returns the policy
```json
{
  "id": "developer-access",
  "name": "Developer (Full Access)",
  "condition": {
    "type": "isMemberOf",
    "value": "Software Development"
  }
}
```
- `404 Not Found` – Policy not found

---


### PUT /policies/{id}

**Description:** Update a policy and re-evaluate users.

**Example:**
```http
PUT /policies/developer-access
```

**Request Body:**
```json
{
  "id": "developer-access",
  "name": "SW Development",
  "condition": {
    "type": "isMemberOf",
    "value": "Software Development"
  }
}
```

**Responses:**

- `200 OK` – Policy was updated
```json
{
  "id": "developer-access",
  "name": "SW Development",
  "condition": {
    "type": "isMemberOf",
    "value": "Software Development"
  }
}
```
- `400 Bad Request` – Request contains validation errors. More details are in the response JSON object.
- `404 Not Found` – Policy was not found

---

### DELETE /policies/{id}

**Description:** Delete a policy and remove it from users.

**Example:**
```http
DELETE /policies/developer-access
```

**Responses:**

- `204 No Content` – Policy deleted
- `404 Not Found` – Policy not found

---

### POST /users

**Description:** Create a new user. Policies are automatically evaluated and attached.

**Request Body:**
```json
{
  "name": "John",
  "firstName": "John",
  "lastName": "Doe",
  "emailAddress": "john.doe@evolveum.com",
  "organizationUnit": ["Software Development"],
  "birthDate": "2010-05-15",
  "registeredOn": "2025-08-01"
}
```

**Responses:**

- `201 Created` – Returns the created user with evaluated policies that are in effect right now.
```json
{
  "name": "John",
  "firstName": "John",
  "lastName": "Doe",
  "emailAddress": "john.doe@evolveum.com",
  "organizationUnit": [
    "Software Development"
  ],
  "birthDate": "2010-05-15",
  "registeredOn": "2025-08-01",
  "policy": [
    "underaged",
    "internal-user",
    "developer-access"
  ]
}
```
- `400 Bad Request` – Json body is missing or contains invalid field value. Please check the JSON response body for more information
- `409 Conflict` – User with given email address already exist

> Note that upon creation the member has already assigned policy if user meet the criteria.

---

## Users

### GET /users

**Description:** Retrieve all registered users.

**Example:**
```http
GET /users
```

**Response:**

- `200 OK`: Return a JSON array containing all users along with the IDs of the policies currently applied to them.

```json
[
  {
    "name": "John",
    "firstName": "John",
    "lastName": "Doe",
    "emailAddress": "john.doe@evolveum.com",
    "organizationUnit": [
      "Software Development"
    ],
    "birthDate": "2010-05-15",
    "registeredOn": "2025-08-01",
    "policy": [
      "underaged",
      "internal-user",
      "developer-access"
    ]
  }
]
```

---

### PUT /users/{emailAddress}

**Description:** Update an existing user.  
**Note:** The path `emailAddress` must match the email inside the request body.

**Example:**
```http
PUT /users/jane.doe@gov.com
```

**Request Body:**
```json
{
  "name": "John",
  "firstName": "John",
  "lastName": "Doe",
  "emailAddress": "john.doe@evolveum.com",
  "organizationUnit": ["Software Development"],
  "birthDate": "1980-05-15",
  "registeredOn": "2025-08-01"
}
```

**Responses:**

- `200 OK` – Returns the modified user with evaluated policies that are in effect right now.
```json
{
  "name": "John",
  "firstName": "John",
  "lastName": "Doe",
  "emailAddress": "john.doe@evolveum.com",
  "organizationUnit": [
    "Software Development"
  ],
  "birthDate": "1980-05-15",
  "registeredOn": "2025-08-01",
  "policy": [
    "regular-working",
    "internal-user",
    "developer-access"
  ]
}
```
- `400 Bad Request` – Email mismatch or invalid format. More details are in the response JSON object.
- `404 Not Found` – User does not exist

> Note that we changed the birthDate for this user. The previous policy "underaged" was removed since the user is no longer underage and the "regular-working" was added since the age meet the criteria for this rule.

---

### GET /users/{emailAddress}

**Description:** Retrieve a specific user by email.

**Example:**
```http
GET /users/john.doe@evolveum.com
```

**Responses:**

- `200 OK` – Returns user according to provided email with policies that are in effect right now.
```json
{
  "name": "John",
  "firstName": "John",
  "lastName": "Doe",
  "emailAddress": "john.doe@evolveum.com",
  "organizationUnit": [
    "Software Development"
  ],
  "birthDate": "1980-05-15",
  "registeredOn": "2025-08-01",
  "policy": [
    "regular-working",
    "internal-user",
    "developer-access"
  ]
}
```
- `400 Bad Request` – Provided email is not valid email format  
- `404 Not Found` – User with provided email was not found

---


### DELETE /users/{emailAddress}

**Description:** Delete a user by email.  
**Note:** This operation is irreversible.

**Example:**
```http
DELETE /users/john.doe@example.com
```

**Responses:**

- `204 No Content` – User was removed.
- `400 Bad Request` – Invalid email format
- `404 Not Found` – User not found

---

# Custom Policy Rules

This system supports a plugin-style architecture for defining custom rule logic. Developers or administrators can extend policy functionality by implementing their own `PolicyRule` and `PolicyRuleFactory`.

---

## Policy Rule

Policies are enforced using instances of classes that implements `PolicyRule` interface. The class that implements `PolicyRule` is a self-contained Java class  that encapsulates the logic to determine whether a given user satisfies a specific condition.

```java
import com.fasterxml.jackson.databind.JsonNode;

public interface PolicyRule {
    boolean appliesTo(User user);
    String getType();
    JsonNode getJson();
}
```

Each policy is instantiated by a corresponding `PolicyRuleFactory`, which is responsible for creating rule instances from a JSON definition.

```java
import com.fasterxml.jackson.databind.JsonNode;

public interface PolicyRuleFactory {
    String getType();
    PolicyRule create(JsonNode json);
}
```

## Defining a Custom Rule

To define a new custom rule, you need to create **two classes**:

1. A rule implementation that conforms to `PolicyRule`.
2. A factory implementation that conforms to `PolicyRuleFactory`.

### 1. Rule Implementation

```java
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRule;
import sk.hruby.michal.usersandpolicies.evaluation.User;

import java.time.LocalDate;

@AllArgsConstructor
public class AgeBetweenRule implements PolicyRule {
    private String type;
    private int minAge;
    private int maxAge;
    private JsonNode json;

    @Override
    public boolean appliesTo(User user) {
        int currentAge = LocalDate.now().getYear() - user.getBirthDate().getYear();
        return minAge <= currentAge && currentAge <= maxAge;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public JsonNode getJson() {
        return this.json;
    }
}
```

### 2. Factory Implementation

```java
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRule;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRuleFactory;

@Component
public class AgeBetweenRuleFactory implements PolicyRuleFactory {

    @Override
    public String getType() {
        return "ageBetween";
    }

    @Override
    public PolicyRule create(JsonNode json) {
        int min = json.get("min").asInt();
        int max = json.get("max").asInt();
        return new AgeBetweenRule(this.getType(), min, max, json);
    }
}
```

>**Requirements:**
>- Annotate your factory class with `@Component` to enable automatic registration via Spring.
>- Ensure the `type` returned by `getType()` is **unique** across all rule types.

---

## JSON Representation

At runtime, the system uses the `type` field in the JSON to match the appropriate factory and delegate rule creation. In our case it is `ageBetween`

```json
{
  "id": "regular-working",
  "name": "Working Age Group",
  "condition": {
    "type": "ageBetween",
    "min": 18,
    "max": 64
  }
}
```

---