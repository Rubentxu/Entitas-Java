package ilargia.entitas.index

import ilargia.entitas.api.ContextInfo
import ilargia.entitas.api.IComponent
import ilargia.entitas.components.Interactive
import ilargia.entitas.factories.CollectionsFactories
import ilargia.entitas.factories.EntitasCollections
import ilargia.entitas.utils.TestComponentIds
import ilargia.entitas.utils.TestContext
import ilargia.entitas.utils.TestMatcher
import ilargia.entitas.utils.TestEntity
import groovy.transform.TypeCheckingMode
import spock.lang.*

@Narrative("""
Como usuario de la libreria
Quiero poder tener una forma de indexar las entidades
Para que pueda realizar busquedas rapidas sobre el contexto de entidades.
""")
@Title(""" Se propone una clase PrimaryEntityIndex para realizar la busqueda""")
@groovy.transform.TypeChecked
class PrimaryEntityIndexSpec extends Specification {
    @Shared
    def entitasCollections = new EntitasCollections(new CollectionsFactories() {})
    @Shared
    TestEntity entity
    @Shared
    Stack<IComponent>[] _componentPools = new Stack[10]

    @Shared
    PrimaryEntityIndex<TestEntity, Integer> index

    @Shared
    TestContext context

    public TestContext createTestContext() {
        return new TestContext(TestComponentIds.totalComponents, 0,
                new ContextInfo("Test", TestComponentIds.componentNames(),
                        TestComponentIds.componentTypes()), ({ -> new TestEntity()}));
    }

    private static Integer   getKey(TestEntity entity, IComponent component) {
        return entity.getCreationIndex()
    }

    @groovy.transform.TypeChecked(TypeCheckingMode.SKIP)
    def setupSpec() {
        context =  createTestContext()
        index = new PrimaryEntityIndex<TestEntity, Integer>(
                "Interactive index", { e,c-> getKey(e, c) }, context.getGroup(TestMatcher.Interactive()))
        entity = context.createEntity()


    }

    @Unroll
    def 'Necesito saber si la entidad tiene un componente #ComponentName'() {
        given: 'una entidad con un componente Interactive y View'
        entity.addComponent(TestComponentIds.Interactive, new Interactive())
       // entity.addComponent(TestComponentIds.View, new View(1))

        expect: 'cuando le preguntamos por #ComponentName y el resultado #result'
        index.getEntity(0) == entity

        cleanup:
        entity.removeComponent(TestComponentIds.Interactive)
       // entity.removeComponent(TestComponentIds.View)

    }


}
