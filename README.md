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

First of all, make sure to have an authorization token for the repo.
For more info, check the docs of Jitpack (https://jitpack.io/docs/PRIVATE/)

Whenever the authentication is setup of Jitpack, add the dependencies to the app build.gradle file
```
implementation "com.wesleydonk.update:update-core:1.0.0"
implementation "com.wesleydonk.update:update-fetcher-pov3:1.0.0"
implementation "com.wesleydonk.update:update-ui:1.0.0"

implementation "com.wesleydonk.update:update-core-no-op:1.0.0"
implementation "com.wesleydonk.update:update-ui-no-op:1.0.0"
```

Force updating by adding a check in the initial activity
```
val url = "<link to configuration file>"
val fetcher = PrinceOfVersionFetcher(this, url)
val storage = RoomStorage(this)
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

## no-op implementation

An empty implementation for production apps will be around any time soon.