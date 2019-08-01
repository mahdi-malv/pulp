# Pulp
Pulp is a logger tool for android using **Kotlin** programming language.


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

**More docs will be added**
