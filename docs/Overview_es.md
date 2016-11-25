# Entitas-Java


![EntitasJavaLogo ](https://raw.githubusercontent.com/Rubentxu/Entitas-Java/master/codegenerator/src/main/resources/img/EntitasJavaLOGO.png "Entitas-Java")

### Resumen
Entitas-Java es un port  de [Entitas-CSharp](https://github.com/sschmid/Entitas-CSharp).
Entitas es un denominado Entity Component System Framework ([ECS](http://t-machine.org/index.php/2007/09/03/entity-systems-are-the-future-of-mmog-development-part-1/))
es super rápido diseñado específicamente para C # y Unity. 
Esta implementacion del port esta bajo Java 8 y pretende usar el poder de las lambdas, el almacenamiento en caché interno y el rápido acceso a los componentes.
Se han tomado varias decisiones de diseño para trabajar óptimamente en un entorno con garbage colector y facilitar el trabajo a este. 
Entitas viene con un generador de código opcional que reduce radicalmente la cantidad de código que tiene que escribir y le facilita mucho el trabajo con el framework.

![EntitasJavaLogo ](https://raw.githubusercontent.com/Rubentxu/Entitas-Java/master/docs/CodeGeneratoApp.png "CodeGenerator Entitas-Java")


El generador de código genera clases y métodos para usted, por lo que puede centrarse en realizar el trabajo. 
Reduce radicalmente la cantidad de código que tiene que escribir y mejora la legibilidad. 
Hace que su código sea menos propenso a errores mientras garantiza el mejor rendimiento. 
Recomiendo encarecidamente su uso!

El Generador de Codigo tiene varias opciones, para compilar diferentes clases de ayuda, se recomienda activarlas todas.
El campo de entrada Component Folder es el directorio origen donde esta las clases de componentes, que tienen que heredar de la interfaz Icomponent y estar anotadas con
la anotacion @Component.
El campo de entrada Generated Folder es el directorio destino de las clases generadas, deben estar dentro del proyecto bajo los directorios src/main/java ó src/test/java
(replicara la los directorios como parte del package).

La anotacion @Component tiene los parametros a configurar:

    String[] pools()  --> un array con los nombres de los Pools a los que pertenece el componente.

    boolean isSingleEntity() ---> si el componente estara en una Entidad de instancia unica.

    String customPrefix() --> De momento sin implementar

    String[] customComponentName()  --> De momento sin implementar



Algunas de las clases que puedes generar son:

> `[NombrePool]ComponentIds` : Tienes los indices de todos los componentes, sus nombres y clases.
> `[NombrePool]Matcher` : Tiene todos la relacion de los matcher para cada componente.
> `Entiy`  :  Hereda de Entity base y tiene metodos de ayuda para gestionar los componentes de la entidad.
> `[NombrePool]Pool`  :  Hereda de BasePool y tiene metodos de ayuda para gestionar los componentes anotados con isSingleEntity=true .
> `Pools`  :  Metodos de ayuda para crear los diferentes pools y donde se guarda la instancia de todos los pools.




[![Build Status](https://travis-ci.org/Rubentxu/Entitas-Java.svg?branch=master)](https://travis-ci.org/Rubentxu/Entitas-Java)
# Overview
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
