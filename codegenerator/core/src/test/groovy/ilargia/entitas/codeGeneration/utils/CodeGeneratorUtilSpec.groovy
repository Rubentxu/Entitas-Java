package ilargia.entitas.codeGeneration.utils

import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Title

@Narrative("""
Como usuario de la libreria
Quiero poder tener ciertas utilidades que facilite la labor de generación de código
Para que pueda reutilizar en distintas clases.
""")
@Title(""" """)
@groovy.transform.TypeChecked
class CodeGeneratorUtilSpec extends Specification {

    void 'Buscamos los plugins configurados en las properties'() {
        given:
        Properties prop = new Properties()
        prop.setProperty("CodeGeneration.Plugins.Packages.Scan", "ilargia.entitas.codeGeneration.utils, ilargia.entitas.codeGeneration.data ");

        when:
        List<Class<?>> classes = CodeGeneratorUtil.loadTypesFromPlugins(prop)

        then:
        classes.size() == 4
    }


}