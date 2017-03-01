/**
 * Copyright 2013 Robin Stumm (serverkorken@googlemail.com, http://dermetfan.bplaced.net)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ilargia.games.core.utils.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.utils.ObjectMap;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;
import static com.indignado.games.smariano.utils.dermetfan.math.GeometryUtils.*;

/**
 * provides methods for geometric operations with Box2D bodies, fixtures and shapes
 *
 * @author dermetfan
 */
public abstract class Box2DUtils {

    /**
     * cached {@link Shape Shapes} and their vertices
     */
    public static final ObjectMap<Shape, ShapeCache> shapeCache = new ObjectMap<Shape, ShapeCache>(1);
    /**
     * temporary {@link Vector2} used by some methods
     * warning: not safe to use as it may change unexpectedly
     */
    public static Vector2 tmpVec = new Vector2();
    /**
     * @see #tmpVec
     */
    public static Vector2 tmpVec2 = new Vector2();
    /**
     * @see #tmpVec
     */
    public static Vector2[] tmpVecArr;
    /**
     * if new {@link Shape} passed in {@link #vertices(Shape)} should be automatically added to the {@link #shapeCache}
     */
    private static boolean autoShapeCache = true;
    /**
     * the maximum of {@link Shape Shapes} and their vertices to automatically cache if {@link #autoShapeCache} is true
     */
    private static int autoShapeCacheMaxSize = Integer.MAX_VALUE;

    /**
     * @return the vertices of all fixtures of the given bodyA
     * @see #vertices(Shape)
     */
    public static Vector2[] vertices(Body body, Vector2[] output) {
        Vector2[][] fixtureVertices = new Vector2[body.getFixtureList().size][]; // caching fixture vertices for performance
        for (int i = 0; i < fixtureVertices.length; i++)
            fixtureVertices[i] = vertices(body.getFixtureList().get(i), tmpVecArr);

        int vertexCount = 0;
        int fvi = -1;
        for (Fixture fixture : body.getFixtureList())
            if (fixture.getShape().getType() == Type.Circle) // for performance (doesn't call #vertices(Shape))
                vertexCount += 4;
            else
                vertexCount += fixtureVertices[++fvi].length;

        output = new Vector2[vertexCount];
        int vi = -1;
        for (Vector2[] verts : fixtureVertices)
            for (Vector2 vertice : verts)
                output[++vi] = vertice;

        return output;
    }

    /**
     * @see #vertices(Body, Vector2[])
     */
    public static Vector2[] vertices(Body body) {
        return vertices(body, tmpVecArr);
    }

    /**
     * @see #vertices(Shape)
     */
    public static Vector2[] vertices(Fixture fixture, Vector2[] output) {
        return vertices(fixture.getShape(), output);
    }

    /**
     * @see #vertices(Fixture, Vector2[])
     */
    public static Vector2[] vertices(Fixture fixture) {
        return vertices(fixture, tmpVecArr);
    }

    /**
     * @return the vertices of the given Shape
     */
    public static Vector2[] vertices(Shape shape, Vector2[] output) {
        if (shapeCache.containsKey(shape))
            return output = shapeCache.get(shape).vertices;
        else {
            switch (shape.getType()) {
                case Polygon:
                    PolygonShape polygonShape = (PolygonShape) shape;

                    output = new Vector2[polygonShape.getVertexCount()];

                    for (int i = 0; i < output.length; i++) {
                        output[i] = new Vector2();
                        polygonShape.getVertex(i, output[i]);
                    }
                    break;
                case Edge:
                    EdgeShape edgeShape = (EdgeShape) shape;

                    edgeShape.getVertex1(tmpVec);
                    edgeShape.getVertex2(tmpVec2);

                    output = new Vector2[]{tmpVec, tmpVec2};
                    break;
                case Chain:
                    ChainShape chainShape = (ChainShape) shape;

                    output = new Vector2[chainShape.getVertexCount()];

                    for (int i = 0; i < output.length; i++) {
                        output[i] = new Vector2();
                        chainShape.getVertex(i, output[i]);
                    }
                    break;
                case Circle:
                    CircleShape circleShape = (CircleShape) shape;

                    output = new Vector2[]{
                            new Vector2(circleShape.getPosition().x - circleShape.getRadius(), circleShape.getPosition().y + circleShape.getRadius()), // top left
                            new Vector2(circleShape.getPosition().x - circleShape.getRadius(), circleShape.getPosition().y - circleShape.getRadius()), // bottom left
                            new Vector2(circleShape.getPosition().x + circleShape.getRadius(), circleShape.getPosition().y - circleShape.getRadius()), // bottom right
                            new Vector2(circleShape.getPosition().x + circleShape.getRadius(), circleShape.getPosition().y + circleShape.getRadius()) // top right
                    };
                    break;
                default:
                    throw new IllegalArgumentException("Shapes of the type '" + shape.getType().name() + "' are not supported");
            }

            if (autoShapeCache && shapeCache.size < autoShapeCacheMaxSize) {
                Vector2[] cachedOutput = new Vector2[output.length];
                System.arraycopy(output, 0, cachedOutput, 0, output.length);
                shapeCache.put(shape, new ShapeCache(cachedOutput, amplitude(filterX(cachedOutput)), amplitude(filterY(cachedOutput)), min(filterX(cachedOutput)), max(filterX(cachedOutput)), min(filterY(cachedOutput)), max(filterY(cachedOutput))));
            }

            return output;
        }
    }

    /**
     * @see #vertices(Shape, Vector2[])
     */
    public static Vector2[] vertices(Shape shape) {
        return vertices(shape, tmpVecArr);
    }

    /**
     * @return the minimal x value of the vertices of all fixtures of the the given Body
     */
    public static float minX(Body body) {
        float x = Float.POSITIVE_INFINITY, tmp;
        for (Fixture fixture : body.getFixtureList())
            x = (tmp = minX(fixture)) < x ? tmp : x;
        return x;
    }

    /**
     * @return the minimal y value of the vertices of all fixtures of the the given Body
     */
    public static float minY(Body body) {
        float y = Float.POSITIVE_INFINITY, tmp;
        for (Fixture fixture : body.getFixtureList())
            y = (tmp = minY(fixture)) < y ? tmp : y;
        return y;
    }

    /**
     * @return the maximal x value of the vertices of all fixtures of the the given Body
     */
    public static float maxX(Body body) {
        float x = Float.NEGATIVE_INFINITY, tmp;
        for (Fixture fixture : body.getFixtureList())
            x = (tmp = maxX(fixture)) > x ? tmp : x;
        return x;
    }

    /**
     * @return the maximal y value of the vertices of all fixtures of the the given Body
     */
    public static float maxY(Body body) {
        float y = Float.NEGATIVE_INFINITY, tmp;
        for (Fixture fixture : body.getFixtureList())
            y = (tmp = maxY(fixture)) > y ? tmp : y;
        return y;
    }

    /**
     * @return the minimal x value of the vertices of the given Fixture
     */
    public static float minX(Fixture fixture) {
        return minX(fixture.getShape());
    }

    /**
     * @return the minimal y value of the vertices of the given Fixture
     */
    public static float minY(Fixture fixture) {
        return minY(fixture.getShape());
    }

    /**
     * @return the maximal x value of the vertices of the given Fixture
     */
    public static float maxX(Fixture fixture) {
        return maxX(fixture.getShape());
    }

    /**
     * @return the maximal y value of the vertices of the given Fixture
     */
    public static float maxY(Fixture fixture) {
        return maxY(fixture.getShape());
    }

    public static float minX(Shape shape) {
        if (shapeCache.containsKey(shape))
            return shapeCache.get(shape).minX;
        return min(filterX(vertices(shape)));
    }

    public static float minY(Shape shape) {
        if (shapeCache.containsKey(shape))
            return shapeCache.get(shape).minY;
        return min(filterY(vertices(shape)));
    }

    public static float maxX(Shape shape) {
        if (shapeCache.containsKey(shape))
            return shapeCache.get(shape).maxX;
        return max(filterX(vertices(shape)));
    }

    public static float maxY(Shape shape) {
        if (shapeCache.containsKey(shape))
            return shapeCache.get(shape).maxY;
        return max(filterY(vertices(shape)));
    }

    /**
     * @return the width of the given Body
     */
    public static float width(Body body) {
        float min = Float.POSITIVE_INFINITY, max = Float.NEGATIVE_INFINITY, tmp;

        for (Fixture fixture : body.getFixtureList()) {
            min = (tmp = minX(fixture)) < min ? tmp : min;
            max = (tmp = maxX(fixture)) > max ? tmp : max;
        }

        return Math.abs(max - min);
    }

    /**
     * @return the height of the given Body
     */
    public static float height(Body body) {
        float min = Float.POSITIVE_INFINITY, max = Float.NEGATIVE_INFINITY, tmp;

        for (Fixture fixture : body.getFixtureList()) {
            min = (tmp = minY(fixture)) < min ? tmp : min;
            max = (tmp = maxY(fixture)) > max ? tmp : max;
        }

        return Math.abs(max - min);
    }

    /**
     * @return the width of the given Fixture
     */
    public static float width(Fixture fixture) {
        return width(fixture.getShape());
    }

    /**
     * @return the height of the given Fixture
     */
    public static float height(Fixture fixture) {
        return height(fixture.getShape());
    }

    /**
     * @return the width of the given Shape
     */
    public static float width(Shape shape) {
        if (shapeCache.containsKey(shape))
            return shapeCache.get(shape).width;
        return amplitude(filterX(vertices(shape)));
    }

    /**
     * @return the height of the given Shape
     */
    public static float height(Shape shape) {
        if (shapeCache.containsKey(shape))
            return shapeCache.get(shape).height;
        return amplitude(filterY(vertices(shape)));
    }

    /**
     * @see #size(Shape)
     */
    public static Vector2 size(Shape shape, Vector2 output) {
        if (shape.getType() == Type.Circle) // no call to #vertices(Shape) for performance
            return output.set(shape.getRadius() * 2, shape.getRadius() * 2);
        else if (shapeCache.containsKey(shape))
            return output.set(shapeCache.get(shape).width, shapeCache.get(shape).height);
        return output.set(width(shape), height(shape));
    }

    /**
     * @return the size of the given Shape
     */
    public static Vector2 size(Shape shape) {
        return size(shape, tmpVec);
    }

    /**
     * @see #positionRelative(CircleShape)
     */
    public static Vector2 positionRelative(CircleShape shape, Vector2 output) {
        return output.set(shape.getPosition());
    }

    /**
     * @return the relative position of the given CircleShape to its Body
     */
    public static Vector2 positionRelative(CircleShape shape) {
        return shape.getPosition();
    }

    /**
     * @param rotation the rotation of the bodyA in radians
     * @return the relative position of the given Shape to its Body
     */
    public static Vector2 positionRelative(Shape shape, float rotation, Vector2 output) {
        // get the position without rotation
        if (shapeCache.containsKey(shape)) {
            ShapeCache sc = shapeCache.get(shape);
            output.set(sc.maxX - sc.width / 2, sc.maxY - sc.height / 2);
        } else {
            tmpVecArr = vertices(shape); // the shape's vertices will hopefully be put in #shapeCache
            if (shapeCache.containsKey(shape)) // the shape's vertices are now hopefully in #shapeCache, so let's try again
                positionRelative(shape, rotation, output);
            else
                // #autoShapeCache is false or #shapeCache reached #autoShapeCacheSize
                output.set(max(filterX(tmpVecArr)) - amplitude(filterX(tmpVecArr)) / 2, max(filterY(tmpVecArr)) - amplitude(filterY(tmpVecArr)) / 2); // so calculating manually is faster than using the methods because there won't be the containsKey checks
        }

        // transform position according to rotation
        // http://stackoverflow.com/questions/1469149/calculating-vertices-of-a-rotated-rectangle
        float xx = output.x, xy = output.y, yx = output.x, yy = output.y;

        xx = xx * cos(rotation) - xy * sin(rotation);
        yy = yx * sin(rotation) + yy * cos(rotation);

        return output.set(xx, yy);
    }

    /**
     * @see #positionRelative(Shape, float, Vector2)
     */
    public static Vector2 positionRelative(Shape shape, float rotation) {
        return positionRelative(shape, rotation, tmpVec);
    }

    /**
     * @see #position(Fixture)
     */
    public static Vector2 position(Fixture fixture, Vector2 output) {
        return output.set(position(fixture.getShape(), fixture.getBody()));
    }

    /**
     * @return the position of the given Fixture in world coordinates
     */
    public static Vector2 position(Fixture fixture) {
        return position(fixture.getShape(), fixture.getBody(), tmpVec);
    }

    /**
     * @see #position(Shape, Body)
     */
    public static Vector2 position(Shape shape, Body body, Vector2 output) {
        return output.set(body.getPosition().add(positionRelative(shape, body.getTransform().getRotation(), output)));
    }

    /**
     * @param shape the Shape which position to get
     * @param body  the Body the given Shape is attached to
     * @return the position of the given Shape in world coordinates
     */
    public static Vector2 position(Shape shape, Body body) {
        return body.getPosition().add(positionRelative(shape, body.getTransform().getRotation(), tmpVec));
    }

    /**
     * @return the autoShapeCache
     */
    public static boolean isAutoShapeCache() {
        return autoShapeCache;
    }

    /**
     * @param autoShapeCache the autoShapeCache to set
     */
    public static void setAutoShapeCache(boolean autoShapeCache) {
        Box2DUtils.autoShapeCache = autoShapeCache;
    }

    /**
     * @return the autoShapeCacheMaxSize
     */
    public static int getAutoShapeCacheMaxSize() {
        return autoShapeCacheMaxSize;
    }

    /**
     * @param autoShapeCacheMaxSize the autoShapeCacheMaxSize to set
     */
    public static void setAutoShapeCacheMaxSize(int autoShapeCacheMaxSize) {
        Box2DUtils.autoShapeCacheMaxSize = autoShapeCacheMaxSize;
    }

    /**
     * cached method results
     */
    public static class ShapeCache {

        public final Vector2[] vertices;
        public final float width, height, minX, maxX, minY, maxY;

        public ShapeCache(Vector2[] vertices, float width, float height, float minX, float maxX, float minY, float maxY) {
            this.vertices = vertices;
            this.width = width;
            this.height = height;
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
        }

    }

}
