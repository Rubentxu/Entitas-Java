package ilargia.entitas.codeGeneration.plugins.generators

import groovy.transform.TypeCheckingMode
import ilargia.entitas.codeGeneration.data.CodeGenFile
import ilargia.entitas.codeGeneration.data.CodeGeneratorData
import ilargia.entitas.codeGeneration.interfaces.IAppDomain
import ilargia.entitas.codeGeneration.plugins.config.TargetPackageConfig
import ilargia.entitas.codeGeneration.plugins.dataProviders.ProviderUtils
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentDataProvider
import ilargia.entitas.codeGeneration.plugins.postProcessors.WriteToDiskPostProcessor
import ilargia.entitas.codeGeneration.utils.CodeGeneratorUtil
import ilargia.entitas.fixtures.FixtureProvider
import ilargia.entitas.fixtures.TestProject
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.jboss.forge.roaster.model.source.JavaClassSource
import spock.lang.Narrative
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title

import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ComponentTypeDataProvider.getFullTypeName
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ConstructorDataProvider.getConstructorData
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ContextsDataProvider.getContextNames
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.EnumsDataProvider.getEnumData
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.GenericsDataProvider.getGenericsData
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.IsUniqueDataProvider.isUnique
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.MemberDataProvider.getMemberData
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ShouldGenerateComponentDataProvider.shouldGenerateComponent
import static ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ShouldGenerateMethodsDataProvider.shouldGenerateMethods

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
        prop.setProperty("CodeGeneration.CodeGenerator.SearchPkg","ilargia.entitas.fixtures.components")

        when:
        componentDataProvider.configure(prop)
        entityGenerator.configure(prop)

        then:
        entityGenerator.gePriority() == 0
        entityGenerator.getName() == "Component (Entity API)"
        entityGenerator.isEnableByDefault() == true
        entityGenerator.runInDryMode() == true

    }


    void 'Consultamos al generador ComponentEntityGenerator por las propiedades que se extraen'() {
        given:
        Properties prop = new Properties()
        prop.setProperty("CodeGeneration.CodeGenerator.SearchPkg","ilargia.entitas.fixtures.components")
        componentDataProvider.configure(prop)
        entityGenerator.configure(prop)
        entityGenerator.getDefaultProperties()

        when:
        List<CodeGenFile<JavaClassSource>> genFiles = entityGenerator.generate(componentDataProvider.getData())

        then:
        genFiles.size() == 3


    }

}