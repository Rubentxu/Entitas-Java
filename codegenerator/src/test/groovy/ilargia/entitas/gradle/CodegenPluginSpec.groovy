package ilargia.entitas.gradle

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

@groovy.transform.TypeChecked
class CodegenPluginSpec extends Specification {

    void 'Queremos comprobar que se genero correctamente el Plugin de generacion de codigo'() {
        given:
        Project project = ProjectBuilder.builder().build()

        when:
        project.getPlugins().apply 'ilargia.entitas.gradle.codegen.plugin'

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


    void 'Queremos comprobar que se genero correctamente la configuracion de generacion de codigo'() {
        given:
        Project project = ProjectBuilder.builder().build()
        project.getPlugins().apply 'ilargia.entitas.gradle.codegen.plugin'

        when:
        CodeGenerationPluginExtension ext = project.getExtensions().getByName("EntitasSetting") as CodeGenerationPluginExtension

        then:
        ext.getProperties().get("configCodeGen") == "Entitas.properties"

    }

    void 'Queremos comprobar que se obtienen datos del plugin java'() {
        given:
        Project project = ProjectBuilder.builder().build()
        project.getPlugins().apply 'java'

        when:
        JavaPlugin ext = project.getExtensions().getByName("sourcesSets") as JavaPlugin

        then:
        ext.getProperties().get("configCodeGen") == "Entitas.properties"

    }



}