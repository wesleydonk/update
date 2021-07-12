# Latest

Latest is an SDK to ensure the latest build is being used by clients.

## Features

- Configurable SDK to define multiple options (or even custom ones)
- Download and install .apk files directly from within the app itself when needed

## Install

Start by adding the dependencies to the app build.gradle file
```
implementation "com.wesleydonk.update:update-core:1.0.0"
implementation "com.wesleydonk.update:update-storage-room:1.0.0"
implementation "com.wesleydonk.update:update-fetcher-tryoutapps:1.0.0"
implementation "com.wesleydonk.update:update-ui:1.0.0"
```

Force updating by adding a check in the initial activity
```
val url = "<link to configuration file>"
val fetcher = TryoutAppsFetcher(this, url)
val storage = RoomStorage(this)
val parser = DefaultParser()

val config = UpdateConfig.Builder()
    .storage(storage)
    .fetcher(fetcher)
    .parser(parser)
    .build(this)

val tryNow = Update.Builder()
    .config(config)
    .build()

lifecycleScope.launch {
    val version = tryNow.checkVersion()
    version?.showTryFragment(activity)
}
```

## no-op implementation

An empty implementation for production apps will be around any time soon.