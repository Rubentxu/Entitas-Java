package ilargia.entitas.codeGeneration.plugins.dataProviders.components

import groovy.transform.TypeCheckingMode
import ilargia.entitas.codeGeneration.data.CodeGeneratorData
import ilargia.entitas.fixtures.TestProject
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Narrative
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title

import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ComponentTypeDataProvider.getFullTypeName
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ConstructorDataProvider.getConstructorData
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ContextsComponentDataProvider.getContextNames
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.EnumsDataProvider.getEnumData
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.GenericsDataProvider.getGenericsData
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.IsUniqueDataProvider.isUnique
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.MemberDataProvider.getMemberData
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ShouldGenerateComponentDataProvider.shouldGenerateComponent
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ShouldGenerateMethodsDataProvider.shouldGenerateMethods

@Narrative("""
Como usuario de la aplicacion
Quiero poder obtener los datos necesarios del proveedor de datos de Componentes
Para que pueda usarse en la generacion del codigo.
""")
@Title(""" """)
@groovy.transform.TypeChecked
class ComponentDataProviderSpec extends Specification {

    String CONTEXTS_KEY = "CodeGeneration.Contexts";

    @Shared
    ComponentDataProvider componentDataProvider


    def setupSpec() {
        componentDataProvider = new ComponentDataProvider()
        Project project = ProjectBuilder.builder().withProjectDir(new File("./")).withGradleUserHomeDir(new File("./build")).build()
        JavaPlugin plugin = project.getPlugins().apply(JavaPlugin.class)
        componentDataProvider.setAppDomain(new TestProject(project))

    }


    void 'Consultamos al proveedor ComponentDataProvider por los contextos por defecto'() {
        given: "que iniciamos la configuracion en ComponentDataProvider"
        Properties prop = new Properties()
        prop.setProperty("CodeGeneration.CodeGenerator.SearchPkg","ilargia.entitas.fixtures.src.main.java.ilargia.components")
        componentDataProvider.setProperties(prop)

        when: 'recogemos las propiedades por defecto'
        Properties prop2 = componentDataProvider.defaultProperties()

        then: 'el resultado de los nombres de contextos debe ser `Core`'
        componentDataProvider.gePriority() == 0
        componentDataProvider.getName() == "Component"
        componentDataProvider.isEnableByDefault() == true
        componentDataProvider.runInDryMode() == true
        prop == prop2
        prop.getProperty(CONTEXTS_KEY) == "Core"
    }

    @groovy.transform.TypeChecked(TypeCheckingMode.SKIP)
    void 'Consultamos al proveedor ComponentDataProvider por los contextos extraidos de los componentes'() {
        given:
        Properties prop = new Properties()
        prop.setProperty("CodeGeneration.SearchPkg","ilargia.entitas.fixtures.src.main.java.ilargia.components")
        componentDataProvider.setProperties(prop)

        when:
        componentDataProvider.defaultProperties()
        List<CodeGeneratorData> datas = componentDataProvider.getData()

        then:
        datas.size() == 8
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
        id | id2 || result  | result2       | result3 | result4 | result5 | result6 | result7 | result8 | result9
        0  | 0   || "Game"  | "Ball"        | true    | false   | 2       | 1       | 0       | true    | []
        1  | 0   || "Game"  | "Bounds"      | false   | false   | 2       | 5       | 0       | true    | ["ilargia.entitas.fixtures.src.main.java.ilargia.components.game.Bounds.Tag"]
        2  | 0   || "Test"  | "Interactive" | false   | false   | 0       | 0       | 0       | true    | []
        3  | 0   || "Test"  | "Motion"      | false   | false   | 1       | 2       | 0       | true    | []
        4  | 0   || "Core"  | "Player"      | false   | false   | 1       | 1       | 0       | true    | ["ilargia.entitas.fixtures.src.main.java.ilargia.components.test.Player.ID"]
        5  | 0   || "Test"  | "Position"    | false   | false   | 2       | 2       | 0       | true    | []
        6  | 0   || "Test"  | "Size"        | false   | false   | 1       | 2       | 0       | true    | []
        7  | 0   || "Input" | "View"        | false   | false   | 1       | 1       | 1       | false    | []


    }
}