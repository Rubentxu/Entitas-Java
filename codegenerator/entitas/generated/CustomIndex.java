package entitas.generated;

import ilargia.entitas.codeGenerator.annotations.CustomEntityIndex;
import ilargia.entitas.codeGenerator.annotations.EntityIndexGetMethod;
import ilargia.entitas.fixtures.IntVector2;
import ilargia.entitas.fixtures.TestContext;
import ilargia.entitas.fixtures.TestEntity;
import ilargia.entitas.fixtures.TestMatcher;
import ilargia.entitas.fixtures.components.dir.Size;
import ilargia.entitas.fixtures.components.dir2.Position;
import ilargia.entitas.index.EntityIndex;
import ilargia.entitas.matcher.Matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CustomEntityIndex(contextType = TestContext.class)
public class CustomIndex extends EntityIndex<TestEntity, IntVector2> {
	static List<IntVector2> _cachedList = new ArrayList<>();

	public CustomIndex(TestContext context) {
		super(
				"MyCustomEntityIndex",
				context.getGroup(Matcher.AllOf(TestMatcher.Position(),
						TestMatcher.Size())),
				(e, c) -> {
					Position position = c instanceof Position
							? (Position) c
							: e.getPosition();
					Size size = c instanceof Size ? (Size) c : e.getSize();
					_cachedList.clear();
					for (int x = (int) position.x; x < position.x + size.width; x++) {
						for (int y = (int) position.y; y < position.y
								- size.height; y++) {
							_cachedList.add(new IntVector2(x, y));
						}
					}
					return _cachedList.toArray(new IntVector2[0]);
				});
	}
	@EntityIndexGetMethod
	public Set<TestEntity> getEntitiesWithPosition(IntVector2 position) {
		return getEntities(position);
	}

	@EntityIndexGetMethod
	public Set<TestEntity> getEntitiesWithPosition2(IntVector2 position,
			IntVector2 size) {
		return getEntities(position);
	}
}
