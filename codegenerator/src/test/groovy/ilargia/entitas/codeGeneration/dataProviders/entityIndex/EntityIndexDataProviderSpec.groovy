package ilargia.entitas.codeGeneration.dataProviders.entityIndex

import groovy.transform.TypeCheckingMode
import ilargia.entitas.codeGeneration.data.SourceDataFile
import ilargia.entitas.fixtures.components.FixtureProvider
import spock.lang.Narrative
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title

import static ilargia.entitas.codeGeneration.dataProviders.entityIndex.EntityIndexDataProvider.*

@Narrative("""
Como usuario de la aplicacion
Quiero poder obtener los datos necesarios del proveedor de datos de Contexto
Para que pueda usarse en la generacion del codigo.
""")
@Title(""" """)
@groovy.transform.TypeChecked
class EntityIndexDataProviderSpec extends Specification {


    String CONTEXTS_KEY = "Entitas.CodeGeneration.Plugins.Contexts";

    @Shared
    FixtureProvider fixtures = new FixtureProvider("src/test/groovy/ilargia/entitas/fixtures/components")
    @Shared
    EntityIndexDataProvider entityIndexDataProvider


    def setupSpec() {
        entityIndexDataProvider = new EntityIndexDataProvider(fixtures.getSourceDataFiles())

    }


    void 'Consultamos al proveedor ComponentDataProvider por los contextos por defecto'() {
        given: "que iniciamos la configuracion en ComponentDataProvider"
        Properties prop =  new Properties()
        entityIndexDataProvider.configure(prop)

        when: 'recogemos las propiedades por defecto'
        Properties prop2 = entityIndexDataProvider.getDefaultProperties()

        then: 'el resultado de los nombres de contextos debe ser `Core`'
        entityIndexDataProvider.gePriority() == 0
        entityIndexDataProvider.getName() == "Entity Index"
        entityIndexDataProvider.isEnableByDefault() == true
        entityIndexDataProvider.runInDryMode() == true
        prop == prop2
        prop.getProperty(CONTEXTS_KEY) == "Core"
    }

    @groovy.transform.TypeChecked(TypeCheckingMode.SKIP)
    void 'Consultamos al proveedor ComponentDataProvider por los contextos extraidos de los componentes'() {
        given:
        Properties prop =  new Properties()
        entityIndexDataProvider.configure(prop)

        when:
        entityIndexDataProvider.getDefaultProperties()
        List<SourceDataFile> datas = entityIndexDataProvider.getData()

        then:
        datas.size() == 1
        getEntityIndexType(datas.get(id)).equals(result)
        isCustom(datas.get(id)).equals(result2)
        getEntityIndexName(datas.get(id)).contains(result3)
        getKeyType(datas.get(id)).contains(result4)
        getComponentType(datas.get(id)).contains(result5)
        getMemberName(datas.get(id)).contains(result6)
        getContextNames(datas.get(id)).size() == result7


        where: 'la Propiedad: #ContextName para el id: #id  result: #result'
        id  ||  result        | result2   |  result3    |   result4  |   result5   |   result6   |   result7
        0   || "EntityIndex"  | false     |   "Player"  |    "ID"    |   "Player"  |     "id"    |    3
       // 1   ||  "Game"  | "Bounds"  |   false   |    false   |     2       |     2       |    0        |     true   |   ["ilargia.entitas.fixtures.components.dir.Bounds.Tag"]

    }
}