# Entitas-Java


![EntitasJavaLogo ](https://raw.githubusercontent.com/Rubentxu/Entitas-Java/master/codegenerator/src/main/resources/img/EntitasJavaLOGO.png "Entitas-Java")


[![Build Status](https://travis-ci.org/Rubentxu/Entitas-Java.svg?branch=master)](https://travis-ci.org/Rubentxu/Entitas-Java)


### Overview
Entitas-Java is a port of [Entitas-CSharp] (https://github.com/sschmid/Entitas-CSharp).

Entitas is a so-called Entity Component System Framework [(ECS)] (http://t-machine.org/index.php/2007/09/03/entity-systems-are-the-future-of-mmog-development-part 1 /).

It is super fast designed specifically for C # and Unity. This port implementation is under Java 8 and uses the power of tools, internal caching, and fast access to components.

Several design decisions have been made to work optimally in a garbage collector environment and facilitate the work to this.

Entitas comes with a code generator that dramatically reduces the amount of code you have to write and makes it much easier to work with the framework.


![EntitasJavaLogo ](https://raw.githubusercontent.com/Rubentxu/Entitas-Java/master/docs/CodeGeneratoApp.png "CodeGenerator Entitas-Java")



The code generator generates classes and methods for you, so you can focus on getting the job done.
Radically reduce the amount of code you have to write and improve readability.
It makes more of a sea code less prone to errors to ensure the best performance.
I highly recommend its use!

The Code Generator has several options, to compile different kinds of help, it is recommended to activate them all.

The Component Folder input field is the source directory where the component classes are, which have to inherit from the Icomponent interface and be annotated with the annotation @Component.

The Generated Folder input field is the target directory of the generated classes, it must be within the project under the src /main/java and src/test/java directories (Replicate the directories as part of the package).

The annotation @Component has the parameters to configure:

    String [] pools () -> an array with the names of the Pools to which the component belongs.

    Boolean isSingleEntity () ---> the component is in a single Instance Entity.

    String customPrefix () -> Currently not implemented

    String [] customComponentName () -> Currently not implemented
    

Some of the classes that can generate children:

> `[NamePool] ComponentIds`: You have the indexes of all components, their names and classes.

> `[NamePool] Matcher`: It has all Matcher relation for each component.

> `Entiy`: Inherits from Entity base and has help methods to manage the components of the entity.

> `[PoolName] Pool`: Inherited from BasePool and has help methods to manage components annotated with isSingleEntity = true.

> `Pools`: Help methods to create the different pools and where to save the instance of all pools.


```
+------------------+
|     Context      |
|------------------|
|    e       e     |      +-----------+
|        e     e---|----> |  Entity   |
|  e        e      |      |-----------|
|     e  e       e |      | Component |
| e            e   |      |           |      +-----------+
|    e     e       |      | Component-|----> | Component |
|  e    e     e    |      |           |      |-----------|
|    e      e    e |      | Component |      |   Data    |
+------------------+      +-----------+      +-----------+
  |
  |
  |     +-------------+  Groups:
  |     |      e      |  Subsets of entities
  |     |   e     e   |  for blazing fast querying
  +---> |        +------------+
        |     e  |    |       |
        |  e     | e  |  e    |
        +--------|----+    e  |
                 |     e      |
                 |  e     e   |
                 +------------+
```

# Different language?
Entitas is also available in
- [C#](https://github.com/sschmid/Entitas-CSharp)
- [Objective-C](https://github.com/wooga/entitas)
- [Clojure](https://github.com/mhaemmerle/entitas-clj)
- [Haskell](https://github.com/mhaemmerle/entitas-haskell)
- [Erlang](https://github.com/mhaemmerle/entitas_erl)
- [Go](https://github.com/wooga/go-entitas)
