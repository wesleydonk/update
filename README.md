# Latest

Latest is an SDK to ensure the latest build is being used by clients.

## Publishing

Publishing can be done by running the following command (MavenLocal)

```
./gradlew publishReleasePublicationToMavenLocal
```

## Features

- Configurable SDK to define multiple options (or even custom ones)
- Download and install .apk files directly from within the app itself when needed

## Install

First of all, make sure to have an authorization token for the repo. For more info, check the docs
of Jitpack (https://jitpack.io/docs/PRIVATE/)

Whenever the authentication is setup of Jitpack, add the dependencies to the app build.gradle file

```
implementation "com.wesleydonk.update:update-core:{latest_version}"
implementation "com.wesleydonk.update:update-fetcher-pov3:{latest_version}"
implementation "com.wesleydonk.update:update-fetcher-pov4:{latest_version}"
implementation "com.wesleydonk.update:update-ui:{latest_version}"

implementation "com.wesleydonk.update:update-core-no-op:{latest_version}"
implementation "com.wesleydonk.update:update-ui-no-op:{latest_version}"
```

Force updating by adding a check in the initial activity

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

## Providing the config url in the BuildConfig

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