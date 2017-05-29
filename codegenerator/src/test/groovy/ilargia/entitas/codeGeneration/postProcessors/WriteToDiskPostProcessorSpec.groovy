package ilargia.entitas.codeGeneration.postProcessors

import groovy.transform.TypeCheckingMode
import ilargia.entitas.codeGeneration.config.TargetDirectoryConfig
import ilargia.entitas.codeGeneration.data.CodeGenFile
import ilargia.entitas.codeGeneration.data.SourceDataFile
import ilargia.entitas.fixtures.components.FixtureProvider
import org.jboss.forge.roaster.model.source.JavaClassSource
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
@groovy.transform.TypeChecked
class WriteToDiskPostProcessorSpec extends Specification {

    @Shared
    FixtureProvider fixtures = new FixtureProvider("src/test/groovy/ilargia/entitas/fixtures/components")
    @Shared
    WriteToDiskPostProcessor postProcessor
    @Shared
    List<CodeGenFile> genFiles


    def setupSpec() {
        postProcessor = new WriteToDiskPostProcessor()
        List<SourceDataFile> dataFiles = fixtures.getSourceDataFiles()
        genFiles = new ArrayList<>()
        for(SourceDataFile df : dataFiles) {
            genFiles.add(new CodeGenFile((String) df.fileName, "", (String)df.subDir,(JavaClassSource) df.source))
        }


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
        prop.getProperty(TargetDirectoryConfig.TARGET_DIRECTORY_KEY) == "./generated"
        postProcessor.getName() == "Write to disk"
        postProcessor.gePriority() == 100
        postProcessor.isEnableByDefault() == true
        postProcessor.runInDryMode() == false


    }
}