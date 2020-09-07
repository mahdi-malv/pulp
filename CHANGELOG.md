## 0.2.1
* Fix `clearLogs()` not operating on it's thread.

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