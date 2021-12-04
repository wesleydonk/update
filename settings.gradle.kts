rootProject.name = "Update"
include(":sample")
include(":sample-automation")
include(":update-core")
include(":update-core-no-op")
include(":update-ui")
include(":update-ui-no-op")
include(":update-ui-automation")
include(":update-ui-automation-no-op")
include(":update-fetcher-pov3")
include(":update-fetcher-pov4")
include(":update-fetcher-no-op")

// Enable Gradle's version catalog support
// https://docs.gradle.org/current/userguide/platforms.html
enableFeaturePreview("VERSION_CATALOGS")
