# Toque Toque

Une application Android moderne et hors-ligne pour gérer ses recettes de cuisine et planifier ses repas de la semaine, développée avec **Jetpack Compose** et **Material Design 3**.

---

## Fonctionnalités

* **Collection de recettes** : Organisez vos créations culinaires par catégories intuitives : *Entrées*, *Plats*, *Desserts* et *Divers*.
* **Gestion détaillée des recettes** :
  * Interface complète de création et d'édition de recettes.
  * Ingrédients groupés par sections (avec quantités, unités et groupes personnalisés comme "Pour la pâte").
  * Instructions étape par étape regroupées par catégories.
  * Suivi des temps de préparation, de cuisson, de réfrigération et de congélation.
  * Galerie de photos associée à chaque recette.
  * Indicateur personnalisé du nombre de portions.
* **Recherche instantanée** : Filtrez rapidement l'ensemble de vos recettes par titre ou mots-clés.
* **Planificateur de repas hebdomadaire** :
  * Chronologie interactive pour planifier vos repas jour par jour.
  * Associez facilement les recettes de votre bibliothèque à des dates précises.
  * Accès rapide au menu du jour.
* **Paramètres pratiques pour la cuisine** :
  * **Mode écran toujours allumé** : Empêche l'écran de votre appareil de s'éteindre pendant que vous cuisinez.
  * **Thème personnalisable** : Prise en charge du mode sombre, du mode clair ou du thème système.
* **Multilingue** : Support natif complet du français (FR) et de l'anglais (EN).

---

## Technologies et Architecture

* **Langage** : 100 % [Kotlin](https://kotlinlang.org/)
* **Interface utilisateur** : [Jetpack Compose](https://developer.android.com/jetpack/compose) avec Material Design 3 (`Material3`)
* **Navigation** : [Jetpack Navigation Compose](https://developer.android.com/guide/navigation)
* **Architecture** : **MVVM** (Model-View-ViewModel) + **Repository Pattern**
* **Stockage local** :
  * [Base de données Room](https://developer.android.com/training/data-storage/room) avec [KSP](https://kotlinlang.org/docs/ksp-overview.html) pour le stockage local avec relations
  * DataStore / SharedPreferences pour la gestion des préférences utilisateur
* **Chargement d'images** : [Coil Compose](https://coil-kt.github.io/coil/compose/)
* **Programmation asynchrone** : Coroutines Kotlin & `StateFlow` / `Flow`
* **Desugaring** : Compatibilité Java 8+ API desugaring (`java.time.LocalDate`, etc.) pour le support des anciennes versions d'Android

---

## Structure du projet

```text
fr.zerohour.toquetoque/
├── data/
│   ├── local/            # Base de données Room, DAOs, Entités, SettingsManager
│   └── repository/       # Implémentations du pattern Repository (Recipe & MealPlan)
├── feature/
│   ├── add/              # Écran d'ajout/édition de recette + ViewModel
│   ├── category/         # Écran de filtrage par catégorie + ViewModel
│   ├── detail/           # Écran de détail d'une recette + ViewModel
│   ├── home/             # Écran principal de collection et recherche + ViewModel
│   └── planner/          # Écran de planification des repas + ViewModel
└── ui/
    ├── navigation/       # Routes de navigation, MainScreen, AppDrawer
    └── theme/            # Couleurs Material 3, typographie (police Inter), formes, thème
```

---

## Prise en main

### Prérequis

* **Android Studio** (Ladybug | 2024.2.1 ou plus récent recommandé)
* **JDK** : Version 17 ou supérieure
* **Android SDK** : API minimale 24 (Android 7.0), API cible 37

### Compilation et exécution

1. **Cloner le dépôt** :
   ```bash
   git clone https://github.com/votre-nom/ToqueToque.git
   cd ToqueToque
   ```

2. **Compiler le projet** :
   Via Gradle en ligne de commande ou depuis Android Studio :
   ```bash
   ./gradlew assembleDebug
   ```

3. **Lancer sur un appareil ou un émulateur** :
   Installez via la ligne de commande (adb), ou dans Android Studio : sélectionnez votre émulateur ou appareil physique et appuyez sur **Run (Maj + F10)**.

---

## Licence

Ce projet est sous licence MIT - consultez le fichier [LICENSE](LICENSE) pour plus de détails.

Copyright (c) 2026 Nathan Boureux.
