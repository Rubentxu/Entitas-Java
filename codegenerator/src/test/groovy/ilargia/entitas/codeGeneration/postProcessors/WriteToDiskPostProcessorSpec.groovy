package ilargia.entitas.codeGeneration.postProcessors

import groovy.transform.TypeCheckingMode
import ilargia.entitas.codeGeneration.CodeGeneratorUtil
import ilargia.entitas.codeGeneration.config.TargetPackageConfig
import ilargia.entitas.codeGeneration.data.CodeGenFile
import ilargia.entitas.codeGeneration.data.SourceDataFile
import ilargia.entitas.fixtures.FixtureProvider
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Narrative
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title

@Narrative("""
Como usuario de la aplicacion
Quiero poder crear los ficheros con el codigo generado
Para falicitarme el desarrollo en la aplicacion.
""")
@Title(""" """)
//@groovy.transform.TypeChecked
class WriteToDiskPostProcessorSpec extends Specification {

    @Shared
    FixtureProvider fixtures
    @Shared
    WriteToDiskPostProcessor postProcessor
    @Shared
    List<CodeGenFile> genFiles
    @Shared
    CodeGeneratorUtil codeGeneratorUtil
    @Shared
    List<SourceDataFile> dataFiles



    def setupSpec() {
        codeGeneratorUtil = Mock(CodeGeneratorUtil)
        Project project = ProjectBuilder.builder().build()
        JavaPlugin plugin = project.getPlugins().apply(JavaPlugin.class)
        postProcessor = new WriteToDiskPostProcessor(codeGeneratorUtil, project)
        fixtures = new FixtureProvider("src/test/java/ilargia/entitas/fixtures/components")
        dataFiles = fixtures.getSourceDataFiles()
        genFiles = new ArrayList<>()


    }

    @groovy.transform.TypeChecked(TypeCheckingMode.SKIP)
    void 'Consultamos al proveedor ComponentDataProvider por los contextos extraidos de los componentes'() {
        given:
        Properties prop = new Properties()
        postProcessor.configure(prop)

        when:
        Properties prop2 = postProcessor.getDefaultProperties()

        then:
        prop == prop2
        prop.getProperty(TargetPackageConfig.TARGET_PACKAGE_KEY) == "entitas.generated"
        postProcessor.getName() == "Write to disk"
        postProcessor.gePriority() == 100
        postProcessor.isEnableByDefault() == true
        postProcessor.runInDryMode() == false


    }

    void 'Consultamos2 al proveedor ComponentDataProvider por los contextos extraidos de los componentes'() {
        given:
        Properties prop = new Properties()
        postProcessor.configure(prop)
        postProcessor.getDefaultProperties()
        for(SourceDataFile df : dataFiles) {
            CodeGenFile codeGenFile = new CodeGenFile(df.getFileName(), "", df.getSubDir(), df.getFileContent())
            genFiles.add(codeGenFile)
        }

        when:
        File file = postProcessor.createFile( genFiles.get(0).fileName, genFiles.get(0).subDir, genFiles.get(0).fileContent);

        then:
        println file.getCanonicalPath()
        file.getName().contains('CustomIndex.java')
        file.getCanonicalPath().contains('CustomIndex.java')



    }
}