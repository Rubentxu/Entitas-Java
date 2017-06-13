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
class ComponentMatcherGeneratorSpec extends Specification {

    @Shared
    ComponentMatcherGenerator matcherGenerator
    @Shared
    Project project
    @Shared
    ComponentDataProvider componentDataProvider

    def setupSpec() {
        matcherGenerator =  new ComponentMatcherGenerator()
        componentDataProvider = new ComponentDataProvider()
        project = ProjectBuilder.builder().withProjectDir(new File("./")).withGradleUserHomeDir(new File("./build")).build()
        JavaPlugin plugin = project.getPlugins().apply(JavaPlugin.class)
        IAppDomain appProject = new TestProject(project)
        componentDataProvider.setAppDomain(new TestProject(project))

    }


    void 'Consultamos al generador ComponentMatcherGenerator por la configuracion por defecto'() {
        given:
        Properties prop = new Properties()
        prop.setProperty("CodeGeneration.CodeGenerator.SearchPkg","ilargia.entitas.fixtures.components")

        when:
        componentDataProvider.configure(prop)
        matcherGenerator.configure(prop)

        then:
        matcherGenerator.gePriority() == 0
        matcherGenerator.getName() == "Component (Matcher API)"
        matcherGenerator.isEnableByDefault() == true
        matcherGenerator.runInDryMode() == true

    }


    void 'Generamos las fuentes con los datos del proveedor'() {
        given:
        Properties prop = new Properties()
        prop.setProperty("CodeGeneration.SearchPkg","ilargia.entitas.fixtures.components")
        componentDataProvider.configure(prop)
        componentDataProvider.getDefaultProperties()
        matcherGenerator.configure(prop)
        matcherGenerator.getDefaultProperties()

        when:
        List<CodeGenFile<JavaClassSource>> genFiles = matcherGenerator.generate(componentDataProvider.getData())

        then:
        genFiles.size() == 3
        genFiles.get(0).fileName == "GameMatcher"
        genFiles.get(0).subDir == "game"
        genFiles.get(0).fileContent.getPackage() == "entitas.generated.game"
        genFiles.get(1).fileName == "TestMatcher"
        genFiles.get(1).subDir == "test"
        genFiles.get(1).fileContent.getPackage() == "entitas.generated.test"
        genFiles.get(2).fileName == "CoreMatcher"
        genFiles.get(2).subDir == "test"
        genFiles.get(2).fileContent.getPackage() == "entitas.generated.test"


    }

}