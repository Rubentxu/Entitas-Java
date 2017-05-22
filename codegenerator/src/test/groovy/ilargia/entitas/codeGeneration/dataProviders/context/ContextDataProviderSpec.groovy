package ilargia.entitas.codeGeneration.dataProviders.context

import groovy.transform.TypeCheckingMode
import ilargia.entitas.codeGeneration.data.SourceDataFile
import ilargia.entitas.fixtures.components.FixtureProvider
import spock.lang.Narrative
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title

import static ilargia.entitas.codeGeneration.dataProviders.components.providers.ComponentTypeDataProvider.getFullTypeName
import static ilargia.entitas.codeGeneration.dataProviders.components.providers.ConstructorDataProvider.getConstructorData
import static ilargia.entitas.codeGeneration.dataProviders.components.providers.ContextsDataProvider.getContextNames
import static ilargia.entitas.codeGeneration.dataProviders.components.providers.EnumsDataProvider.getEnumData
import static ilargia.entitas.codeGeneration.dataProviders.components.providers.GenericsDataProvider.getGenericsData
import static ilargia.entitas.codeGeneration.dataProviders.components.providers.IsUniqueDataProvider.isUnique
import static ilargia.entitas.codeGeneration.dataProviders.components.providers.MemberDataProvider.getMemberData
import static ilargia.entitas.codeGeneration.dataProviders.components.providers.ShouldGenerateComponentDataProvider.shouldGenerateComponent
import static ilargia.entitas.codeGeneration.dataProviders.components.providers.ShouldGenerateMethodsDataProvider.shouldGenerateMethods

@Narrative("""
Como usuario de la aplicacion
Quiero poder obtener los datos necesarios del proveedor de datos de Contexto
Para que pueda usarse en la generacion del codigo.
""")
@Title(""" """)
@groovy.transform.TypeChecked
class ContextDataProviderSpec extends Specification {

    String CONTEXTS_KEY = "Entitas.CodeGeneration.Plugins.Contexts";

    @Shared
    FixtureProvider fixtures = new FixtureProvider("src/test/groovy/ilargia/entitas/fixtures/components")
    @Shared
    ContextDataProvider contextDataProvider


    def setupSpec() {
        ContextDataProvider = new ContextDataProvider(fixtures.getSourceDataFiles())

    }


    void 'Consultamos al proveedor ContextDataProvider por los contextos por defecto'() {
        given: "que iniciamos la configuracion en ComponentDataProvider"
        Properties prop =  new Properties()
        contextDataProvider.configure(prop)

        when: 'recogemos las propiedades por defecto'
        Properties prop2 = contextDataProvider.getDefaultProperties()

        then: 'el resultado de los nombres de contextos debe ser `Core`'
        contextDataProvider.gePriority() == 0
        contextDataProvider.getName() == "Context"
        contextDataProvider.isEnableByDefault() == true
        contextDataProvider.runInDryMode() == true
        prop == prop2
        prop.getProperty(CONTEXTS_KEY) == "Core"
    }

    @groovy.transform.TypeChecked(TypeCheckingMode.SKIP)
    void 'Consultamos al proveedor ComponentDataProvider por los contextos extraidos de los componentes'() {
        given:
        Properties prop =  new Properties()
        contextDataProvider.configure(prop)

        when:
        contextDataProvider.getDefaultProperties()
        List<SourceDataFile> datas = contextDataProvider.getData()

        then:
        datas.size() == 6
        getContextNames(datas.get(id)).get(id2).equals(result)
        getFullTypeName(datas.get(id)).contains(result2)
        isUnique(datas.get(id)) == result3
        shouldGenerateComponent(datas.get(id)) == result4
        getConstructorData(datas.get(id)).size() == result5
        getMemberData(datas.get(id)).size() == result6
        getGenericsData(datas.get(id)).size() == result7
        shouldGenerateMethods(datas.get(id)) == result8
        getEnumData(datas.get(id)) == result9

        where: 'la Propiedad: #ContextName para el id: #id  result: #result'
          id  |   id2 ||  result  | result2   |  result3  |   result4  |   result5   |   result6   |   result7   |   result8  | result9
          0   |   0   ||  "Game"  | "Ball"    |   true    |    false   |     2       |     1       |    0        |     true   |   []
          1   |   0   ||  "Game"  | "Bounds"  |   false   |    false   |     2       |     2       |    0        |     true   |   ["ilargia.entitas.fixtures.components.Bounds.Tag"]
          2   |   0   ||  "Core"  | "Motion"  |   false   |    false   |     1       |     1       |    0        |     true   |   []
          3   |   0   ||  "Input" | "Player"  |   false   |    false   |     1       |     1       |    0        |     true   |   ["ilargia.entitas.fixtures.components.Player.ID"]
          4   |   0   ||  "Game"  | "Score"   |   false   |    true    |     1       |     1       |    0        |     true   |   []
          5   |   0   ||  "Input" | "View"    |   false   |    false   |     1       |     1       |    1        |     false  |   []

    }
}