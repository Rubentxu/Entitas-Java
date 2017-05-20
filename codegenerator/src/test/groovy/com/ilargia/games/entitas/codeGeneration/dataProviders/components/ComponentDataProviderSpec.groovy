package com.ilargia.games.entitas.codeGeneration.dataProviders.components

import com.ilargia.games.entitas.codeGeneration.data.SourceDataFile
import com.ilargia.games.entitas.fixtures.components.FixtureProvider
import groovy.transform.TypeCheckingMode
import spock.lang.Narrative
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title
import static com.ilargia.games.entitas.codeGeneration.dataProviders.components.providers.ContextsDataProvider.getContextNames

@Narrative("""
Como usuario de la aplicacion
Quiero poder tener obtener del fichero de configuración, los datos referentes para configurar los proveedores de datos
Para que pueda usarse en la generacion del codigo.
""")
@Title(""" ComponentDataProvider se encargara de aprovisionar estos datos de configuarcion""")
@groovy.transform.TypeChecked
class ComponentDataProviderSpec extends Specification {

    static String CONTEXTS_KEY = "Entitas.CodeGeneration.Plugins.Contexts";

    @Shared
    FixtureProvider fixtures = new FixtureProvider("src/test/groovy/com/ilargia/games/entitas/fixtures/components")
    @Shared
    ComponentDataProvider componentDataProvider


    def setupSpec() {
       componentDataProvider = new ComponentDataProvider(fixtures.getSourceDataFiles())

    }


    void 'Consultamos al proveedor ComponentDataProvider por los contextos por defecto'() {
        given: "que iniciamos la configuracion en ComponentDataProvider"
        Properties prop =  new Properties()
        componentDataProvider.configure(prop)

        when: 'recogemos las propiedades por defecto'
        Properties prop2 = componentDataProvider.getDefaultProperties()

        then: 'el resultado de los nombres de contextos debe ser `Core`'
        prop == prop2
        prop.getProperty(CONTEXTS_KEY) == "Core"
    }

    @groovy.transform.TypeChecked(TypeCheckingMode.SKIP)
    void 'Consultamos al proveedor ComponentDataProvider por los contextos extraidos de los componentes'() {
        given:
        Properties prop =  new Properties()
        componentDataProvider.configure(prop)

        when:
        componentDataProvider.getDefaultProperties()
        List<SourceDataFile> datas = componentDataProvider.getData()

        then:
        datas.size() == 6
        getContextNames(datas.get(0)).get(0).equals("Core")
        getContextNames(datas.get(1)).get(0).equals("Game")
        getContextNames(datas.get(2)).get(0).equals("Game")
        getContextNames(datas.get(3)).get(0).equals("Input")
        getContextNames(datas.get(4)).get(0).equals("Game")
        getContextNames(datas.get(5)).get(0).equals("Input")

    }
}