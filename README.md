# breakin.dev |  Module Structure

This document describes the module organization of the Breakin backend project.

## Overview

The project follows a layered architecture with clear separation of concerns. Modules are organized into six main groups:

```
modules/
├── applications/      # Executable applications
├── tasks/            # Batch task modules
├── common/           # Reusable technical modules
├── apis/             # External interface layer
├── schema/           # Database schema definitions
└── breakin/          # Breakin-specific business modules
```

---

## Module Groups

### 1. Applications (`applications/`)

> **Deployment Units**: Applications represent independent deployment units.
> You can configure which modules to include based on your hardware resource planning:
> - API applications can selectively scan and register `apis` modules
> - Batch applications can selectively scan and register `tasks` modules
>
> This allows flexible deployment strategies (monolith, microservices, or hybrid).

Executable Spring Boot applications that serve as entry points.

- **`api-application`**: REST API server
  - Handles HTTP requests
  - Integrates API controllers, services, and data layers
  - Runs as standalone web application

- **`batch-application`**: Batch job scheduler
  - Executes scheduled tasks (job crawling, ES sync, etc.)
  - Runs background jobs independently

**Dependencies**: Can depend on any other module groups (apis, breakin, common, schema)

---

### 2. Tasks (`tasks/`)

Self-contained batch task modules that can be reused across applications.

- **`resource-crawl-task`**: Job posting crawler
  - Crawls job postings from various companies (Meta, Google, etc.)
  - Extracts and processes job content
  - Generates structured job data

- **`elasticsearch-sync-task`**: Elasticsearch synchronization
  - Syncs job data to Elasticsearch
  - Keeps search indices up-to-date

**Dependencies**: Can depend on `breakin`, `common` modules

---

### 3. Common (`common/`)

Reusable technical modules that are **not specific to Breakin** and can be used in any project.

- **`logging`**: Structured JSON logging
  - MDC-based context logging
  - Logstash JSON format
  - LoggingFilter for automatic request tracking

- **`openai-base`**: OpenAI API client
  - Abstraction layer for OpenAI integration
  - Reusable across projects

**Dependencies**: Should have minimal dependencies (Spring Boot, third-party libraries only)

---

### 4. APIs (`apis/`)

External interface layer that exposes business logic to the outside world.

- **`api`**: Core REST API
  - Job CRUD operations
  - User management
  - Comments and reactions

- **`search-api`**: Search-specific API
  - Job search endpoints
  - Specialized search operations

**Dependencies**: Depends on `breakin` modules (service, model, etc.)

---

### 5. Schema (`schema/`)

Database schema definitions (DDL/DML scripts).

- Contains SQL migration scripts
- Defines database structure
- Loaded at application startup

**Dependencies**: None (pure SQL resources)

---

### 6. Breakin (`breakin/`)

**Breakin-specific** business modules. These contain the core domain logic unique to the Breakin project.

- **`model`**: Domain models and entities
  - Job, User, Comment, CommunityPost, etc.
  - Value objects and enums

- **`service`**: Business logic services
  - JobService, UserService, CommentService, etc.
  - Domain operations and workflows

- **`exception`**: Domain exceptions
  - Custom exception classes
  - Error handling logic

- **`infrastructure`**: Repository interfaces
  - Port definitions for data access
  - Domain-driven design ports

- **`repository-jdbc`**: JDBC repository implementations
  - Spring Data JDBC implementations
  - SQL queries and data mapping

- **`elasticsearch`**: Elasticsearch integration
  - Job search functionality
  - Document mapping and indexing

- **`auth`**: Authentication and authorization
  - Security configuration
  - JWT handling

- **`outbox`**: Outbox pattern implementation
  - Transactional messaging
  - Event publishing

**Dependencies**: 
- `model`: No dependencies (pure domain)
- `service`: Depends on `model`, `infrastructure`
- `repository-jdbc`, `elasticsearch`: Implement `infrastructure` interfaces
- Others: Depend on relevant `breakin` modules

---

## Dependency Rules

### Allowed Dependencies

```
applications → apis, breakin, tasks, common, schema
tasks → breakin, common
apis → breakin, common
breakin → other breakin modules (following layer rules)
common → (minimal external dependencies only)
schema → (none)
```

### Layer Rules within `breakin/`

```
infrastructure → model
service → model, infrastructure
repository-jdbc → model, infrastructure
elasticsearch → model, infrastructure
auth → model
outbox → model
```

---

## Design Principles

1. **Separation of Concerns**: Each module has a single, well-defined responsibility

2. **Dependency Direction**: Dependencies flow inward toward domain logic
   - APIs depend on services
   - Services depend on repositories
   - Repositories depend on models

3. **Reusability**: 
   - `common` modules are generic and reusable
   - `breakin` modules are project-specific

4. **Modularity**: Modules can be:
   - Tested independently
   - Deployed separately (if needed)
   - Understood in isolation

5. **Clear Boundaries**: 
   - `applications` = entry points
   - `apis` = external interfaces
   - `breakin` = business logic
   - `common` = technical utilities

---

## Adding a New Module

### Where should I add my module?

Ask yourself these questions:

1. **Is it an executable application?** → `applications/`
2. **Is it a background task?** → `tasks/`
3. **Is it generic and reusable across projects?** → `common/`
4. **Is it an external API?** → `apis/`
5. **Is it database schema?** → `schema/`
6. **Is it Breakin-specific business logic?** → `breakin/`

### Example

- New crawler for LinkedIn jobs → `tasks/linkedin-crawl-task`
- New Prometheus metrics module → `common/metrics`
- New admin API → `apis/admin-api`
- New notification service → `breakin/notification`

---

## Module Configuration

Each module is configured in:
- `settings.gradle.kts`: Module inclusion
- `build.gradle.kts`: Module dependencies and plugins

Example:
```kotlin
// settings.gradle.kts
include(":modules:breakin:model")

// build.gradle.kts (in dependent module)
dependencies {
    implementation(project(":modules:breakin:model"))
}
```

---

## Benefits of This Structure

1. **Scalability**: Easy to add new modules without affecting existing ones
2. **Maintainability**: Clear organization makes navigation easy
3. **Testability**: Independent modules can be tested in isolation
4. **Team Collaboration**: Different teams can own different module groups
5. **Microservices Ready**: Modules can be extracted into separate services if needed
6. **Identity**: The `breakin` namespace clearly identifies project-specific code

---

## Related Documentation

- [Gradle Multi-Module Documentation](https://docs.gradle.org/current/userguide/multi_project_builds.html)
- [Spring Boot Multi-Module](https://spring.io/guides/gs/multi-module/)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
