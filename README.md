# Pulp
Pulp is a very simple logger tool for android using **Kotlin** programming language.


## Installation

* Add the dependency code to your project

```groovy
implementation "ir.malv.utils:pulp:$version"
```
<img src="https://img.shields.io/bintray/v/mah-d/maven/pulp?color=blue&style=plastic"></img>

Or include the pre-release version

* Add this to your gradle repositories
```groovy
maven { url 'https://dl.bintray.com/mah-d/preview' }
```

## Usage

### Simple usage

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

**Note**: You can pass an array of tags instead of one:
```kotlin
Pulp.warn(arrayOf("Be careful", "MyApp"), "Message", throwable)
```

Output will be like:
```
2019-08-01 16:06:40.768 26299-26299/ir.malv.logtest I/Pulp: MainTag:
    Tags: [TAG]
    Message: Message but not enough
    Data:
     ExtraMessage1  Message...
     ExtraMessage2  Message...
```

#### Add throwable to log

```kotlin
val t = Throwable("Error")
Pulp.error("TAG", "Failed", t) {
    "Extra data" to "data"
}
```

#### Setting the Log tag

```kotlin
Pulp.setMainTag("My APP")
```

### Advanced usage

#### Toggle Enable/Disable Pulp

```kotlin
Pulp.setLogsEnabled(false)
```

Disabling the logger can also be done using an AndroidManifest meta-data.

```xml
<meta-data android:name="pulp_enabled" android:value="false" />
```
**Note**: To get Pulp extract manifest (since it needs constructor), you need to add this to your `Application` class:

```kotlin
Pulp.init(this /* context */)
```

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

#### Print a simple message (not a Pulp log)
When you don't want to send callback or follow the message style of pulp, you can print a simple message.

```kotlin
Pulp.sout("Message") // output: MainTag ### Message
```
It will not notify callbacks.


* All config methods can be chained:

```kotlin
Pulp.init(this)
    .setMainTag("MyApp")
    .setHandlerEnabled(true)
    .setLogsEnabled(true)
    .setDatabaseEnabled(true)
    .addHandler(object : Pulp.LogHandler {
    // ...
})
```
