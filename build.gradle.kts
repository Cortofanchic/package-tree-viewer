plugins {
    id("java")
    id("application")
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClass = "org.example.build.RunCLI"
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

var pkg = project.findProperty("PKG") ?: System.getenv("PACKAGE_NAME") ?: ""
var url = project.findProperty("URL") ?: System.getenv("REPO_URL")     ?: ""
var repo = project.findProperty("REPO") ?: System.getenv("REPO_PATH")    ?: ""
var tree = project.findProperty("TREE") ?: System.getenv("TREE_MODE")    ?: ""
var depth = project.findProperty("DEPTH") ?: System.getenv("MAX_DEPTH")    ?: ""
var sub = project.findProperty("SUB") ?: System.getenv("FILTER_SUB")   ?: ""

var runArgs = mutableListOf<String>();
if (!pkg.equals("")) runArgs += "--package=${pkg}"
if (!url.equals("")) runArgs += "--url=${url}"
if (!repo.equals("")) runArgs += "--repo=${repo}"
if (!tree.equals("")) runArgs += "--tree=${tree}"
if (!depth.equals("")) runArgs += "--depth=${depth}"
if (!sub.equals("")) runArgs += "--sub=${sub}"

tasks.named<JavaExec>("run") {
    doFirst { println("[INFO] Launching ${runArgs.size} args") }
    if (runArgs.isNotEmpty()) { args = runArgs }
}

tasks.test {
    useJUnitPlatform()
}
