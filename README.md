# Pulp
Pulp is a very simple logger tool for android using **Kotlin** programming language.


### Installation

* Add this to your gradle repositories
```groovy
maven { url 'https://dl.bintray.com/mah-d/maven' }
```

* Add the dependency code to your project

```groovy
implementation 'ir.malv.utils:pulp:0.0.1'
```

### Usage

##### Simple usage

```kt
Pulp.info("TAG", "This is a message")
```

##### Add extra data

```kt
Pulp.info("TAG", "Message, but not enough") {
   "ExtraMessage1" to "Message..."
   "ExtraMessage2" to "Message..."
   // ...
}
```

Output will be like:
```
2019-08-01 16:06:40.768 26299-26299/ir.malv.logtest I/Pulp: MainTag:
    Tags: [TAG]
    Message: Message but not enough
    Data:
    	ExtraMessage1	Message...
      ExtraMessage2  Message...
```

##### Add throwable to log

```kt
val t = Throwable("Error")
Pulp.error("TAG", "Failed", t) {
    "Extra data" to "data"
}
```

##### Setting the Log tag

```kt
Pulp.setMainTag("My APP")
```

##### Toggle Enable/Disable Pulp

Disabling the logger can be done using an AndroidManifest meta-data.

```xml
<meta-data android:name="pulp_enabled" android:value="false" />
```
**Note**: To get Pulp extract manifest (since it needs constructor), you need to add this to your `Application` class:

```kt
Pulp.init(this /* context */)
```

##### Listenning to Logs when it happened

When Pulp triggers to print a log, it can be listened using callbacks (If Pulp was disabled via manifest it still can be listened)

```kt
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

```kt
Pulp.setHandlerEnabled(enabled = false)
```

* All config methods can be chained:

```kt
Pulp.init(this)
    .setMainTag("MyApp")
    .setHandlerEnabled(true)
    .addHandler(object : Pulp.LogHandler {
    // ...
})
```
