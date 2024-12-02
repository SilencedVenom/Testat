plugins {
    id("java")
    id("maven-publish")
}

group = "de.hsw"
version = "1.0-SNAPSHOT"

java {}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/SilencedVenom/Testat")
            credentials {
                username = (project.findProperty("gpr.user") as String?) ?: System.getenv("USERNAME")
                password = (project.findProperty("gpr.token") as String?) ?: System.getenv("TOKEN")
           }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:42.7.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}