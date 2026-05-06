# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.
**It is the single source of truth for every session — personal subscription or work subscription.**
Read it fully before writing any code or making any suggestions.

---

## What This Project Is

A **portfolio Android app** called "Baking App" built to impress recruiters.
It is a **complete greenfield rewrite** in modern Kotlin. Every file here was written from scratch —
there is no legacy code to reference or copy from.

**Package name**: `com.tarekbohdima.bakingapp`

---

## Tech Stack — Non-Negotiable

| Concern | Choice | What is forbidden |
|---|---|---|
| Language | Kotlin only | Java |
| UI | Jetpack Compose + Material 3 | XML layouts, Views |
| Navigation | Navigation Compose (single-activity) | XML nav graphs, multiple activities for navigation |
| DI | Hilt | Koin, manual DI |
| Async | Coroutines + StateFlow | RxJava, LiveData |
| Video | Media3 ExoPlayer | old ExoPlayer, VideoView |
| Images | Coil | Picasso, Glide |
| Network | Retrofit + Kotlin Serialization | Gson, Moshi, Volley |
| Local cache | Room (coroutine extensions) | SQLite directly, Realm |
| Widget | Jetpack Compose Glance | RemoteViews widgets |
| Architecture | MVVM — one `UiState` sealed class per screen | MVI, MVP |
| Annotation processing | KSP | kapt |

---

## App Features & Screens (Full Specification)

### 1 — Recipe List Screen
- Grid of recipe cards (name, servings, thumbnail image)
- Tapping a card navigates to the Recipe Detail screen
- Shows a loading spinner while fetching, error state with retry on failure

### 2 — Recipe Detail Screen
- **Adaptive layout**: `maxWidth ≥ 600dp` (tablet/sw600dp) → two-pane side-by-side
  (left: ingredients + steps list; right: embedded step player).
  Narrower → single pane; tapping a step navigates to the Step Player screen.
- Shows the full ingredients list and step list for the selected recipe.

### 3 — Step Player Screen
- `PlayerView` (Media3) plays the step's `videoURL`.
- If `videoURL` is blank, shows `thumbnailURL` image; if that is also blank, shows a text placeholder.
- Step description text below the player.
- Previous / Next buttons to move between steps within the same recipe.

### 4 — Home Screen Widget (Glance)
- Displays the ingredient list for a user-selected recipe.
- User picks the recipe via `WidgetConfigActivity` (launched by the launcher when adding the widget).
- Selected recipe + ingredient list stored in Glance `PreferencesGlanceStateDefinition`.

---

## Data Source

Single network endpoint (fetch once, cache in Room):

```
GET https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json
```

Returns a JSON array of recipes. Domain model:

```kotlin
data class Recipe(val id: Int, val name: String, val servings: Int, val image: String,
                  val ingredients: List<Ingredient>, val steps: List<Step>)
data class Ingredient(val quantity: Double, val measure: String, val ingredient: String)
data class Step(val id: Int, val shortDescription: String, val description: String,
                val videoURL: String, val thumbnailURL: String)
```

`image`, `videoURL`, `thumbnailURL` may be empty strings — always guard with `.isNotBlank()`.

---

## Current Implementation Status

All of the following have been written and the project **compiles and assembles a debug APK**:

- `BakingApp.kt` — `@HiltAndroidApp` Application class
- `MainActivity.kt` — `@AndroidEntryPoint`, hosts `BakingNavGraph`
- **Data layer**: Room entities (`RecipeEntity`, `IngredientEntity`, `StepEntity`, `RecipeWithDetails`),
  `RecipeDao`, `BakingDatabase`, Retrofit `BakingApiService`, DTOs with `@Serializable`, mapping extensions
- **Domain layer**: `Recipe`, `Ingredient`, `Step` data classes (zero Android imports), `RecipeRepository` interface
- **DI**: `DatabaseModule`, `NetworkModule`, `RepositoryModule`
- **Repository**: `RecipeRepositoryImpl` — offline-first (`onStart { if (count() == 0) refresh() }`)
- **Navigation**: type-safe routes (`RecipeList`, `RecipeDetail(recipeId)`, `StepPlayer(recipeId, stepIndex)`)
- **Screens**: `RecipeListScreen`, `RecipeDetailScreen` (adaptive), `StepPlayerScreen` (Media3)
- **ViewModels**: all three, each exposing `StateFlow<*UiState>`
- **Theme**: `BakingAppTheme` (amber/brown palette, dynamic color on Android 12+)
- **Widget**: `IngredientsWidget`, `IngredientsWidgetReceiver`, `WidgetConfigActivity`
- **CI**: `.github/workflows/ci.yml` (Spotless → Detekt → tests → Kover → assembleDebug → bundleRelease on main)
- **Quality**: Spotless (ktlint 1.5.0), Detekt (`config/detekt/detekt.yml`), Kover

**Not yet done** (next tasks):
- GitHub repository creation + issue board setup
- `CHANGELOG.md`
- Professional `README.md` (project overview, screenshots/GIFs, tech-stack table, architecture diagram, build/run instructions, CI status + coverage badges, Play Store link once published)
- Widget visual design pass — current Glance layout is functional but plain; redesign for polish, then refresh `docs/screenshots/widget.png`
- Widget end-to-end test on a real device/emulator
- Instrumented UI tests (Compose test)
- ProGuard rules for Retrofit/Serialization
- Play Store listing assets (icon, screenshots, store description)

---

## Architecture Rules

```
com.tarekbohdima.bakingapp/
├── BakingApp.kt
├── MainActivity.kt
├── data/
│   ├── local/            Room — BakingDatabase, RecipeDao, entity/
│   ├── remote/           Retrofit — BakingApiService, dto/
│   └── repository/       RecipeRepositoryImpl
├── domain/
│   ├── model/            Pure Kotlin data classes — ZERO Android imports allowed here
│   └── repository/       RecipeRepository interface
├── di/                   DatabaseModule, NetworkModule, RepositoryModule
├── ui/
│   ├── navigation/       Screen.kt (routes), BakingNavGraph.kt
│   ├── recipelist/       RecipeListUiState, RecipeListViewModel, RecipeListScreen
│   ├── recipedetail/     RecipeDetailUiState, RecipeDetailViewModel, RecipeDetailScreen
│   ├── stepplayer/       StepPlayerUiState, StepPlayerViewModel, StepPlayerScreen
│   └── theme/            Color.kt, Theme.kt, Type.kt
└── widget/               IngredientsWidget, IngredientsWidgetReceiver, WidgetConfigActivity
```

**UiState pattern** (mandatory for every screen):
```kotlin
sealed interface RecipeListUiState {
    data object Loading : RecipeListUiState
    data class Success(val recipes: List<Recipe>) : RecipeListUiState
    data class Error(val message: String) : RecipeListUiState
}
// ViewModel exposes:
val uiState: StateFlow<RecipeListUiState> = …stateIn(WhileSubscribed(5_000), Loading)
```

**Offline-first data flow**:
`Room Flow` is the single source of truth. Network is only called when the local cache is empty
(or on explicit refresh). Never expose network models to the UI layer.

---

## Program to Abstractions — Hard Rule

Every dependency that could ever be swapped is declared as an **interface**. No exceptions outside DI modules.

| ✅ Inject this | ❌ Never inject this |
|---|---|
| `RecipeRepository` | `RecipeRepositoryImpl` |
| `RecipeDao` | `BakingDatabase` |
| `BakingApiService` | `Retrofit` |
| `Flow<T>` | `LiveData<T>` (LiveData is banned in this project) |

**When adding any new dependency:**
1. Define its interface in `domain/` or `data/` (wherever the contract belongs).
2. Write the implementation in the layer below.
3. Bind with `@Binds` in a `@Module`.
4. Inject only the interface everywhere else.

`@Module` classes are the **sole exemption** — they exist to wire concrete types together.

---

## Hilt — Law of Demeter

Inject the **exact type you need**, not a container that lets you reach further.

```kotlin
// ✅ Correct
@Inject lateinit var dao: RecipeDao

// ❌ Wrong — violates LoD
@Inject lateinit var db: BakingDatabase
fun something() { db.recipeDao().insert(...) }
```

Additional rules:
- Never inject `ApplicationContext` in a business-logic class to call `getSystemService(…)` —
  add a `@Provides` function in a module that extracts the exact system service, then inject that.
- Never pass a Hilt `EntryPoint` or component reference into a non-Hilt class.
  Use `@EntryPoint` at the call site instead.

---

## XML Minimization — Hard Rule

This is a Kotlin-idiomatic project. **Never add an XML file unless the Android OS requires it.**

| XML file | Required because |
|---|---|
| `AndroidManifest.xml` | Build system requirement |
| `res/xml/ingredients_widget_info.xml` | AppWidget provider — OS reads it at install time |
| `res/values/strings.xml` | `appwidget-provider` XML references string resources |
| `res/values/themes.xml` | `android:theme` in Manifest must reference an XML style |

Everything else is Kotlin:
- UI → Jetpack Compose (no `.xml` layouts)
- Navigation → type-safe Compose Navigation (no nav graph XML)
- Theming → `BakingAppTheme` in `Theme.kt`
- Build → `*.gradle.kts` + `libs.versions.toml`

---

## Key Versions & Build Constraints

| Tool | Version | Notes |
|---|---|---|
| AGP | **8.9.1** | Pinned — see constraint below |
| Kotlin | 2.1.21 | |
| KSP | 2.1.21-2.0.2 | Must match Kotlin minor exactly |
| Compose BOM | 2024.12.01 | |
| Hilt | 2.56.2 | |
| Room | 2.7.1 | |
| Navigation Compose | 2.8.9 | 2.8+ required for type-safe routes |
| Media3 | 1.5.1 | |
| Glance | 1.1.1 | |
| JVM target | Java 17 | |

All versions live in `gradle/libs.versions.toml`. **Always use `libs.*` catalog aliases — never hardcode version strings in build files.**

### Why AGP is pinned at 8.9.1 (not 9.x)

AGP 9.x removed `BaseExtension` from its public API. Both `kotlin.android` (KGP) and Hilt's
Gradle plugin cast to `BaseExtension` at configuration time — they fail with a `ClassCastException`
on AGP 9.x. AGP 8.9.1 is the minimum that:
- Supports `core-ktx 1.18.0` (requires AGP ≥ 8.9.1 and `compileSdk 36`)
- Still exposes `BaseExtension` (so Hilt and `kotlin.android` work)

**`android.builtInKotlin=false`** is set in `gradle.properties`.
AGP 8.8+ enables "built-in Kotlin" by default — KSP's Gradle plugin tries to register generated
sources via `kotlin.sourceSets`, which AGP rejects in built-in-Kotlin mode.
Disabling it makes KSP use the explicit `kotlin.android` path instead.

**Upgrade path**: When upgrading AGP to 9.x in the future —
1. Verify that KGP (`kotlin.android`) has been updated to use `ApplicationExtension` instead of `BaseExtension`.
2. Verify that Hilt's plugin has the same fix.
3. Remove `android.builtInKotlin=false` from `gradle.properties`.
4. Remove the explicit `kotlin-android` plugin from all `build.gradle.kts` files (AGP 9.x provides it via built-in Kotlin).

### JVM target

Set via the Kotlin 2.x `compilerOptions` DSL (the old `kotlinOptions` block is deprecated and
errors in Kotlin 2.x):
```kotlin
kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}
```
`compileOptions { sourceCompatibility/targetCompatibility = VERSION_17 }` in the `android {}` block
covers the Java side.

---

## Build Commands

```bash
./gradlew assembleDebug                    # debug APK
./gradlew assembleRelease                  # signed release APK (needs env vars below)
./gradlew bundleRelease                    # release AAB for Play Store
./gradlew test                             # JVM unit tests
./gradlew test --tests "com.tarekbohdima.bakingapp.RecipeRepositoryTest"
./gradlew connectedAndroidTest             # instrumented tests (device/emulator required)
./gradlew spotlessCheck                    # formatting check (runs on CI)
./gradlew spotlessApply                    # auto-fix formatting — run before every commit
./gradlew detekt                           # static analysis
./gradlew koverXmlReport                   # coverage XML → build/reports/kover/
./gradlew lint
./gradlew clean assembleDebug              # clean build
```

Release signing reads from environment variables:
`KEYSTORE_PATH`, `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`.
In CI these come from repository secrets (see below).

---

## Git Strategy

**Branch naming:**
- `feature/<issue-number>-short-description`
- `fix/<issue-number>-short-description`
- `chore/<description>`
- `docs/<description>`
- `release/<versionName>`

**Non-negotiable rules:**
- Every PR must be linked to a GitHub Issue with `Closes #<n>` in the PR body. **No unlinked PRs.**
- PRs target `main`. `main` is protected — CI must pass before merge.
- Merge strategy: squash-merge (keeps `main` history linear).
- PR template lives at `.github/pull_request_template.md`.
- Issue templates: `.github/ISSUE_TEMPLATE/bug_report.yml` and `feature_request.yml`.

---

## Tag Strategy

Every Play Store release on `main` is tagged. Tags also serve as CI triggers for the release job.

```bash
# After bumping versionCode/versionName and merging the release PR:
git tag v1.0.0 -m "Release 1.0.0 — initial launch"
git push origin v1.0.0
```

| Pattern | Use |
|---|---|
| `v{major}.{minor}.{patch}` | Production release |
| `v{major}.{minor}.{patch}-rc.{n}` | Release candidate (internal / closed testing track) |
| `v{major}.{minor}.{patch}-alpha.{n}` | Alpha / internal testing |

Rules:
- Write `CHANGELOG.md` entry **before** tagging.
- Never delete or move a published tag — create a new one if there is a mistake.
- Tags on `main` automatically trigger the release CI job (`bundleRelease` → signed AAB → artifact upload).

---

## CI / CD

Pipeline file: `.github/workflows/ci.yml`

**On every push / PR:**
Spotless → Detekt → unit tests → Kover coverage → `assembleDebug` → upload APK artifact (7-day retention)

**On push to `main` only:**
`bundleRelease` → upload AAB artifact (30-day retention)

**GitHub repository secrets required:**
| Secret | Purpose |
|---|---|
| `KEYSTORE_BASE64` | Base64-encoded `.jks` file (encode with `base64 -i release.jks`) |
| `KEYSTORE_PASSWORD` | Store password |
| `KEY_ALIAS` | Key alias |
| `KEY_PASSWORD` | Key password |
| `CODECOV_TOKEN` | Coverage upload (optional) |

---

## Google Play Release Checklist

1. Increment `versionCode` (always +1) and set `versionName` in `app/build.gradle.kts`.
2. Write the `CHANGELOG.md` entry for this version.
3. Open a PR titled `release: v{versionName}`, link a release issue, merge to `main`.
4. CI builds and uploads the signed AAB artifact.
5. Download the AAB. Upload to Play Console: **Internal → Closed → Open → Production** (use staged rollout).
6. Tag: `git tag v{versionName} -m "…" && git push origin v{versionName}`.

---

## Code Quality Rules

### Spotless (formatting)
- Tool: ktlint 1.5.0, line-length 120
- Config: root `build.gradle.kts` → `spotless {}` block
- **Run `./gradlew spotlessApply` before every commit.** CI blocks on `spotlessCheck` failure.
- Composable function naming (`PascalCase`) is exempt from ktlint's `function-naming` rule.

### Detekt (static analysis)
- Config: `config/detekt/detekt.yml`
- `FunctionNaming` is suppressed for `@Composable` functions.
- `MagicNumber` is disabled — numeric literals in Compose layout code are acceptable.
- `LongMethod` threshold is 60 lines; Compose screens may need a suppression if unavoidable.

### Kover (coverage)
- Reports: `build/reports/kover/`
- Uploaded to Codecov on CI (token: `CODECOV_TOKEN` secret).
- Room schema files in `app/schemas/` are **committed** intentionally — they are the database migration audit trail.

---

## Gradle KTS / Version Catalog Conventions

(Based on android/skills patterns and Gradle KTS best-practices.)

- **Version catalog bundles** (`libs.bundles.*`) group related deps — defined in `libs.versions.toml`.
  Example: `libs.bundles.retrofit`, `libs.bundles.room`, `libs.bundles.media3`.
- **Lazy task registration**: always `tasks.register { }` not `tasks.create { }`.
- **Type-safe project accessors**: enable in `settings.gradle.kts` with
  `enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")` before adding a second module.
- **Convention plugins in `build-logic/`**: extract shared build config here when a second module is added.
- `ksp {}` block in `app/build.gradle.kts` exports Room schemas to `app/schemas/` — keep it.
- Never hardcode a version string in any `*.gradle.kts` — use the catalog alias.
