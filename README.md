<!-- modrinth_exclude.start -->

<div align="center">
<img alt="Logo" src="logo-large.png" />

# Capsaicin

![supported Minecraft versions: 1.18 | 1.19](https://img.shields.io/badge/support%20for%20MC-1.18%20%7C%201.19-%2356AD56?style=for-the-badge)

[![latest maven release](https://img.shields.io/maven-metadata/v?color=0f9fbc&metadataUrl=https%3A%2F%2Fmaven.siphalor.de%2Fde%2Fsiphalor%2capsaicin-1.18%2Fmaven-metadata.xml&style=flat-square)](https://maven.siphalor.de/de/siphalor/capsaicin-1.18/)

This library mod provides hooks to dynamically modify properties of food items at runtime.

**&nbsp;
[Discord](https://discord.gg/6gaXmbj)
&nbsp;**

</div>

<!-- modrinth_exclude.end -->

## Usage

```groovy
repositories {
	// ...other maven repositories
	maven { url "https://maven.siphalor.de" }
}

dependencies {
	// Use the latest version from the badge at the top of this README
	modImplementation("de.siphalor:capsaicin-1.18:1.0.0")
	// Alternatively, you may embed (jar-in-jar) this library with the following dependency configuration
	include(modApi("de.siphalor:capsaicin-1.18:1.0.0"))
}

```

The [testmod](src/testmod/java/de/siphalor/capsaicin/testmod/CapsaicinTestmod.java) show some examples of how you might use the API.

The main interface is the [`FoodModificationRegistry`](src/main/java/de/siphalor/capsaicin/api/food/FoodModificationRegistry.java) which exposes the available hooks.

> **Warning**
> Since the hooks are internally called from the respective Vanilla functions, **you must not use these Vanilla functions inside the hooks**.
> 
> E.g. the `PROPERTIES_MODIFIERS` gets called for the vanilla `getFoodComponent`, so you must not call that function from inside your hook.
> 
> The values are already provided as parameters, so there should be no reason to do so anyway.

## Compatibility

This mod provides a plugin for AppleSkin, so the modified values should be shown correctly.
