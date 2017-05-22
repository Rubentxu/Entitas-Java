package ilargia.entitas

import ilargia.entitas.api.ContextInfo
import ilargia.entitas.api.IComponent
import ilargia.entitas.components.Position
import ilargia.entitas.components.View
import ilargia.entitas.exceptions.EntityAlreadyHasComponentException
import ilargia.entitas.exceptions.EntityDoesNotHaveComponentException
import ilargia.entitas.factories.CollectionsFactories
import ilargia.entitas.factories.EntitasCollections
import ilargia.entitas.utils.TestComponentIds
import ilargia.entitas.utils.TestEntity
import ilargia.entitas.exceptions.EntityIsNotEnabledException
import spock.lang.*

@Narrative("""
Como usuario de la libreria
Quiero poder tener una forma facil, reutilizable y rapida de gestionar los elementos(Entitades) y datos(Componentes) en el escenario de mi juego
Para que pueda usar esta informacion en la logica de mi juego.
""")
@Title(""" Se propone una clase Entity que represente un elemento del Escenario y contenga todos los estados de este
divididos en componentes reutilizables por otras entidades""")
class EntitySpec extends Specification {
    @Shared
    def entitasCollections = new EntitasCollections(new CollectionsFactories() {})
    @Shared
    TestEntity entity = new TestEntity(); ;
    @Shared
    Stack<IComponent>[] _componentPools = new Stack[10];

    def setupSpec() {
        entity.initialize(0, 10, _componentPools, new ContextInfo("Test", TestComponentIds.componentNames(),
                TestComponentIds.componentTypes()), null)
        entity.clearEventsListener()
        entity.reactivate(0)

    }

    @Unroll
    def 'Necesito saber si la entidad tiene un componente #ComponentName'() {
        given: 'una entidad con un componente Position y View'
        entity.addComponent(TestComponentIds.Position, new Position(100, 100))
        entity.addComponent(TestComponentIds.View, new View(1))

        expect: 'le preguntamos por #ComponentName y el resultado #result'
        entity.hasComponent(idComponent) == result

        cleanup:
        entity.removeComponent(TestComponentIds.Position)
        entity.removeComponent(TestComponentIds.View)

        where: 'ComponentName: #ComponentName  id: #idComponente result: #result'
        ComponentName | idComponent                  || result
        'Posicion'    | TestComponentIds.Position    || true
        'View'        | TestComponentIds.View        || true
        'Player'      | TestComponentIds.Player      || false
        'Interactive' | TestComponentIds.Interactive || false

    }

    @Unroll
    def 'Necesito saber si la entidad tiene un componente #ComponentName1 y un componente #ComponentName2'() {
        given: 'una entidad con un componente Position y View'
        entity.addComponent(TestComponentIds.Position, new Position(100, 100))
        entity.addComponent(TestComponentIds.View, new View(1))

        expect: 'le preguntamos si tiene el componente #ComponentName1, el componente #ComponentName2 y el resultado #result'
        entity.hasComponents(idComponent1, idComponent2) == result

        cleanup:
        entity.removeComponent(TestComponentIds.Position)
        entity.removeComponent(TestComponentIds.View)

        where: 'ComponentName: #ComponentName1  id: #idComponente1 ComponentName2: #ComponentName2  id2: #idComponente2 result: #result'
        ComponentName1 | idComponent1              | ComponentName2 | idComponent2                 || result
        'Posicion'     | TestComponentIds.Position | 'View'         | TestComponentIds.View        || true
        'Posicion'     | TestComponentIds.Position | 'Interactive'  | TestComponentIds.Interactive || false
        'View'         | TestComponentIds.View     | 'Interactive'  | TestComponentIds.Interactive || false
        'Player'       | TestComponentIds.Player   | 'Interactive'  | TestComponentIds.Interactive || false


    }

    @Unroll
    def 'Necesito saber si la entidad tiene algun componente #ComponentName1 o un componente #ComponentName2'() {
        given: 'una entidad con un componente Position y View'
        entity.addComponent(TestComponentIds.Position, new Position(100, 100))
        entity.addComponent(TestComponentIds.View, new View(1))

        expect: 'le preguntamos si tiene algun componente #ComponentName1 o el componente #ComponentName2 con resultado #result'
        entity.hasAnyComponent(idComponent1, idComponent2) == result

        cleanup:
        entity.removeComponent(TestComponentIds.Position)
        entity.removeComponent(TestComponentIds.View)

        where: 'ComponentName: #ComponentName1  id: #idComponente1 ComponentName2: #ComponentName2  id2: #idComponente2 result: #result'
        ComponentName1 | idComponent1              | ComponentName2 | idComponent2                 || result
        'Posicion'     | TestComponentIds.Position | 'View'         | TestComponentIds.View        || true
        'Posicion'     | TestComponentIds.Position | 'Interactive'  | TestComponentIds.Interactive || true
        'View'         | TestComponentIds.View     | 'Interactive'  | TestComponentIds.Interactive || true
        'Player'       | TestComponentIds.Player   | 'Interactive'  | TestComponentIds.Interactive || false


    }

    def 'Necesito poder desactivar una entidad para poder reusarla en el futuro'() {
        when: 'cuando destruimos la entidad'
        entity.internalDestroy();

        then: 'isEnabled = false'
        entity.isEnabled() == false

        when: 'cuando reactivamos la entidad'
        entity.reactivate(0)

        then: 'isEnabled = true'
        entity.isEnabled() == true

    }

    def 'Necesito agregar un componente'() {
        when: 'agregamos un componente Position'
        entity.addComponent(TestComponentIds.Position, new Position(100, 100))

        then: 'le preguntamos si tiene el componente Position'
        entity.hasComponent(TestComponentIds.Position)

        when: 'le volvemos agregar otro componente con el mismo indice'
        entity.addComponent(TestComponentIds.Position, new Position(100, 100))

        then: 'nos devuelve una excepcion'
        thrown EntityAlreadyHasComponentException

        when: 'destruimos la entidad y le agregamos otro componente'
        entity.internalDestroy()
        entity.addComponent(TestComponentIds.Position, new Position(100, 100))

        then: 'nos devuelve una excepcion EntityIsNotEnabledException'
        thrown EntityIsNotEnabledException

        cleanup:
        entity.reactivate(0)

    }

    def 'Necesito quitar un componente'() {
        given: 'un componente Position agregado a la entidad'
        entity.addComponent(TestComponentIds.Position, new Position(100, 100))

        when: 'quitamos el componente Position de la entidad'
        entity.removeComponent(TestComponentIds.Position)

        then: 'le preguntamos si tiene el componente Position y el resultado es false'
        entity.hasComponent(TestComponentIds.Position) == false

        when: 'volvemos a quitar el componente Position'
        entity.removeComponent(TestComponentIds.Position)

        then: 'le preguntamos si tiene el componente Position y provoca una excepcion EntityDoesNotHaveComponentException'
        thrown EntityDoesNotHaveComponentException

        when: 'cuando destruimos la entidad y le eliminamos otro componente'
        entity.internalDestroy()
        entity.removeComponent(TestComponentIds.Position)

        then: 'nos devuelve una excepcion EntityIsNotEnabledException'
        thrown EntityIsNotEnabledException

        cleanup:
        entity.reactivate(0)

    }

    def 'Necesito reemplazar un componente'() {

        when: 'reemplazamos un componente Position cuando no existe ninguno'
        entity.replaceComponent(TestComponentIds.Position, new Position(50, 50))

        then: 'le preguntamos si existe y el resultado es true'
        entity.hasComponent(TestComponentIds.Position) == true

        when: 'reemplazamos el componente Position con valores diferentes'
        entity.replaceComponent(TestComponentIds.Position, new Position(150, 150))

        then: 'le preguntamos si la posicion de y es 150,150 y el resultado es true'
        entity.hasComponent(TestComponentIds.Position) == true
        ((Position) entity.getComponent(TestComponentIds.Position)).x == 150
        ((Position) entity.getComponent(TestComponentIds.Position)).y == 150

        when: 'destruimos la entidad y reemplazamos el componente Position'
        entity.internalDestroy()
        entity.replaceComponent(TestComponentIds.Position, new Position(150, 150))

        then: 'nos devuelve una excepcion EntityIsNotEnabledException'
        thrown EntityIsNotEnabledException

        cleanup:
        entity.reactivate(0)

    }

    def 'Necesito obtener un componente'() {
        given: 'un componente Position agregado a la entidad'
        def position = new Position(100, 100)
        entity.addComponent(TestComponentIds.Position, position)

        when: 'obtenemos el componente Position'
        def result = entity.getComponent(TestComponentIds.Position)

        then: 'nos devuelve lo mismos datos agregados al inicio'
        result == position

        when: 'intentamos obtener otro componente diferente'
        entity.getComponent(TestComponentIds.View)

        then: 'nos devuelve una excepcion EntityDoesNotHaveComponentException'
        thrown EntityDoesNotHaveComponentException

    }

}