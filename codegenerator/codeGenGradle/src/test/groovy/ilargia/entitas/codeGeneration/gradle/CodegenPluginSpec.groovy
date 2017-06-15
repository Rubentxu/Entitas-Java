package ilargia.entitas.codeGeneration.gradle

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

//@groovy.transform.TypeChecked
class CodegenPluginSpec extends Specification {


    void 'Queremos comprobar que se genero correctamente el Plugin de generacion de codigo'() {
        given:
        Project project = ProjectBuilder.builder().build()

        when:
        project.getPlugins().apply 'entitas.codegen'

        then:
        project.getTasks().getByPath("codegen") instanceof CodeGenerationTask


    }

    void 'Queremos comprobar que se genero correctamente la tarea de generacion de codigo'() {
        given:
        Project project = ProjectBuilder.builder().build()

        when:
        Task task = project.task('codegen', type: CodeGenerationTask)

        then:
        task instanceof CodeGenerationTask


    }


    void 'Queremos comprobar que se obtiene la configuracion por defecto'() {
        given:
        Properties prop = new Properties()
        Project project = ProjectBuilder.builder().build()
        project.getPlugins().apply 'entitas.codegen'

        when:
        CodeGenerationPluginExtension ext = project.getExtensions().getByName("entitas") as CodeGenerationPluginExtension
        ext.configure(prop)
        ext.getDefaultProperties()

        then:
        ext.getProperties().get("targetPackage") == "entitas.generated"
        ext.getTargetPackage() == "entitas.generated"
        ext.getDataProviders().size() == 3
        ext.getCodeGenerators().size() == 1
        ext.getPlugins().size() == 1
        ext.getPackages() == null


    }

    void 'Queremos comprobar que la configuracion en el build se obtiene desde el plugin'() {
        given:
        Properties prop = new Properties()
        Project project = ProjectBuilder.builder().build()
        project.getPlugins().apply 'entitas.codegen'


        when:
        CodeGenerationPluginExtension ext = project.getExtensions().getByName("entitas") as CodeGenerationPluginExtension

        ext.configure(prop)
        ext.targetPackage("test.gen")


        then:
//        hasEntitas == true
         ext.getProperties().get("targetPackage") == "test.gen"
//        ext.getTargetPackage() == "entitas.generated"
//        ext.getDataProviders().size() == 3
//        ext.getCodeGenerators().size() == 1
//        ext.getPlugins().size() == 1
//        ext.getPackages() == null


    }


}


