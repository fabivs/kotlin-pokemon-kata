# Pokemon Kata (Kotlin)

## Project setup

### Without Docker

Requirements: JDK, Gradle.

```
$ .\gradlew.bat build
$ .\gradlew.bat run
```

The APIs will be available locally at: http://127.0.0.1:8080.

### With Docker

...

## Assignment

Define two REST endpoints.

### API Endpoint 1

`/HTTP/GET /pokemon/<pokemon name>`

The endpoint receives a pokemon name (as id), the application obtains the information with an API call to
the https://pokeapi.co/ server, and returns a response.

About pokeapi:

- most of what's needed is under the `pokemon-species` API
- the description can be found in the `flavor_text` array, use any of the english descriptions.

Example call: `GET http://localhost:5000/pokemon/mewtwo`
Response should be:

- Pokemon name
- Pokemon standard description
- Pokemon habitat
- `is_legendary` boolean

Example response:

```
{
    "name": "mewtwo",
    "description": "It was created by a scientist ...",
    "habitat": "rare",
    "isLegendary": true
}
```

### API Endpoint 2

`HTTP/GET /pokemon/translated/<pokemon name>`

This endpoint will "translate" the pokemon description and other basic info.

1) If the `habitat` is "cave" OR if `legendary` is true, apply the Yoda translation
2) For every other pokemon, use the Shakespeare translation
3) If *for whatever reason* (exception handling), the translation can't be obtained, return the standard description.

We will first call the PokéAPI (https://pokeapi.co/) and then the FunTranslations API
(https://funtranslations.com) to obtain the translated description.

https://funtranslations.com/api/shakespeare
https://funtranslations.com/api/yoda

Response should be:

- Pokemon name
- Pokemon standard description
- Pokemon habitat
- `is_legendary` boolean

Example response:

```
{
    "name": "mewtwo",
    "description": "Created by a scientist ... it was.",
    "habitat": "rare",
    "isLegendary": true
}
```

# Solution

The project has been implemented in Kotlin with Ktor (a simple and minimalistic web framework).
Dependency injection and Use case patterns are used to decouple the business logic from the external systems as much as
possible.

- REST endpoints are defined in the `Routing.kt` file.
- Use cases are defined in the `usecase` package and contain the core business logic of the application.
- Clients to access external services are defined via an interface in `domain` and then implemented in
  the classes inside the `infrastructure` package.
- Dependency injection is performed via the `DependencyContainer` file.

Tests:

- `ApplicationTest` contains the acceptance E2E tests
- Integrations tests are under the `infrastructure` package
- Unit tests are the tests under the `usecase` package

## Differences between actual production code

- in `DependencyContainer`, the base URLs for the external services would be environment variables instead of hardcoded
- tests would've been separated into different directories (unit, integration, e2e) and they would've been tagged
  appropriately so that they could be run separately with different commands, but for the purposes of this exercise, the
  tests were too few, and it was not worth the effort.