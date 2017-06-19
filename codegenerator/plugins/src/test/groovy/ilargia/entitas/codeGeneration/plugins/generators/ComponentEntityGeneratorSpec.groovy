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
Quiero generar la subclase de Entity con funciones de utilidad
Para falicitarme el desarrollo en la aplicacion.
""")
@Title(""" """)
@groovy.transform.TypeChecked
class ComponentEntityGeneratorSpec extends Specification {

    @Shared
    ComponentEntityGenerator entityGenerator
    @Shared
    Project project
    @Shared
    ComponentDataProvider componentDataProvider

    def setupSpec() {
        entityGenerator =  new ComponentEntityGenerator()
        componentDataProvider = new ComponentDataProvider()
        project = ProjectBuilder.builder().withProjectDir(new File("./")).withGradleUserHomeDir(new File("./build")).build()
        JavaPlugin plugin = project.getPlugins().apply(JavaPlugin.class)
        IAppDomain appProject = new TestProject(project)
        componentDataProvider.setAppDomain(new TestProject(project))

    }


    void 'Consultamos al generador ComponentEntityGenerator por la configuracion por defecto'() {
        given:
        Properties prop = new Properties()
        prop.setProperty("CodeGeneration.CodeGenerator.SearchPkg","ilargia.entitas.fixtures.src.main.java.ilargia.components")

        when:
        componentDataProvider.setProperties(prop)
        entityGenerator.setProperties(prop)

        then:
        entityGenerator.gePriority() == 0
        entityGenerator.getName() == "Component (Entity API)"
        entityGenerator.isEnableByDefault() == true
        entityGenerator.runInDryMode() == true

    }


    void 'Generamos las fuentes con los datos del proveedor'() {
        given:
        Properties prop = new Properties()
        prop.setProperty("CodeGeneration.SearchPkg","ilargia.entitas.fixtures.src.main.java.ilargia.components")
        componentDataProvider.setProperties(prop)
        componentDataProvider.defaultProperties()
        entityGenerator.setProperties(prop)
        entityGenerator.defaultProperties()

        when:
        List<CodeGenFile<JavaClassSource>> genFiles = entityGenerator.generate(componentDataProvider.getData())

        then:
        genFiles.size() == 3


    }

}