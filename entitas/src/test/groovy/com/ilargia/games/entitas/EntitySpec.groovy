package com.ilargia.games.entitas

import com.ilargia.games.entitas.api.ContextInfo
import com.ilargia.games.entitas.api.IComponent
import com.ilargia.games.entitas.components.Position
import com.ilargia.games.entitas.components.View
import com.ilargia.games.entitas.factories.CollectionsFactories
import com.ilargia.games.entitas.factories.EntitasCollections
import com.ilargia.games.entitas.utils.TestComponentIds
import com.ilargia.games.entitas.utils.TestEntity
import spock.lang.Narrative
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title
import spock.lang.Unroll

@Narrative("""
Como usuario
Quiero poder tener una forma facil, reutilizable y rapida de gestionar los elementos(Entitades) y datos(Componentes) en el escenario de mi juego
Para que pueda usar esta informacion de manera facil en la logica de mi juego.
""")
@Title("""Entity es la pieza que va contener los componentes asociados a un elemento del escenario.""")

class EntitySpec extends Specification {
    @Shared def entitasCollections =  new EntitasCollections(new CollectionsFactories() {})
    @Shared TestEntity entity = new TestEntity();;
    @Shared Stack<IComponent>[] _componentPools = new Stack[10];

    def setupSpec() {
        entity.initialize(0, 10, _componentPools, new ContextInfo("Test", TestComponentIds.componentNames(),
                TestComponentIds.componentTypes()))
        entity.clearEventsListener()
        entity.reactivate(0)

    }

    @Unroll
    def 'Queremos saber si la entidad tiene un componente #ComponenteName'() {            // 2
        given: 'una entidad con un componente Position y View'                    // 3
            entity.addComponent(TestComponentIds.Position, new Position(100,100))
            entity.addComponent(TestComponentIds.View,  new View(1))

        expect: 'cuando le preguntamos si tiene el componente #ComponenteName con id: #idComponente'                          // 4
            entity.hasComponent(idComponente) == result

        cleanup:
            entity.removeComponent(TestComponentIds.Position)
            entity.removeComponent(TestComponentIds.View)
        where:
            ComponenteName  |    idComponente                || result
            'Posicion'      |    TestComponentIds.Position   || true
            'View'          |    TestComponentIds.View       || true
            'Player'        |    TestComponentIds.Player     || false

    }
}