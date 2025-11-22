import de.siphalor.jcyo.gradle.JcyoTask
import java.util.*

plugins {
	alias(libs.plugins.loom)
	`java-library`
	`maven-publish`
	alias(libs.plugins.jcyo)
	alias(libs.plugins.modPublisher)
}

val minecraftVersionDescriptor = project.properties["minecraft.version.descriptor"] as String
val mcProps = Properties().apply {
	val propFile = project.layout.settingsDirectory.file("gradle/mc-${minecraftVersionDescriptor}/gradle.properties")
	load(propFile.asFile.inputStream())
}

group = "de.siphalor.${project.name}"
val archivesBaseName = "${project.name}-mc${minecraftVersionDescriptor}"
val shortVersion = "${properties["version"]}"
version = "${shortVersion}+mc${mcLibs.versions.minecraft.get()}"

repositories {
	maven {
		name = "AppleSkin"
		url = uri("https://maven.ryanliptak.com/")
	}
	maven {
		name = "Polymer"
		url = uri("https://maven.nucleoid.xyz")
	}
	maven {
		// Required for some transitive dependencies
		name = "Shedaniel"
		url = uri("https://maven.shedaniel.me/")
	}
	maven {
		// Required for some transitive dependencies
		name = "TerraformersMC"
		url = uri("https://maven.terraformersmc.com/releases")
	}
	maven {
		name = "ParchmentMC"
		url = uri("https://maven.parchmentmc.org")
	}
}

val testmod: SourceSet by sourceSets.creating {
	compileClasspath += sourceSets.main.get().compileClasspath
	runtimeClasspath += sourceSets.main.get().runtimeClasspath
}

loom {
	runs {
		create("testmodClient") {
			client()
			name("Testmod Client")
			source(testmod)
		}
	}
}

dependencies {
	annotationProcessor(libs.lombok)
	compileOnly(libs.lombok)
	testAnnotationProcessor(libs.lombok)
	testCompileOnly(libs.lombok)

	minecraft(mcLibs.minecraft)
	mappings(loom.layered {
		officialMojangMappings()
		parchment("org.parchmentmc.data:parchment-${mcLibs.versions.minecraft.get()}:${mcLibs.versions.parchment.get()}@zip")
	})
	modImplementation(libs.fabric.loader)

	// AppleSkin
	modCompileOnly(mcLibs.appleskin)
	modLocalRuntime(mcLibs.appleskin) {
		exclude(module = "modmenu")
	}

	// Transitive
	modLocalRuntime(mcLibs.cloth.config)
	modCompileOnly(mcLibs.fabric.api)
	modLocalRuntime(mcLibs.fabric.api)

	// Polymer
	modCompileOnly(mcLibs.polymer)

	// Testmod stuff
	"testmodImplementation"(sourceSets.main.map { it.output })
}

tasks.processResources {
    inputs.property("version", project.version)
	inputs.property("minecraft.version.gte", mcProps["minecraft.version.greaterThanOrEqual"])
	inputs.property("minecraft.version.lt", mcProps["minecraft.version.lessThan"])
	inputs.property("extraClientMixins", mcProps["mixins.extra.client"])
	inputs.property("extraCommonMixins", mcProps["mixins.extra.common"])

	filesMatching("fabric.mod.json") {
		expand(
			"version" to project.version,
			"minecraft_version_greaterThanOrEqual" to mcProps.getProperty("minecraft.version.greaterThanOrEqual"),
			"minecraft_version_lessThan" to mcProps.getProperty("minecraft.version.lessThan")
		)
	}

	fun formatExtraMixins(property: String?): String {
		val mixins = property?.split(",")?.map { it.trim() } ?: listOf()
		if (mixins.isEmpty()) return ""
		return "," + mixins.joinToString(",") { "\"$it\"" }
	}
	filesMatching("capsaicin.mixins.json") {
		expand(
			"extraClientMixins" to formatExtraMixins(mcProps.getProperty("mixins.extra.client")),
			"extraCommonMixins" to formatExtraMixins(mcProps.getProperty("mixins.extra.common"))
		)
	}
}

java {
	sourceCompatibility = JavaVersion.toVersion(mcLibs.versions.java.get())
	targetCompatibility = JavaVersion.toVersion(mcLibs.versions.java.get())

	withSourcesJar()
}

val jcyoVars = mcProps.stringPropertyNames()
	.filter { it.startsWith("preprocessor.") }
	.map { it to mcProps[it] }
	.associate { (key, value) -> key.substring("preprocessor.".length) to value.toString() }
val jcyo = tasks.register<JcyoTask>("jcyo") {
	inputDirectory = file("src/main/java")
	variables = jcyoVars
}
val testmodJcyo = tasks.register<JcyoTask>("testmodJcyo") {
	inputDirectory = file("src/testmod/java")
	variables = jcyoVars
}

tasks.compileJava {
	dependsOn(jcyo)
}
tasks.named("compileTestmodJava") {
	dependsOn(testmodJcyo)
}

tasks.jar {
	from("LICENSE") {
		rename { "${it}_${archivesBaseName}" }
	}
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			artifactId = archivesBaseName
			version = shortVersion

			from(components["java"])

			pom {
				name.set("Capsaicin")
				description.set("""
					A library mod that allows to dynamically modify food properties.
					This artifact supports ${mcProps["minecraft.version.greaterThanOrEqual"]} (inclusive) to ${mcProps["minecraft.version.lessThan"]} (exclusive).
				""".trimIndent())
				url.set(project.property("git.url") as String)
				scm {
					url.set(project.property("git.url") as String)
				}
			}
		}
	}

	repositories {
		if (project.hasProperty("siphalor.maven.user")) {
			maven {
				name = "Siphalor"
				url = uri("https://maven.siphalor.de/upload.php")
				credentials {
					username = project.property("siphalor.maven.user") as String
					password = project.property("siphalor.maven.password") as String
				}
			}
		}
	}
}
