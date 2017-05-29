package ilargia.entitas.gradle

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

@groovy.transform.TypeChecked
class CodegenPluginSpec extends Specification {

    void 'Consultamos al proveedor ComponentDataProvider por los contextos extraidos de los componentes'() {
        given:
        Project project = ProjectBuilder.builder().build()

        when:
        Task task = project.task('codegen', type: CodeGenerationTask)

        then:
        task instanceof CodeGenerationTask


    }

    void 'Consultamos2 al proveedor ComponentDataProvider por los contextos extraidos de los componentes'() {
        given:
        Project project = ProjectBuilder.builder().build()

        when:
        project.getPlugins().apply 'ilargia.entitas.gradle.codegen.plugin'

        then:
        project.getTasks().getByPath("codegen") instanceof CodeGenerationTask


    }
}