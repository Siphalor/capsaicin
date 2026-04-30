<!-- modrinth_exclude.start -->

<div align="center">
<img alt="Logo" src="logo-large.png" />

# Capsaicin

![supported Minecraft versions: 1.20 | 1.21](https://img.shields.io/badge/support%20for%20MC-1.20%20%7C%201.21-%2356AD56?style=for-the-badge)

[![latest maven release](https://img.shields.io/maven-metadata/v?color=0f9fbc&metadataUrl=https%3A%2F%2Fmaven.siphalor.de%2Fde%2Fsiphalor%2Fcapsaicin%2Fcapsaicin-mc1.21.10%2Fmaven-metadata.xml&style=flat-square)](https://maven.siphalor.de/de/siphalor/capsaicin/)

This library mod provides hooks to dynamically modify properties of food items at runtime.

**&nbsp;
[Discord](https://discord.gg/6gaXmbj)
&nbsp;**

</div>

<!-- modrinth_exclude.end -->

## Usage

### Setup

```groovy
repositories {
	// ...other maven repositories
	maven { url "https://maven.siphalor.de" }
}

dependencies {
	// Use the latest version from the badge at the top of this README
	modImplementation("de.siphalor.capsaicin:capsaicin-mc1.21.10:1.4.0")
	// Alternatively, you may embed (jar-in-jar) this library with the following dependency configuration
	include(modApi("de.siphalor.capsaicin:capsaicin-mc1.21.10:1.4.0"))
}

```

A brief overview of the supported Minecraft versions and corresponding artifact ids:

| Minecraft Version | Artifact Id           |
|-------------------|-----------------------|
| 1.20–1.20.3       | `capsaicin-mc1.20.4`  |
| 1.20.4            | _Unsupported_         |
| 1.21–1.21.1       | `capsaicin-mc1.21.1`  |
| 1.21.2–1.21.4     | `capsaicin-mc1.21.3`  |
| 1.21.5–1.21.8     | `capsaicin-mc1.21.5`  |
| 1.21.9–1.21.11    | `capsaicin-mc1.21.10` |

### API

The [testmod](src/testmod/java/de/siphalor/capsaicin/testmod/CapsaicinTestmod.java) show some examples of how you might use the API.

The main interface is the [`FoodModifications`](src/main/java/de/siphalor/capsaicin/api/food/FoodModifications.java) which exposes the available hooks.

> **Warning**
> Since the hooks are internally called from the respective Vanilla functions, **you must not use these Vanilla functions inside the hooks**.
> 
> E.g. the `PROPERTIES_MODIFIERS` gets called for the vanilla `stack.get(DataComponents.FOOD)`, so you must not call that function from inside your hook.
> 
> The values are already provided as parameters, so there should be no reason to do so anyway.

## Compatibility

This mod provides a plugin for AppleSkin, so the modified values should be shown correctly.
