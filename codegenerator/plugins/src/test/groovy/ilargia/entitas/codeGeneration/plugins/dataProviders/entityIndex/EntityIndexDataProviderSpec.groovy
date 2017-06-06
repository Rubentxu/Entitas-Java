package ilargia.entitas.codeGeneration.plugins.dataProviders.entityIndex

import groovy.transform.TypeCheckingMode
import ilargia.entitas.codeGeneration.config.CodeGeneratorConfig
import ilargia.entitas.codeGeneration.data.CodeGeneratorData
import ilargia.entitas.fixtures.TestProject
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Narrative
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title

import static ilargia.entitas.codeGeneration.plugins.dataProviders.entityIndex.EntityIndexDataProvider.*

@Narrative("""
Como usuario de la aplicacion
Quiero poder obtener la informacion necesaria del proveedor de datos de Entidades Indexadas
Para que pueda usarse en la generacion del codigo.
""")
@Title(""" """)
@groovy.transform.TypeChecked
class EntityIndexDataProviderSpec extends Specification {

    @Shared
    EntityIndexDataProvider entityIndexDataProvider


    def setupSpec() {
        entityIndexDataProvider = new EntityIndexDataProvider()
        Project project = ProjectBuilder.builder().withProjectDir(new File("./")).withGradleUserHomeDir(new File("./build")).build()
        JavaPlugin plugin = project.getPlugins().apply(JavaPlugin.class)
        entityIndexDataProvider.setAppDomain(new TestProject(project))
    }


    @groovy.transform.TypeChecked(TypeCheckingMode.SKIP)
    void 'Consultamos al proveedor ComponentDataProvider por los contextos extraidos de los componentes'() {
        given:
        Properties prop = new Properties()
        prop.setProperty(CodeGeneratorConfig.SEARCH_PACKAGES_KEY, "ilargia.entitas.fixtures.components")
        entityIndexDataProvider.configure(prop)

        when:
        entityIndexDataProvider.getDefaultProperties()
        List<CodeGeneratorData> datas = entityIndexDataProvider.getData()

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
        0  || "EntityIndex" | false   | "Player"      | "ID"    | "Player" | "id"    | 2       | 0
        1  || "CustomIndex" | true    | "CustomIndex" | ""      | ""       | ""      | 1       | 2


    }
}