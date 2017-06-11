package ilargia.entitas.codeGeneration.plugins.generators

import ilargia.entitas.codeGeneration.data.CodeGenFile
import ilargia.entitas.codeGeneration.interfaces.IAppDomain
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentDataProvider

import ilargia.entitas.fixtures.TestProject
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.jboss.forge.roaster.model.source.JavaClassSource
import spock.lang.Narrative
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title

@Narrative("""
Como usuario de la aplicacion
Quiero generar una clase con los indices de los compoenentes
Para poder referenciarlos en la  el desarrollo en la aplicacion.
""")
@Title(""" """)
//@groovy.transform.TypeChecked
class ComponentContextGeneratorSpec extends Specification {

    @Shared
    ComponentContextGenerator contextGenerator
    @Shared
    Project project
    @Shared
    ComponentDataProvider componentDataProvider

    def setupSpec() {
        contextGenerator =  new ComponentContextGenerator()
        componentDataProvider = new ComponentDataProvider()
        project = ProjectBuilder.builder().withProjectDir(new File("./")).withGradleUserHomeDir(new File("./build")).build()
        JavaPlugin plugin = project.getPlugins().apply(JavaPlugin.class)
        IAppDomain appProject = new TestProject(project)
        componentDataProvider.setAppDomain(new TestProject(project))

    }


    void 'Consultamos al generador ComponentContextGenerator por la configuracion por defecto'() {
        given:
        Properties prop = new Properties()
        prop.setProperty("CodeGeneration.CodeGenerator.SearchPkg","ilargia.entitas.fixtures.components")

        when:
        componentDataProvider.configure(prop)
        contextGenerator.configure(prop)

        then:
        contextGenerator.gePriority() == 0
        contextGenerator.getName() == "Component (Context API)"
        contextGenerator.isEnableByDefault() == true
        contextGenerator.runInDryMode() == true

    }


    void 'Generamos las fuentes con los datos del proveedor'() {
        given:
        Properties prop = new Properties()
        prop.setProperty("CodeGeneration.CodeGenerator.SearchPkg","ilargia.entitas.fixtures.components")
        componentDataProvider.configure(prop)
        componentDataProvider.getDefaultProperties()
        contextGenerator.configure(prop)
        contextGenerator.getDefaultProperties()

        when:
        List<CodeGenFile<JavaClassSource>> genFiles = contextGenerator.generate(componentDataProvider.getData())

        then:
        genFiles.size() == 3
        genFiles.get(0).fileName == "GameContext"
        genFiles.get(0).subDir == "game"
        genFiles.get(0).fileContent.getPackage() == "entitas.generated.game"
        genFiles.get(1).fileName == "TestContext"
        genFiles.get(1).subDir == "test"
        genFiles.get(1).fileContent.getPackage() == "entitas.generated.test"
        genFiles.get(2).fileName == "CoreContext"
        genFiles.get(2).subDir == "test"
        genFiles.get(2).fileContent.getPackage() == "entitas.generated.test"


    }

}