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
        List<Class<?>> classes = CodeFinder.findClassRecursive("org.jboss.forge.roaster.model.util")

        then:
        classes.size() == 9
    }

    void 'Buscamos las clases a partir de un paquete base dentro de un directorio del classpath'() {

        when:
        List<Class<?>> classes = CodeFinder.findClassRecursive("ilargia.entitas.codeGeneration.data")

        then:
        classes.size() == 5
    }

    void 'Buscamos una clase en concreto dentro del un jar en el classpath'() {

        when:
        Class<?> clazz = CodeFinder.findClass("org.jboss.forge.roaster.model.util.DesignPatterns.class")

        then:
        clazz.getName() == "org.jboss.forge.roaster.model.util.DesignPatterns"
    }

    void 'Buscamos una clase en concreto dentro del classpath'() {

        when:
        Class<?> clazz = CodeFinder.findClass("ilargia.entitas.codeGeneration.plugins.data.MethodData.class")

        then:
        clazz.getName() == "ilargia.entitas.codeGeneration.plugins.data.MethodData"
    }
}