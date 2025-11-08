## Purpose

This file gives an AI coding agent the minimal, concrete context needed to be productive in this repository: project layout, how to run the app, key design patterns, and known pitfalls to avoid (so PR suggestions are safe and useful).

Quick snapshot
- Backend: Spring Boot (Maven) located in `backend/`.
- Frontend: Vite + React in `frontend/` (see `frontend/.github/copilot-instructions.md` for front-end details).
- Database: Postgres (connection configured in `backend/src/main/resources/application.properties`).

How to run (Windows / PowerShell)
- Backend (from repo root):
  - cd backend
  - .\mvnw.cmd spring-boot:run
  - The app expects a Postgres DB at `localhost:5432` by default; see `backend/src/main/resources/application.properties` for credentials.
- Frontend (from repo root):
  - cd frontend
  - npm install
  - npm run dev

Key files to read first
- `backend/src/main/java/com/example/PersonalHealthTracker/config/MapperConfig.java` — ModelMapper is configured with MatchingStrategies.LOOSE.
- `backend/src/main/java/com/example/PersonalHealthTracker/controllers/RecipeController.java` — shows recipe creation flow: map DTO→entity, null collection before save, persist recipe, then batch-create ingredients and reattach.
- `backend/src/main/java/com/example/PersonalHealthTracker/domain/dto/` and `.../domain/entities/` — DTO vs entity shapes; fields often have different names (e.g. `itemName` vs `ingredientName`).
- `backend/src/main/java/com/example/PersonalHealthTracker/mappers/Impl/` — mappers use `ModelMapper` to map between entities and DTOs.
- `backend/src/main/resources/application.properties` — DB connection and JPA settings.

Important patterns & gotchas (do not change blindly)
- ModelMapper is used broadly with LOOSE matching (see `MapperConfig.java`). This can cause ambiguous mappings for nested properties (example: `IngredientDto.ingredientName` can match both `IngredientEntity.item.itemName` and `IngredientEntity.recipe.recipeName`). Prefer explicit TypeMap or custom converters for nested/collection mappings instead of relying on implicit mapping.
- Recipe creation flow intentionally nulls `recipeIngredients` before saving the parent, then creates ingredients in batch and reattaches them (see `RecipeController.createItem`). Follow that pattern when changing persistence logic.
- Constructor injection is the norm (services, mappers injected via constructors). Some controllers use `@Controller` vs `@RestController`—confirm the intended response handling before converting annotations.
- Several service method names are misspelled (e.g., `creteItem`, `creteRecipe`) — search for these exact names when navigating services; do not rename them without updating all usages and tests.

Database / docker
- A simple `docker-compose.yml` exists under `backend/` to start Postgres (password `changeme123`). Note: the DB username in `application.properties` (`paas`) may not match docker-compose defaults — verify credentials before spinning up the container.

PR / code-change guidance for AI agents
- Prefer minimal, targeted edits. When changing mappings or DTO shapes, update both DTOs and their mappers and add a TypeMap or custom converter instead of relying on loose matching.
- When touching persistence flows (Recipe/Ingredient), preserve the two-step create pattern or provide tests proving the new approach handles cascade and FK logic correctly.
- Use existing service/repository naming conventions; respect the `services/impl` package for implementations.

If you need more context
- Read these files in order: `MapperConfig.java` → `RecipeController.java` → `domain/dto/*` and `domain/entities/*` → `mappers/Impl/*` → `services/impl/*`.
- Frontend-specific guidance is in `frontend/.github/copilot-instructions.md`.

If a section is unclear or you want this shortened/expanded, tell me which area to change and I'll update the file.
