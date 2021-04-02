# Pulp
Pulp is a very simple logger tool for android using **Kotlin** programming language.


## Installation

* Add the dependency code to your project

```groovy
// mavenCentral() is needed
implementation "ir.malv.utils:pulp:$version"
```
version: <img src="https://img.shields.io/maven-central/v/ir.malv.utils/pulp?logo=android"></img>

## Usage

### Simple usage

Initialize a **LogHandler** at the start of application

```kotlin
Pulp.addHandler(LogCatHandler())
```

then use the log functions:

```kotlin
Pulp.info("TAG", "This is a message")
```

#### Add extra data

```kotlin
Pulp.info("TAG", "Message, but not enough") {
   "ExtraMessage1" to "Message..."
   "ExtraMessage2" to "Message..."
   // ...
}
```


#### Add throwable to log

```kotlin
val t = Throwable("Error")
Pulp.error("TAG", "Failed", t) {
    "Extra data" to "data"
}
```


### Advanced usage

#### Listening to Logs when it was triggered

When Pulp triggers to print a log, it can be listened using callbacks.

```kotlin
Pulp.addHandler(object: Pulp.LogHandler {
    override fun onLog(
      level: Pulp.Level,
      tags: List<String>,
      message: String,
      t: Throwable?,
      data: Pulp.LogData
    ) {
        // Get the data and do stuff
    }
})
```

> `LogCatHandler` is an implementation of `LogHandler` which prints to Logcat

**Note**: You can add multiple handlers and all of them will be called when logging.
* Handlers can also be disabled:

```kotlin
Pulp.setHandlerEnabled(enabled = false)
```

#### Enable using database

Pulp can save logs into it's database and return the in a `LiveData` stream.
To enable database call:

```kotlin
Pulp.setDatabaseEnabled(true)
```

And because interacting with database needs context, make sure you have called `Pulp.init(context)`, or `Pulp.setApplicationContext(context)`.

And to interact with database using this code:

```kotlin
val savedLogs: LiveData<List<PulpItems>> = Pulp.getSavedLogs(context)

savedLogs.observe(activity, Observer {
   // it is List<PulpItem>
})
```

and to clear the logs:

```kotlin
Pulp.clearLogs(context)
```

##### Log levels
* `D`: Debug
* `I`: Info
* `W`: Warning
* `E`: Error
* `WTF`: Unexpected


* All config methods can be chained:

```kotlin
Pulp.init(this)
    .setHandlerEnabled(true)
    .setLogsEnabled(true)
    .setDatabaseEnabled(true)
    .addHandler(object : Pulp.LogHandler {
    // ...
})
```
