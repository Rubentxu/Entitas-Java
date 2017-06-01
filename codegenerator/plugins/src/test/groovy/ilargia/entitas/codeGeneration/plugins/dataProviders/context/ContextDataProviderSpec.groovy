package ilargia.entitas.codeGeneration.plugins.dataProviders.context

import groovy.transform.TypeCheckingMode
import ilargia.entitas.fixtures.FixtureProvider
import spock.lang.Narrative
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title

@Narrative("""
Como usuario de la aplicacion
Quiero poder obtener los datos necesarios del proveedor de datos de Contexto
Para que pueda usarse en la generacion del codigo.
""")
@Title(""" """)
@groovy.transform.TypeChecked
class ContextDataProviderSpec extends Specification {

    @Shared
    FixtureProvider fixtures = new FixtureProvider("src/test/java/ilargia/entitas/fixtures/components")
    @Shared
    ContextDataProvider contextDataProvider

    def setupSpec() {
        contextDataProvider = new ContextDataProvider()

    }


    @groovy.transform.TypeChecked(TypeCheckingMode.SKIP)
    void 'Consultamos al proveedor ContextDataProvider por los contextos extraidos de la configuracion'() {
        given:
        Properties prop = new Properties()
        contextDataProvider.configure(prop)

        when:
        contextDataProvider.getDefaultProperties()
        List<HashMap<String, String>> datas = contextDataProvider.getData()

        then:
        datas.size() == 1
        datas.get(0).get("context_name") == "Core"

    }
}