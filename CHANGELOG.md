# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## Unreleased

### Fixed

- Fixed improper marking of supported Minecraft versions in `fabric.mod.json`.

## [1.4.3] - 2026-06-19

### Fixed

- Fixed handling of `CamoFoodItem`, so that camo foods that are non-edible by itself can now gain edibility.

## [1.4.2] - 2026-04-30

### Fixed
- Separate Minecraft 1.21.5 through 1.21.8 into their own release,
  because intermediary mappings of a mixin target changed due to inheritance changes.

## [1.4.1] - 2026-04-26

### Fixed
- Fixed obsolete static Polymer version restrictions
- Fixed food properties being carried over to incorrect stacks under certain circumstances

## [1.4.0] - 2025-11-22

### Changed
- Changed the complete project structure to a cross-version project setup
- Support is now currently provided for Minecraft 1.20 to 1.21.10
- The following API changes have been made in accordance with Minecraft changes:
  - 1.20.5 to 1.21.1:
    - `FoodModifications.EATING_TIME_MODIFIERS` has become `FoodModifications.EATING_TIME_SECONDS_MODIFIERS` and
      accordingly uses fractional seconds (`float`) instead of ticks (`int`) as its value type.
    - In `FoodProperties` the status effects are now `FoodProperties.PossibleEffect`
      instead of the custom `Pair` implementation.
  - 1.21.2 to 1.21.10:
    - `FoodModifications.EATING_TIME_SECONDS_MODIFIERS` has been removed in favor of
      `FoodModifications.CONSUMABLE_MODIFIERS`. This now uses `ConsumableProperties`.
    - Status effects have been moved from `FoodProperties.PROPERTIES_MODIFIERS` to
      `FoodModifications.CONSUMABLE_MODIFIERS` in accordance with vanilla Minecraft changes.
