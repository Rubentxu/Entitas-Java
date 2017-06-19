package ilargia.entitas.codeGeneration.utils

import ilargia.entitas.codeGeneration.config.CodeGeneratorConfig
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
        CodeGeneratorConfig codeGeneratorConfig = new CodeGeneratorConfig()
        codeGeneratorConfig.setProperties(prop)
        codeGeneratorConfig.setPlugins(new ArrayList<String>()
        {{  add("ilargia.entitas.codeGeneration.utils")
            add("ilargia.entitas.codeGeneration.data")
        }})
        when:
        List<Class<?>> classes = CodeGeneratorUtil.loadTypesFromPlugins(codeGeneratorConfig)

        then:
        classes.size() == 4
    }


}