# Changelog
## 0.4.3
* Fix `JvmStatic` and `JvmOverloads` to make `Pulp` class able to be used in Java classes without `.INSTANCE` 

## 0.4.0
* Remove default log to logcat. Instead it's mandatory to call `Pulp.addHandler(LogCatListener())` to print logs in logcat.
* Better formatting for LogCat print
* `Pulp.sout()` is marked as deprecated
* `Pulp.setMainTag()` is marked as deprecated

## 0.3.1
* Add null support to data values

## 0.3.0
* Fix `clearLogs()` not operating on it's thread.
* [Breaking] Migrate project to AndroidX.

## 0.2.0
* Disabling log using `Pulp.setLogEnabled`.
* Fix minor bugs.
* Fix extra stack trace while having throwable in the log.
* Added time millisecond to callback.
* Support for database
    Now enabling the database will cause the logs to be saved in a database.
    Log database will provide a `LiveData` stream in order to access to database items.
    It is also possible to clear the saved logs using `Pulp.clearLogs`.
* Fix `Pulp.wtf` logging.

## 0.0.1
* First release for testing purposes.
* Basic functionality.
* Support for handler and log callback.
* Disable log only via manifest.