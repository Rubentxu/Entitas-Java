package ilargia.entitas.codeGeneration.plugins.dataProviders.entityIndex

import groovy.transform.TypeCheckingMode
import ilargia.entitas.codeGeneration.data.SourceDataFile
import ilargia.entitas.fixtures.FixtureProvider
import spock.lang.Narrative
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title

import static ilargia.entitas.codeGeneration.plugins.dataProviders.entityIndex.EntityIndexDataProvider.getComponentType
import static ilargia.entitas.codeGeneration.plugins.dataProviders.entityIndex.EntityIndexDataProvider.getContextNames
import static ilargia.entitas.codeGeneration.plugins.dataProviders.entityIndex.EntityIndexDataProvider.getCustomMethods
import static ilargia.entitas.codeGeneration.plugins.dataProviders.entityIndex.EntityIndexDataProvider.getEntityIndexName
import static ilargia.entitas.codeGeneration.plugins.dataProviders.entityIndex.EntityIndexDataProvider.getEntityIndexType
import static ilargia.entitas.codeGeneration.plugins.dataProviders.entityIndex.EntityIndexDataProvider.getKeyType
import static ilargia.entitas.codeGeneration.plugins.dataProviders.entityIndex.EntityIndexDataProvider.getMemberName
import static ilargia.entitas.codeGeneration.plugins.dataProviders.entityIndex.EntityIndexDataProvider.isCustom


@Narrative("""
Como usuario de la aplicacion
Quiero poder obtener la informacion necesaria del proveedor de datos de Entidades Indexadas
Para que pueda usarse en la generacion del codigo.
""")
@Title(""" """)
@groovy.transform.TypeChecked
class EntityIndexDataProviderSpec extends Specification {

    @Shared
    FixtureProvider fixtures = new FixtureProvider("src/test/java/ilargia/entitas/fixtures/components")
    @Shared
    EntityIndexDataProvider entityIndexDataProvider


    def setupSpec() {
        entityIndexDataProvider = new EntityIndexDataProvider(fixtures.getSourceDataFiles())

    }


    @groovy.transform.TypeChecked(TypeCheckingMode.SKIP)
    void 'Consultamos al proveedor ComponentDataProvider por los contextos extraidos de los componentes'() {
        given:
        Properties prop = new Properties()
        entityIndexDataProvider.configure(prop)

        when:
        entityIndexDataProvider.getDefaultProperties()
        List<SourceDataFile> datas = entityIndexDataProvider.getData()

        then:
        datas.size() == 2
        getEntityIndexType(datas.get(id)).equals(result)
        isCustom(datas.get(id)) == result2
        getEntityIndexName(datas.get(id)).contains(result3)
        getKeyType(datas.get(id)).contains(result4)
        getComponentType(datas.get(id)).contains(result5)
        getMemberName(datas.get(id)).contains(result6)
        getContextNames(datas.get(id)).size() == result7
        getCustomMethods(datas.get(id)).size() == result8

        where: 'la Propiedad: #ContextName para el id: #id  result: #result'
        id || result        | result2 | result3       | result4 | result5  | result6 | result7 | result8
        0  || "EntityIndex" | false   | "Player"      | "ID"    | "Player" | "id"    | 3       | 0
        1  || "CustomIndex" | true    | "CustomIndex" | ""      | ""       | ""      | 1       | 2


    }
}