package com.ilargia.games.entitas

import com.ilargia.games.entitas.api.ContextInfo
import com.ilargia.games.entitas.api.IComponent
import com.ilargia.games.entitas.components.Position
import com.ilargia.games.entitas.components.View
import com.ilargia.games.entitas.exceptions.EntityAlreadyHasComponentException
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
Como usuario de la libreria
Quiero poder tener una forma facil, reutilizable y rapida de gestionar los elementos(Entitades) y datos(Componentes) en el escenario de mi juego
Para que pueda usar esta informacion en la logica de mi juego.
""")
@Title(""" Se propone una clase Entity que represente un elemento del Escenario y contenga todos los estados de este
divididos en componentes reutilizables por otras entidades""")
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
    def 'Necesito saber si la entidad tiene un componente #ComponentName'() {            // 2
        given: 'una entidad con un componente Position y View'                    // 3
            entity.addComponent(TestComponentIds.Position, new Position(100,100))
            entity.addComponent(TestComponentIds.View,  new View(1))

        expect: 'le preguntamos si tiene el componente #ComponentName con id: #idComponent'                          // 4
            entity.hasComponent(idComponent) == result

        cleanup:
            entity.removeComponent(TestComponentIds.Position)
            entity.removeComponent(TestComponentIds.View)
        where:
            ComponentName  |    idComponent                   || result
            'Posicion'      |    TestComponentIds.Position      || true
            'View'          |    TestComponentIds.View          || true
            'Player'        |    TestComponentIds.Player        || false
            'Interactive'   |    TestComponentIds.Interactive   || false

    }

    @Unroll
    def 'Necesito saber si la entidad tiene un componente #ComponentName1 y un componente #ComponentName2'() {            // 2
        given: 'una entidad con un componente Position y View'                    // 3
            entity.addComponent(TestComponentIds.Position, new Position(100,100))
            entity.addComponent(TestComponentIds.View,  new View(1))

        expect: 'le preguntamos si tiene el componente #ComponentName1 y el componente #ComponentName2'                          // 4
            entity.hasComponents(idComponent1, idComponent2) == result

        cleanup:
            entity.removeComponent(TestComponentIds.Position)
            entity.removeComponent(TestComponentIds.View)
        where:
            ComponentName1 |    idComponent1                  |   ComponentName2 |    idComponent2                || result
            'Posicion'      |    TestComponentIds.Position      |  'View'           |   TestComponentIds.View         ||  true
            'Posicion'      |    TestComponentIds.Position      |  'Interactive'    |   TestComponentIds.Interactive  ||  false
            'View'          |    TestComponentIds.View          |  'Interactive'    |   TestComponentIds.Interactive  ||  false
            'Player'        |    TestComponentIds.Player        |  'Interactive'    |   TestComponentIds.Interactive  ||  false


    }

    @Unroll
    def 'Necesito saber si la entidad tiene algun componente #ComponentName1 o un componente #ComponentName2'() {            // 2
        given: 'una entidad con un componente Position y View'                    // 3
            entity.addComponent(TestComponentIds.Position, new Position(100,100))
            entity.addComponent(TestComponentIds.View,  new View(1))

        expect: 'le preguntamos si tiene algun componente #ComponentName1 o el componente #ComponentName2'                          // 4
            entity.hasAnyComponent(idComponent1, idComponent2) == result

        cleanup:
            entity.removeComponent(TestComponentIds.Position)
            entity.removeComponent(TestComponentIds.View)
        where:
            ComponentName1 |    idComponent1                  |   ComponentName2 |    idComponent2                || result
            'Posicion'      |    TestComponentIds.Position      |  'View'           |   TestComponentIds.View         ||  true
            'Posicion'      |    TestComponentIds.Position      |  'Interactive'    |   TestComponentIds.Interactive  ||  true
            'View'          |    TestComponentIds.View          |  'Interactive'    |   TestComponentIds.Interactive  ||  true
            'Player'        |    TestComponentIds.Player        |  'Interactive'    |   TestComponentIds.Interactive  ||  false


    }

    def 'Necesito poder desactivar una entidad para poder reusarla en el futuro'() {
        when: 'cuando destruimos la entidad'
            entity.destroy()

        then:
            entity.isEnabled() == false

        when: 'cuando destruimos la entidad'
            entity.reactivate(0)

        then:
            entity.isEnabled() == true

    }

    def 'Necesito agregar un componente'() {
        when: 'cuando agregamos un componente Position'
            entity.addComponent(TestComponentIds.Position, new Position(100,100) )

        then: 'le preguntamos si tiene el componente Position'                          // 4
            entity.hasComponent(TestComponentIds.Position)

        when: 'cuando le volvemos agregar otro componente con el mismo indice'
            entity.addComponent(TestComponentIds.Position, new Position(100,100) )

        then: 'nos devuelve una excepcion'
            thrown EntityAlreadyHasComponentException

    }

}