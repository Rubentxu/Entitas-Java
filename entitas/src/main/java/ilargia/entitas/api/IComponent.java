package ilargia.entitas.api;

/**
 * Implement this interface if you want to create a component which you can add to an entity.
 * Optionally, you can add these annotations:
 * '@Unique': the code generator will generate additional methods for the context to ensure that only one entity with this component exists.
 * E.g. context.isAnimating = true or context.setResources();
 * '@Contexts': You can make this component to be available only in the specified contexts.
 * The code generator can generate these attributes for you.
 * More available Annotations can be found in ilargia.entitas.codeGenerator.annotations.
 * @author Rubentxu
 */
public interface IComponent {
}