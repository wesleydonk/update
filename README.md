# Update

A library which helps to reduce the time to support in-app updates outside of the Play Store.  

[![](https://jitpack.io/v/wesleydonk/update.svg)](https://jitpack.io/#wesleydonk/update)

## Publishing

Publishing can be done by running the following command (MavenLocal)

```
./gradlew publishReleasePublicationToMavenLocal
```

## Installation

Add this to your root `build.gradle` file:
```
allprojects {
    repositories {
        maven { url "https://www.jitpack.io" }
    }
}
```

Add the library to the `app/build.gradle` file:
```
implementation "com.github.wesleydonk:update:update-core:{latest_version}"
implementation "com.github.wesleydonk:update:update-fetcher-pov3:{latest_version}"
implementation "com.github.wesleydonk:update:update-fetcher-pov4:{latest_version}"
implementation "com.github.wesleydonk:update:update-ui:{latest_version}"

implementation "com.github.wesleydonk:update:update-core-no-op:{latest_version}"
implementation "com.github.wesleydonk:update:update-ui-no-op:{latest_version}"
implementation "com.github.wesleydonk:update:update-fetcher-no-op:{latest_version}"
```


## Features

- Provide in-app updates
- Separate artifacts for Tryoutapps, which uses [Prince of Versions](https://github.com/infinum/Android-Prince-of-Versions).
- Download and update .apk files directly from within the app itself when required.

## Usage

There is a [sample](https://github.com/wesleydonk/Update/tree/main/sample) available in the repository. All that it requires is:
```
val url = "<link to configuration file>"
val fetcher = PrinceOfVersionFetcher(this, url)
val parser = DefaultParser()

val config = UpdateConfig.Builder()
    .storage(storage)
    .fetcher(fetcher)
    .parser(parser)
    .build(this)

val update = Update.Builder()
    .config(config)
    .build()

lifecycleScope.launch {
    update.getLatestVersion().collect { version ->
        version.showUpdateDialogFragment(supportFragmentManager)
    }
}

update.synchronize()
```

## Connecting to a CI

By providing the config url dynamically from the CI, it can be parsed by the client app. Depending
on the job/workflow that is running on the CI, the CONFIG_URL can be different. An empty string will
be used whenever there is no url set in the environment.

Add the following in the app/build.gradle file:

```
def UPDATE_CONFIG_URL = 'UPDATE_CONFIG_URL'
def STRING = 'String'

android {
    defaultConfig {
        ...
        buildConfigField STRING, UPDATE_CONFIG_URL, '"' + getConfigUrl() + '"'
    }
}

static def getConfigUrl() {
    return System.getenv('CONFIG_URL') ?: ''
}
```

Providing the config url as buildconfig property makes it a breeze to use it in the above example.

Replace
```
val url = "<link to configuration file>"
```

with
```
val url = BuildConfig.UPDATE_CONFIG_URL
```