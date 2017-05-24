package ilargia.entitas.codeGeneration.dataProviders.context

import groovy.transform.TypeCheckingMode
import ilargia.entitas.codeGeneration.data.SourceDataFile
import ilargia.entitas.fixtures.components.FixtureProvider
import spock.lang.Narrative
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title

import static ilargia.entitas.codeGeneration.dataProviders.context.ContextDataProvider.getContextName;

@Narrative("""
Como usuario de la aplicacion
Quiero poder obtener los datos necesarios del proveedor de datos de Contexto
Para que pueda usarse en la generacion del codigo.
""")
@Title(""" """)
@groovy.transform.TypeChecked
class ContextDataProviderSpec extends Specification {

    @Shared
    FixtureProvider fixtures = new FixtureProvider("src/test/groovy/ilargia/entitas/fixtures/components")
    @Shared
    ContextDataProvider contextDataProvider

    def setupSpec() {
        contextDataProvider = new ContextDataProvider()

    }


    @groovy.transform.TypeChecked(TypeCheckingMode.SKIP)
    void 'Consultamos al proveedor ContextDataProvider por los contextos extraidos de la configuracion'() {
        given:
        Properties prop =  new Properties()
        contextDataProvider.configure(prop)

        when:
        contextDataProvider.getDefaultProperties()
        List<HashMap<String, String>> datas = contextDataProvider.getData()

        then:
        datas.size() == 1
        datas.get(0).get("context_name") == "Core"

    }
}