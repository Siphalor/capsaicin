import de.siphalor.jcyo.gradle.JcyoTask
import de.siphalor.minecraft_modding_toolkit.gradle.project_plugin.filter.JsonMergeFilterReader

plugins {
	`java-library`
	`maven-publish`
	alias(mcLibs.plugins.smcmtk)
	alias(mcLibs.plugins.fabric.loom)
	alias(libs.plugins.jcyo)
	alias(libs.plugins.modPublisher)
}

val minecraftVersionDescriptor = project.properties["minecraft.version.descriptor"] as String

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

smcmtk {
	useMojangMappings()
	createModConfigurations(listOf(sourceSets.main.get(), testmod))
}

dependencies {
	annotationProcessor(libs.lombok)
	compileOnly(libs.lombok)
	testAnnotationProcessor(libs.lombok)
	testCompileOnly(libs.lombok)

	minecraft(mcLibs.minecraft)
	"modImplementation"(libs.fabric.loader)

	// AppleSkin
	"modCompileOnly"(mcLibs.appleskin)
	"modLocalRuntime"(mcLibs.appleskin) {
		exclude(module = "modmenu")
	}

	// Transitive
	"modLocalRuntime"(mcLibs.cloth.config)
	"modCompileOnly"(mcLibs.fabric.api)
	"modLocalRuntime"(mcLibs.fabric.api)

	// Polymer
	"modCompileOnly"(mcLibs.polymer)

	// Testmod stuff
	"testmodImplementation"(sourceSets.main.map { it.output })
}

tasks.processResources {
    inputs.property("version", project.version)
	inputs.property("minecraft.version.gte", smcmtk.mcProps.getting("minecraft.version.greaterThanOrEqual"))
	inputs.property("minecraft.version.lt", smcmtk.mcProps.getting("minecraft.version.lessThan"))
	inputs.property("extraClientMixins", smcmtk.mcProps.getting("mixins.extra.client"))
	inputs.property("extraCommonMixins", smcmtk.mcProps.getting("mixins.extra.common"))

	filesMatching("fabric.mod.json") {
		filter<JsonMergeFilterReader>(mapOf("merge" to mapOf(
			"version" to project.version,
			"breaks" to mapOf("minecraft" to listOf(
				smcmtk.mcProps.getting("minecraft.version.greaterThanOrEqual").get(),
				smcmtk.mcProps.getting("minecraft.version.lessThan").get()
			))
		)))
	}

	filesMatching("capsaicin.mixins.json") {
		filter<JsonMergeFilterReader>(mapOf("merge" to mapOf(
			"client" to smcmtk.mcProps.getting("mixins.extra.client").get(),
			"mixins" to smcmtk.mcProps.getting("mixins.extra.common").get()
		)))
	}
}

java {
	sourceCompatibility = JavaVersion.toVersion(mcLibs.versions.java.get())
	targetCompatibility = JavaVersion.toVersion(mcLibs.versions.java.get())

	withSourcesJar()
}
val jcyoVars = smcmtk.mcProps.map {
	it.filterKeys { key -> key.startsWith("preprocessor.") }.mapKeys { (key, _) -> key.substring("preprocessor.".length) }
}
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
					This artifact supports \
					${smcmtk.mcProps.getting("minecraft.version.greaterThanOrEqual").get()} (inclusive) to \
					${smcmtk.mcProps.getting("minecraft.version.lessThan")} (exclusive).
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
