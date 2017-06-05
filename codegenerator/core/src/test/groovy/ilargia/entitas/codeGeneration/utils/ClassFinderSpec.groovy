package ilargia.entitas.codeGeneration.utils


import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Title

@Narrative("""
Como usuario de la libreria
Quiero poder obtener las clases que existen bajo un package concreto
Para que pueda usarse en la generacion del codigo.
""")
@Title(""" """)
@groovy.transform.TypeChecked
class ClassFinderSpec extends Specification {


    void 'Buscamos las clases a partir de un paquete base dentro de un jar dentro del classpath'() {

        when:
        List<Class<?>> classes = CodeFinder.findClassRecursive("junit.extensions")

        then:
        classes.size() == 4
    }

    void 'Buscamos las clases a partir de un paquete base dentro de un directorio del classpath'() {

        when:
        List<Class<?>> classes = CodeFinder.findClassRecursive("ilargia.entitas.codeGeneration.data")

        then:
        classes.size() == 2
    }

    void 'Buscamos una clase en concreto dentro del un jar en el classpath'() {

        when:
        Class<?> clazz = CodeFinder.findClass("junit.extensions.TestDecorator.class")

        then:
        clazz.getName() == "junit.extensions.TestDecorator"
    }

    void 'Buscamos una clase en concreto dentro del classpath'() {

        when:
        Class<?> clazz = CodeFinder.findClass("ilargia.entitas.codeGeneration.data.CodeGenFile.class")

        then:
        clazz.getName() == "ilargia.entitas.codeGeneration.data.CodeGenFile"
    }
}