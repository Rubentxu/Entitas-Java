package com.ilargia.games.egdx.util;


import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import com.badlogic.gdx.physics.box2d.joints.GearJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.PulleyJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import java.util.Iterator;

import com.ilargia.games.entitas.api.IEntity;
import net.dermetfan.gdx.maps.MapUtils;
import net.dermetfan.gdx.math.GeometryUtils;
import net.dermetfan.gdx.physics.box2d.Box2DUtils;

public class MapEntityParser {
    private net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser.Aliases aliases = new net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser.Aliases();
    public static final net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser.Listener.Adapter defaultListener = new net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser.Listener.Adapter();
    private net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser.Listener listener;
    private float unitScale;
    private boolean ignoreMapUnitScale;
    private boolean ignoreLayerUnitScale;
    private float tileWidth;
    private float tileHeight;
    private boolean triangulate;
    private MapProperties heritage;
    private ObjectMap<String, IEntity> entities;
    private ObjectMap<String, Fixture> fixtures;
    private ObjectMap<String, Joint> joints;
    private MapProperties mapProperties;
    private MapProperties layerProperties;
    private final Vector2 vec2;
    private final Vector3 vec3;
    private final Matrix4 mat4;

    public MapEntityParser() {
        this.listener = defaultListener;
        this.unitScale = 1.0F;
        this.tileWidth = 1.0F;
        this.tileHeight = 1.0F;
        this.entities = new ObjectMap();
        this.fixtures = new ObjectMap();
        this.joints = new ObjectMap();
        this.vec2 = new Vector2();
        this.vec3 = new Vector3();
        this.mat4 = new Matrix4();
    }


    public World load(World world, Map map) {
        MapProperties oldMapProperties = this.mapProperties;
        this.mapProperties = map.getProperties();
        world.setGravity(this.vec2.set(((Float)MapUtils.getProperty(this.mapProperties, this.aliases.gravityX, Float.valueOf(world.getGravity().x))).floatValue(), ((Float)MapUtils.getProperty(this.mapProperties, this.aliases.gravityY, Float.valueOf(world.getGravity().y))).floatValue()));
        world.setAutoClearForces(((Boolean)MapUtils.getProperty(this.mapProperties, this.aliases.autoClearForces, Boolean.valueOf(world.getAutoClearForces()))).booleanValue());
        if(!this.ignoreMapUnitScale) {
            this.unitScale = ((Float)MapUtils.getProperty(this.mapProperties, this.aliases.unitScale, Float.valueOf(this.unitScale))).floatValue();
        }

        this.tileWidth = ((Float)MapUtils.getProperty(this.mapProperties, this.aliases.tileWidth, Float.valueOf(this.tileWidth))).floatValue();
        this.tileHeight = ((Float)MapUtils.getProperty(this.mapProperties, this.aliases.tileHeight, Float.valueOf(this.tileHeight))).floatValue();
        this.listener.init(this);
        Array layers = (Array)Pools.obtain(Array.class);
        layers.clear();
        this.listener.load(map, layers);
        Iterator var5 = layers.iterator();

        while(var5.hasNext()) {
            MapLayer mapLayer = (MapLayer)var5.next();
            this.load(world, mapLayer);
        }

        layers.clear();
        Pools.free(layers);
        this.mapProperties = oldMapProperties;
        return world;
    }

    public World load(World world, MapLayer layer) {
        MapProperties oldLayerProperties = this.layerProperties;
        this.layerProperties = layer.getProperties();
        float oldUnitScale = this.unitScale;
        if(!this.ignoreLayerUnitScale) {
            this.unitScale = ((Float)MapUtils.getProperty(layer.getProperties(), this.aliases.unitScale, Float.valueOf(this.unitScale))).floatValue();
        }

        String typeFallback = (String)MapUtils.findProperty(this.aliases.type, "", new MapProperties[]{this.heritage, this.mapProperties, this.layerProperties});
        Array objects = (Array)Pools.obtain(Array.class);
        objects.clear();
        this.listener.load(layer, objects);
        Iterator var7 = objects.iterator();

        MapObject object;
        String type;
        while(var7.hasNext()) {
            object = (MapObject)var7.next();
            type = (String)MapUtils.getProperty(object.getProperties(), this.aliases.type, typeFallback);
            if(type.equals(this.aliases.object)) {
                this.createObject(world, object);
            }
        }

        var7 = objects.iterator();

        while(var7.hasNext()) {
            object = (MapObject)var7.next();
            type = (String)MapUtils.getProperty(object.getProperties(), this.aliases.type, typeFallback);
            if(type.equals(this.aliases.body)) {
                this.createBody(world, object);
            }
        }

        var7 = objects.iterator();

        while(var7.hasNext()) {
            object = (MapObject)var7.next();
            type = (String)MapUtils.getProperty(object.getProperties(), this.aliases.type, typeFallback);
            if(type.equals(this.aliases.fixture)) {
                this.createFixtures(object);
            }
        }

        var7 = objects.iterator();

        while(var7.hasNext()) {
            object = (MapObject)var7.next();
            type = (String)MapUtils.getProperty(object.getProperties(), this.aliases.type, typeFallback);
            if(type.equals(this.aliases.joint)) {
                this.createJoint(object);
            }
        }

        objects.clear();
        Pools.free(objects);
        this.layerProperties = oldLayerProperties;
        this.unitScale = oldUnitScale;
        return world;
    }

    public Body createObject(World world, MapObject object) {
        if((object = this.listener.createObject(object)) == null) {
            return null;
        } else {
            Body body = this.createBody(world, object);
            this.createFixtures(object, body);
            return body;
        }
    }

    public Body createBody(World world, MapObject mapObject) {
        if((mapObject = this.listener.createBody(mapObject)) == null) {
            return null;
        } else {
            MapProperties properties = mapObject.getProperties();
            BodyDef bodyDef = new BodyDef();
            this.assignProperties(bodyDef, this.heritage);
            this.assignProperties(bodyDef, this.mapProperties);
            this.assignProperties(bodyDef, this.layerProperties);
            this.assignProperties(bodyDef, properties);
            Body body = world.createBody(bodyDef);
            body.setUserData(MapUtils.findProperty(this.aliases.userData, body.getUserData(), new MapProperties[]{this.heritage, this.mapProperties, this.layerProperties, properties}));
            this.entities.put(findAvailableName(mapObject.getName(), this.entities), body);
            this.listener.created(body, mapObject);
            return body;
        }
    }

    public Fixture createFixture(MapObject mapObject, Body body) {
        if((mapObject = this.listener.createFixture(mapObject)) == null) {
            return null;
        } else {
            String orientation = (String)MapUtils.findProperty(this.aliases.orientation, this.aliases.orthogonal, new MapProperties[]{this.heritage, this.mapProperties, this.layerProperties, mapObject.getProperties()});
            this.transform(this.mat4, orientation);
            Object shape = null;
            if(mapObject instanceof RectangleMapObject) {
                Rectangle properties = ((RectangleMapObject)mapObject).getRectangle();
                this.vec3.set(properties.x, properties.y, 0.0F);
                this.vec3.mul(this.mat4);
                float fixtureDef = this.vec3.x;
                float fixture = this.vec3.y;
                float width;
                float height;
                if(!orientation.equals(this.aliases.staggered)) {
                    this.vec3.set(properties.width, properties.height, 0.0F).mul(this.mat4);
                    width = this.vec3.x;
                    height = this.vec3.y;
                } else {
                    width = properties.width * this.unitScale;
                    height = properties.height * this.unitScale;
                }

                ((PolygonShape)((PolygonShape)(shape = new PolygonShape()))).setAsBox(width / 2.0F, height / 2.0F, this.vec2.set(fixtureDef - body.getPosition().x + width / 2.0F, fixture - body.getPosition().y + height / 2.0F), body.getAngle());
            } else if(!(mapObject instanceof PolygonMapObject) && !(mapObject instanceof PolylineMapObject)) {
                if(!(mapObject instanceof CircleMapObject) && !(mapObject instanceof EllipseMapObject)) {
                    if(mapObject instanceof TextureMapObject) {
                        throw new IllegalArgumentException("Cannot parse " + mapObject.getName() + " because " + ClassReflection.getSimpleName(mapObject.getClass()) + "s are not supported");
                    }

                    assert false : mapObject + " is a not known subclass of " + MapObject.class.getName();
                } else {
                    if(mapObject instanceof CircleMapObject) {
                        Circle properties2 = ((CircleMapObject)mapObject).getCircle();
                        this.vec3.set(properties2.x, properties2.y, properties2.radius);
                    } else {
                        Ellipse properties3 = ((EllipseMapObject)mapObject).getEllipse();
                        if(properties3.width != properties3.height) {
                            throw new IllegalArgumentException("Cannot parse " + mapObject.getName() + " because " + ClassReflection.getSimpleName(mapObject.getClass()) + "s that are not circles are not supported");
                        }

                        this.vec3.set(properties3.x + properties3.width / 2.0F, properties3.y + properties3.height / 2.0F, properties3.width / 2.0F);
                    }

                    this.vec3.mul(this.mat4);
                    this.vec3.sub(body.getPosition().x, body.getPosition().y, 0.0F);
                    CircleShape properties4 = (CircleShape)((CircleShape)(shape = new CircleShape()));
                    properties4.setPosition(this.vec2.set(this.vec3.x, this.vec3.y));
                    properties4.setRadius(this.vec3.z);
                }
            } else {
                FloatArray properties1 = (FloatArray)Pools.obtain(FloatArray.class);
                properties1.clear();
                properties1.addAll(mapObject instanceof PolygonMapObject?((PolygonMapObject)mapObject).getPolygon().getTransformedVertices():((PolylineMapObject)mapObject).getPolyline().getTransformedVertices());
                int fixtureDef1 = 0;

                for(int fixture1 = 1; fixture1 < properties1.size; fixture1 += 2) {
                    this.vec3.set(properties1.get(fixtureDef1), properties1.get(fixture1), 0.0F);
                    this.vec3.mul(this.mat4);
                    properties1.set(fixtureDef1, this.vec3.x - body.getPosition().x);
                    properties1.set(fixture1, this.vec3.y - body.getPosition().y);
                    fixtureDef1 += 2;
                }

                if(mapObject instanceof PolygonMapObject) {
                    ((PolygonShape)((PolygonShape)(shape = new PolygonShape()))).set(properties1.items, 0, properties1.size);
                } else if(properties1.size == 4) {
                    ((EdgeShape)((EdgeShape)(shape = new EdgeShape()))).set(properties1.get(0), properties1.get(1), properties1.get(2), properties1.get(3));
                } else {
                    properties1.shrink();
                    ((ChainShape)((ChainShape)(shape = new ChainShape()))).createChain(properties1.items);
                }

                Pools.free(properties1);
            }

            MapProperties properties5 = mapObject.getProperties();
            FixtureDef fixtureDef2 = new FixtureDef();
            fixtureDef2.shape = (Shape)shape;
            this.assignProperties(fixtureDef2, this.heritage);
            this.assignProperties(fixtureDef2, this.mapProperties);
            this.assignProperties(fixtureDef2, this.layerProperties);
            this.assignProperties(fixtureDef2, properties5);
            Fixture fixture2 = body.createFixture(fixtureDef2);
            fixture2.setUserData(MapUtils.findProperty(this.aliases.userData, fixture2.getUserData(), new MapProperties[]{this.heritage, this.mapProperties, this.layerProperties, properties5}));
            ((Shape)shape).dispose();
            this.fixtures.put(findAvailableName(mapObject.getName(), this.fixtures), fixture2);
            this.listener.created(fixture2, mapObject);
            return fixture2;
        }
    }

    public Fixture[] createFixtures(MapObject mapObject, Body body) {
        if((mapObject = this.listener.createFixtures(mapObject)) == null) {
            return null;
        } else {
            Polygon polygon;
            if(mapObject instanceof PolygonMapObject && (!GeometryUtils.isConvex(polygon = ((PolygonMapObject)mapObject).getPolygon()) || !Box2DUtils.check.isValidPolygonShape(polygon.getVertices()))) {
                Polygon[] convexPolygons = GeometryUtils.toPolygonArray(this.triangulate?GeometryUtils.triangulate(polygon.getTransformedVertices()):GeometryUtils.decompose(polygon.getTransformedVertices()));
                Fixture[] fixtures = new Fixture[convexPolygons.length];

                for(int i = 0; i < fixtures.length; ++i) {
                    PolygonMapObject convexObject = new PolygonMapObject(convexPolygons[i]);
                    convexObject.setColor(mapObject.getColor());
                    convexObject.setName(mapObject.getName());
                    convexObject.setOpacity(mapObject.getOpacity());
                    convexObject.setVisible(mapObject.isVisible());
                    convexObject.getProperties().putAll(mapObject.getProperties());
                    fixtures[i] = this.createFixture(convexObject, body);
                }

                return fixtures;
            } else {
                return new Fixture[]{this.createFixture(mapObject, body)};
            }
        }
    }

    public Fixture createFixture(MapObject mapObject) {
        return this.createFixture(mapObject, this.findBody(mapObject, new MapProperties[]{this.heritage, this.mapProperties, this.layerProperties}));
    }

    public Fixture[] createFixtures(MapObject mapObject) {
        return this.createFixtures(mapObject, this.findBody(mapObject, new MapProperties[]{this.heritage, this.mapProperties, this.layerProperties}));
    }

    public void transform(Matrix4 mat, String orientation) {
        mat.idt();
        if(orientation.equals(this.aliases.isometric)) {
            mat.scale((float)(Math.sqrt(2.0D) / 2.0D), (float)(Math.sqrt(2.0D) / 4.0D), 1.0F);
            mat.rotate(0.0F, 0.0F, 1.0F, -45.0F);
            mat.translate(-1.0F, 1.0F, 0.0F);
            mat.scale(this.unitScale * 2.0F, this.unitScale * 2.0F, this.unitScale * 2.0F);
        } else if(orientation.equals(this.aliases.staggered)) {
            mat.scale(this.unitScale, this.unitScale, this.unitScale);
            int mapHeight = ((Integer)MapUtils.findProperty(this.aliases.height, Integer.valueOf(0), new MapProperties[]{this.mapProperties, this.layerProperties})).intValue();
            mat.translate(-this.tileWidth / 2.0F, -this.tileHeight * (float)(mapHeight / 2) + this.tileHeight / 2.0F, 0.0F);
        } else {
            mat.scale(this.unitScale, this.unitScale, this.unitScale);
        }

    }

    private Body findBody(MapObject mapObject, MapProperties... heritage) {
        String name = mapObject.getName();
        Body body = null;
        if(name != null) {
            body = (Body)this.entities.get(name);
        }

        if(body == null) {
            body = (Body)this.entities.get(MapUtils.getProperty(mapObject.getProperties(), this.aliases.body, ""));
        }

        if(body == null) {
            MapProperties[] var5 = heritage;
            int var6 = heritage.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                MapProperties properties = var5[var7];
                if((body = (Body)this.entities.get(MapUtils.getProperty(properties, this.aliases.body, ""))) != null) {
                    break;
                }
            }
        }

        if(body == null) {
            throw new IllegalStateException("the body of " + (name == null?"an unnamed ":"the ") + "fixture " + (name != null?name:"") + "does not exist");
        } else {
            return body;
        }
    }

    public Joint createJoint(MapObject mapObject) {
        if((mapObject = this.listener.createJoint(mapObject)) == null) {
            return null;
        } else {
            MapProperties properties = mapObject.getProperties();
            String jointType = (String)MapUtils.getProperty(properties, this.aliases.jointType, "");
            Object jointDef;
            if(jointType.equals(this.aliases.distanceJoint)) {
                DistanceJointDef joint = new DistanceJointDef();
                this.assignProperties(joint, this.heritage);
                this.assignProperties(joint, this.mapProperties);
                this.assignProperties(joint, this.layerProperties);
                this.assignProperties(joint, properties);
                jointDef = joint;
            } else if(jointType.equals(this.aliases.frictionJoint)) {
                FrictionJointDef joint1 = new FrictionJointDef();
                this.assignProperties(joint1, this.heritage);
                this.assignProperties(joint1, this.mapProperties);
                this.assignProperties(joint1, this.layerProperties);
                this.assignProperties(joint1, properties);
                jointDef = joint1;
            } else if(jointType.equals(this.aliases.gearJoint)) {
                GearJointDef joint2 = new GearJointDef();
                this.assignProperties(joint2, this.heritage);
                this.assignProperties(joint2, this.mapProperties);
                this.assignProperties(joint2, this.layerProperties);
                this.assignProperties(joint2, properties);
                jointDef = joint2;
            } else if(jointType.equals(this.aliases.mouseJoint)) {
                MouseJointDef joint3 = new MouseJointDef();
                this.assignProperties(joint3, this.heritage);
                this.assignProperties(joint3, this.mapProperties);
                this.assignProperties(joint3, this.layerProperties);
                this.assignProperties(joint3, properties);
                jointDef = joint3;
            } else if(jointType.equals(this.aliases.prismaticJoint)) {
                PrismaticJointDef joint4 = new PrismaticJointDef();
                this.assignProperties(joint4, this.heritage);
                this.assignProperties(joint4, this.mapProperties);
                this.assignProperties(joint4, this.layerProperties);
                this.assignProperties(joint4, properties);
                jointDef = joint4;
            } else if(jointType.equals(this.aliases.pulleyJoint)) {
                PulleyJointDef joint5 = new PulleyJointDef();
                this.assignProperties(joint5, this.heritage);
                this.assignProperties(joint5, this.mapProperties);
                this.assignProperties(joint5, this.layerProperties);
                this.assignProperties(joint5, properties);
                jointDef = joint5;
            } else if(jointType.equals(this.aliases.revoluteJoint)) {
                RevoluteJointDef joint6 = new RevoluteJointDef();
                this.assignProperties(joint6, this.heritage);
                this.assignProperties(joint6, this.mapProperties);
                this.assignProperties(joint6, this.layerProperties);
                this.assignProperties(joint6, properties);
                jointDef = joint6;
            } else if(jointType.equals(this.aliases.ropeJoint)) {
                RopeJointDef joint7 = new RopeJointDef();
                this.assignProperties(joint7, this.heritage);
                this.assignProperties(joint7, this.mapProperties);
                this.assignProperties(joint7, this.layerProperties);
                this.assignProperties(joint7, properties);
                jointDef = joint7;
            } else if(jointType.equals(this.aliases.weldJoint)) {
                WeldJointDef joint8 = new WeldJointDef();
                this.assignProperties(joint8, this.heritage);
                this.assignProperties(joint8, this.mapProperties);
                this.assignProperties(joint8, this.layerProperties);
                this.assignProperties(joint8, properties);
                jointDef = joint8;
            } else {
                if(!jointType.equals(this.aliases.wheelJoint)) {
                    throw new IllegalArgumentException(ClassReflection.getSimpleName(JointType.class) + " " + jointType + " is unknown");
                }

                WheelJointDef joint9 = new WheelJointDef();
                this.assignProperties(joint9, this.heritage);
                this.assignProperties(joint9, this.mapProperties);
                this.assignProperties(joint9, this.layerProperties);
                this.assignProperties(joint9, properties);
                jointDef = joint9;
            }

            this.assignProperties((JointDef)jointDef, properties);
            Joint joint10 = ((JointDef)jointDef).bodyA.getWorld().createJoint((JointDef)jointDef);
            joint10.setUserData(MapUtils.getProperty(properties, this.aliases.userData, joint10.getUserData()));
            this.joints.put(findAvailableName(mapObject.getName(), this.joints), joint10);
            this.listener.created(joint10, mapObject);
            return joint10;
        }
    }

    public void assignProperties(BodyDef bodyDef, MapProperties properties) {
        if(properties != null) {
            bodyDef.type = ((String)MapUtils.getProperty(properties, this.aliases.bodyType, "")).equals(this.aliases.staticBody)?BodyType.StaticBody:(((String)MapUtils.getProperty(properties, this.aliases.bodyType, "")).equals(this.aliases.dynamicBody)?BodyType.DynamicBody:(((String)MapUtils.getProperty(properties, this.aliases.bodyType, "")).equals(this.aliases.kinematicBody)?BodyType.KinematicBody:bodyDef.type));
            bodyDef.active = ((Boolean)MapUtils.getProperty(properties, this.aliases.active, Boolean.valueOf(bodyDef.active))).booleanValue();
            bodyDef.allowSleep = ((Boolean)MapUtils.getProperty(properties, this.aliases.allowSleep, Boolean.valueOf(bodyDef.allowSleep))).booleanValue();
            bodyDef.angle = ((Float)MapUtils.getProperty(properties, this.aliases.angle, Float.valueOf(bodyDef.angle))).floatValue() * 0.017453292F;
            bodyDef.angularDamping = ((Float)MapUtils.getProperty(properties, this.aliases.angularDamping, Float.valueOf(bodyDef.angularDamping))).floatValue();
            bodyDef.angularVelocity = ((Float)MapUtils.getProperty(properties, this.aliases.angularVelocity, Float.valueOf(bodyDef.angularVelocity))).floatValue();
            bodyDef.awake = ((Boolean)MapUtils.getProperty(properties, this.aliases.awake, Boolean.valueOf(bodyDef.awake))).booleanValue();
            bodyDef.bullet = ((Boolean)MapUtils.getProperty(properties, this.aliases.bullet, Boolean.valueOf(bodyDef.bullet))).booleanValue();
            bodyDef.fixedRotation = ((Boolean)MapUtils.getProperty(properties, this.aliases.fixedRotation, Boolean.valueOf(bodyDef.fixedRotation))).booleanValue();
            bodyDef.gravityScale = ((Float)MapUtils.getProperty(properties, this.aliases.gravityScale, Float.valueOf(bodyDef.gravityScale))).floatValue();
            bodyDef.linearDamping = ((Float)MapUtils.getProperty(properties, this.aliases.linearDamping, Float.valueOf(bodyDef.linearDamping))).floatValue();
            bodyDef.linearVelocity.set(((Float)MapUtils.getProperty(properties, this.aliases.linearVelocityX, Float.valueOf(bodyDef.linearVelocity.x))).floatValue(), ((Float)MapUtils.getProperty(properties, this.aliases.linearVelocityY, Float.valueOf(bodyDef.linearVelocity.y))).floatValue());
            bodyDef.position.set(((Float)MapUtils.getProperty(properties, this.aliases.x, Float.valueOf(bodyDef.position.x))).floatValue() * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.y, Float.valueOf(bodyDef.position.y))).floatValue() * this.unitScale);
        }
    }

    public void assignProperties(FixtureDef fixtureDef, MapProperties properties) {
        if(properties != null) {
            fixtureDef.density = ((Float)MapUtils.getProperty(properties, this.aliases.density, Float.valueOf(fixtureDef.density))).floatValue();
            fixtureDef.filter.categoryBits = ((Short)MapUtils.getProperty(properties, this.aliases.categoryBits, Short.valueOf(fixtureDef.filter.categoryBits))).shortValue();
            fixtureDef.filter.groupIndex = ((Short)MapUtils.getProperty(properties, this.aliases.groupIndex, Short.valueOf(fixtureDef.filter.groupIndex))).shortValue();
            fixtureDef.filter.maskBits = ((Short)MapUtils.getProperty(properties, this.aliases.maskBits, Short.valueOf(fixtureDef.filter.maskBits))).shortValue();
            fixtureDef.friction = ((Float)MapUtils.getProperty(properties, this.aliases.friciton, Float.valueOf(fixtureDef.friction))).floatValue();
            fixtureDef.isSensor = ((Boolean)MapUtils.getProperty(properties, this.aliases.isSensor, Boolean.valueOf(fixtureDef.isSensor))).booleanValue();
            fixtureDef.restitution = ((Float)MapUtils.getProperty(properties, this.aliases.restitution, Float.valueOf(fixtureDef.restitution))).floatValue();
        }
    }

    public void assignProperties(JointDef jointDef, MapProperties properties) {
        if(properties != null) {
            jointDef.bodyA = (Body)this.entities.get(MapUtils.getProperty(properties, this.aliases.bodyA, ""));
            jointDef.bodyB = (Body)this.entities.get(MapUtils.getProperty(properties, this.aliases.bodyB, ""));
            jointDef.collideConnected = ((Boolean)MapUtils.getProperty(properties, this.aliases.collideConnected, Boolean.valueOf(jointDef.collideConnected))).booleanValue();
        }
    }

    public void assignProperties(DistanceJointDef distanceJointDef, MapProperties properties) {
        if(properties != null) {
            distanceJointDef.dampingRatio = ((Float)MapUtils.getProperty(properties, this.aliases.dampingRatio, Float.valueOf(distanceJointDef.dampingRatio))).floatValue();
            distanceJointDef.frequencyHz = ((Float)MapUtils.getProperty(properties, this.aliases.frequencyHz, Float.valueOf(distanceJointDef.frequencyHz))).floatValue();
            distanceJointDef.length = ((Float)MapUtils.getProperty(properties, this.aliases.length, Float.valueOf(distanceJointDef.length))).floatValue() * (this.tileWidth + this.tileHeight) / 2.0F * this.unitScale;
            distanceJointDef.localAnchorA.set(((Float)MapUtils.getProperty(properties, this.aliases.localAnchorAX, Float.valueOf(distanceJointDef.localAnchorA.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.localAnchorAY, Float.valueOf(distanceJointDef.localAnchorA.y))).floatValue() * this.tileHeight * this.unitScale);
            distanceJointDef.localAnchorB.set(((Float)MapUtils.getProperty(properties, this.aliases.localAnchorBX, Float.valueOf(distanceJointDef.localAnchorB.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.localAnchorBY, Float.valueOf(distanceJointDef.localAnchorB.y))).floatValue() * this.tileHeight * this.unitScale);
        }
    }

    public void assignProperties(FrictionJointDef frictionJointDef, MapProperties properties) {
        if(properties != null) {
            frictionJointDef.localAnchorA.set(((Float)MapUtils.getProperty(properties, this.aliases.localAnchorAX, Float.valueOf(frictionJointDef.localAnchorA.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.localAnchorAY, Float.valueOf(frictionJointDef.localAnchorA.y))).floatValue() * this.tileHeight * this.unitScale);
            frictionJointDef.localAnchorB.set(((Float)MapUtils.getProperty(properties, this.aliases.localAnchorBX, Float.valueOf(frictionJointDef.localAnchorB.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.localAnchorBY, Float.valueOf(frictionJointDef.localAnchorB.y))).floatValue() * this.tileHeight * this.unitScale);
            frictionJointDef.maxForce = ((Float)MapUtils.getProperty(properties, this.aliases.maxForce, Float.valueOf(frictionJointDef.maxForce))).floatValue();
            frictionJointDef.maxTorque = ((Float)MapUtils.getProperty(properties, this.aliases.maxTorque, Float.valueOf(frictionJointDef.maxTorque))).floatValue();
        }
    }

    public void assignProperties(GearJointDef gearJointDef, MapProperties properties) {
        if(properties != null) {
            gearJointDef.joint1 = (Joint)this.joints.get(MapUtils.getProperty(properties, this.aliases.joint1, ""));
            gearJointDef.joint2 = (Joint)this.joints.get(MapUtils.getProperty(properties, this.aliases.joint2, ""));
            gearJointDef.ratio = ((Float)MapUtils.getProperty(properties, this.aliases.ratio, Float.valueOf(gearJointDef.ratio))).floatValue();
        }
    }

    public void assignProperties(MouseJointDef mouseJointDef, MapProperties properties) {
        if(properties != null) {
            mouseJointDef.dampingRatio = ((Float)MapUtils.getProperty(properties, this.aliases.dampingRatio, Float.valueOf(mouseJointDef.dampingRatio))).floatValue();
            mouseJointDef.frequencyHz = ((Float)MapUtils.getProperty(properties, this.aliases.frequencyHz, Float.valueOf(mouseJointDef.frequencyHz))).floatValue();
            mouseJointDef.maxForce = ((Float)MapUtils.getProperty(properties, this.aliases.maxForce, Float.valueOf(mouseJointDef.maxForce))).floatValue();
            mouseJointDef.target.set(((Float)MapUtils.getProperty(properties, this.aliases.targetX, Float.valueOf(mouseJointDef.target.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.targetY, Float.valueOf(mouseJointDef.target.y))).floatValue() * this.tileHeight * this.unitScale);
        }
    }

    public void assignProperties(PrismaticJointDef prismaticJointDef, MapProperties properties) {
        if(properties != null) {
            prismaticJointDef.enableLimit = ((Boolean)MapUtils.getProperty(properties, this.aliases.enableLimit, Boolean.valueOf(prismaticJointDef.enableLimit))).booleanValue();
            prismaticJointDef.enableMotor = ((Boolean)MapUtils.getProperty(properties, this.aliases.enableMotor, Boolean.valueOf(prismaticJointDef.enableMotor))).booleanValue();
            prismaticJointDef.localAnchorA.set(((Float)MapUtils.getProperty(properties, this.aliases.localAnchorAX, Float.valueOf(prismaticJointDef.localAnchorA.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.localAnchorAY, Float.valueOf(prismaticJointDef.localAnchorA.y))).floatValue() * this.tileHeight * this.unitScale);
            prismaticJointDef.localAnchorB.set(((Float)MapUtils.getProperty(properties, this.aliases.localAnchorBX, Float.valueOf(prismaticJointDef.localAnchorB.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.localAnchorBY, Float.valueOf(prismaticJointDef.localAnchorB.y))).floatValue() * this.tileHeight * this.unitScale);
            prismaticJointDef.localAxisA.set(((Float)MapUtils.getProperty(properties, this.aliases.localAxisAX, Float.valueOf(prismaticJointDef.localAxisA.x))).floatValue(), ((Float)MapUtils.getProperty(properties, this.aliases.localAxisAY, Float.valueOf(prismaticJointDef.localAxisA.y))).floatValue());
            prismaticJointDef.lowerTranslation = ((Float)MapUtils.getProperty(properties, this.aliases.lowerTranslation, Float.valueOf(prismaticJointDef.lowerTranslation))).floatValue() * (this.tileWidth + this.tileHeight) / 2.0F * this.unitScale;
            prismaticJointDef.maxMotorForce = ((Float)MapUtils.getProperty(properties, this.aliases.maxMotorForce, Float.valueOf(prismaticJointDef.maxMotorForce))).floatValue();
            prismaticJointDef.motorSpeed = ((Float)MapUtils.getProperty(properties, this.aliases.motorSpeed, Float.valueOf(prismaticJointDef.motorSpeed))).floatValue();
            prismaticJointDef.referenceAngle = ((Float)MapUtils.getProperty(properties, this.aliases.referenceAngle, Float.valueOf(prismaticJointDef.referenceAngle))).floatValue() * 0.017453292F;
            prismaticJointDef.upperTranslation = ((Float)MapUtils.getProperty(properties, this.aliases.upperTranslation, Float.valueOf(prismaticJointDef.upperTranslation))).floatValue() * (this.tileWidth + this.tileHeight) / 2.0F * this.unitScale;
        }
    }

    public void assignProperties(PulleyJointDef pulleyJointDef, MapProperties properties) {
        if(properties != null) {
            pulleyJointDef.groundAnchorA.set(((Float)MapUtils.getProperty(properties, this.aliases.groundAnchorAX, Float.valueOf(pulleyJointDef.groundAnchorA.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.groundAnchorAY, Float.valueOf(pulleyJointDef.groundAnchorA.y))).floatValue() * this.tileHeight * this.unitScale);
            pulleyJointDef.groundAnchorB.set(((Float)MapUtils.getProperty(properties, this.aliases.groundAnchorBX, Float.valueOf(pulleyJointDef.groundAnchorB.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.groundAnchorBY, Float.valueOf(pulleyJointDef.groundAnchorB.y))).floatValue() * this.tileHeight * this.unitScale);
            pulleyJointDef.lengthA = ((Float)MapUtils.getProperty(properties, this.aliases.lengthA, Float.valueOf(pulleyJointDef.lengthA))).floatValue() * (this.tileWidth + this.tileHeight) / 2.0F * this.unitScale;
            pulleyJointDef.lengthB = ((Float)MapUtils.getProperty(properties, this.aliases.lengthB, Float.valueOf(pulleyJointDef.lengthB))).floatValue() * (this.tileWidth + this.tileHeight) / 2.0F * this.unitScale;
            pulleyJointDef.localAnchorA.set(((Float)MapUtils.getProperty(properties, this.aliases.localAnchorAX, Float.valueOf(pulleyJointDef.localAnchorA.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.localAnchorAY, Float.valueOf(pulleyJointDef.localAnchorA.y))).floatValue() * this.tileHeight * this.unitScale);
            pulleyJointDef.localAnchorB.set(((Float)MapUtils.getProperty(properties, this.aliases.localAnchorBX, Float.valueOf(pulleyJointDef.localAnchorB.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.localAnchorBY, Float.valueOf(pulleyJointDef.localAnchorB.y))).floatValue() * this.tileHeight * this.unitScale);
            pulleyJointDef.ratio = ((Float)MapUtils.getProperty(properties, this.aliases.ratio, Float.valueOf(pulleyJointDef.ratio))).floatValue();
        }
    }

    public void assignProperties(RevoluteJointDef revoluteJointDef, MapProperties properties) {
        if(properties != null) {
            revoluteJointDef.enableLimit = ((Boolean)MapUtils.getProperty(properties, this.aliases.enableLimit, Boolean.valueOf(revoluteJointDef.enableLimit))).booleanValue();
            revoluteJointDef.enableMotor = ((Boolean)MapUtils.getProperty(properties, this.aliases.enableMotor, Boolean.valueOf(revoluteJointDef.enableMotor))).booleanValue();
            revoluteJointDef.localAnchorA.set(((Float)MapUtils.getProperty(properties, this.aliases.localAnchorAX, Float.valueOf(revoluteJointDef.localAnchorA.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.localAnchorAY, Float.valueOf(revoluteJointDef.localAnchorA.y))).floatValue() * this.tileHeight * this.unitScale);
            revoluteJointDef.localAnchorB.set(((Float)MapUtils.getProperty(properties, this.aliases.localAnchorBX, Float.valueOf(revoluteJointDef.localAnchorB.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.localAnchorBY, Float.valueOf(revoluteJointDef.localAnchorB.y))).floatValue() * this.tileHeight * this.unitScale);
            revoluteJointDef.lowerAngle = ((Float)MapUtils.getProperty(properties, this.aliases.lowerAngle, Float.valueOf(revoluteJointDef.lowerAngle))).floatValue() * 0.017453292F;
            revoluteJointDef.maxMotorTorque = ((Float)MapUtils.getProperty(properties, this.aliases.maxMotorTorque, Float.valueOf(revoluteJointDef.maxMotorTorque))).floatValue();
            revoluteJointDef.motorSpeed = ((Float)MapUtils.getProperty(properties, this.aliases.motorSpeed, Float.valueOf(revoluteJointDef.motorSpeed))).floatValue();
            revoluteJointDef.referenceAngle = ((Float)MapUtils.getProperty(properties, this.aliases.referenceAngle, Float.valueOf(revoluteJointDef.referenceAngle))).floatValue() * 0.017453292F;
            revoluteJointDef.upperAngle = ((Float)MapUtils.getProperty(properties, this.aliases.upperAngle, Float.valueOf(revoluteJointDef.upperAngle))).floatValue() * 0.017453292F;
        }
    }

    public void assignProperties(RopeJointDef ropeJointDef, MapProperties properties) {
        if(properties != null) {
            ropeJointDef.localAnchorA.set(((Float)MapUtils.getProperty(properties, this.aliases.localAnchorAX, Float.valueOf(ropeJointDef.localAnchorA.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.localAnchorAY, Float.valueOf(ropeJointDef.localAnchorA.y))).floatValue() * this.tileHeight * this.unitScale);
            ropeJointDef.localAnchorB.set(((Float)MapUtils.getProperty(properties, this.aliases.localAnchorBX, Float.valueOf(ropeJointDef.localAnchorB.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.localAnchorBY, Float.valueOf(ropeJointDef.localAnchorB.y))).floatValue() * this.tileHeight * this.unitScale);
            ropeJointDef.maxLength = ((Float)MapUtils.getProperty(properties, this.aliases.maxLength, Float.valueOf(ropeJointDef.maxLength))).floatValue() * (this.tileWidth + this.tileHeight) / 2.0F * this.unitScale;
        }
    }

    public void assignProperties(WeldJointDef weldJointDef, MapProperties properties) {
        if(properties != null) {
            weldJointDef.localAnchorA.set(((Float)MapUtils.getProperty(properties, this.aliases.localAnchorAX, Float.valueOf(weldJointDef.localAnchorA.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.localAnchorAY, Float.valueOf(weldJointDef.localAnchorA.y))).floatValue() * this.tileHeight * this.unitScale);
            weldJointDef.localAnchorB.set(((Float)MapUtils.getProperty(properties, this.aliases.localAnchorBX, Float.valueOf(weldJointDef.localAnchorB.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.localAnchorBY, Float.valueOf(weldJointDef.localAnchorB.y))).floatValue() * this.tileHeight * this.unitScale);
            weldJointDef.referenceAngle = ((Float)MapUtils.getProperty(properties, this.aliases.referenceAngle, Float.valueOf(weldJointDef.referenceAngle))).floatValue() * 0.017453292F;
        }
    }

    public void assignProperties(WheelJointDef wheelJointDef, MapProperties properties) {
        if(properties != null) {
            wheelJointDef.dampingRatio = ((Float)MapUtils.getProperty(properties, this.aliases.dampingRatio, Float.valueOf(wheelJointDef.dampingRatio))).floatValue();
            wheelJointDef.enableMotor = ((Boolean)MapUtils.getProperty(properties, this.aliases.enableMotor, Boolean.valueOf(wheelJointDef.enableMotor))).booleanValue();
            wheelJointDef.frequencyHz = ((Float)MapUtils.getProperty(properties, this.aliases.frequencyHz, Float.valueOf(wheelJointDef.frequencyHz))).floatValue();
            wheelJointDef.localAnchorA.set(((Float)MapUtils.getProperty(properties, this.aliases.localAnchorAX, Float.valueOf(wheelJointDef.localAnchorA.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.localAnchorAY, Float.valueOf(wheelJointDef.localAnchorA.y))).floatValue() * this.tileHeight * this.unitScale);
            wheelJointDef.localAnchorB.set(((Float)MapUtils.getProperty(properties, this.aliases.localAnchorBX, Float.valueOf(wheelJointDef.localAnchorB.x))).floatValue() * this.tileWidth * this.unitScale, ((Float)MapUtils.getProperty(properties, this.aliases.localAnchorBY, Float.valueOf(wheelJointDef.localAnchorB.y))).floatValue() * this.tileHeight * this.unitScale);
            wheelJointDef.localAxisA.set(((Float)MapUtils.getProperty(properties, this.aliases.localAxisAX, Float.valueOf(wheelJointDef.localAxisA.x))).floatValue(), ((Float)MapUtils.getProperty(properties, this.aliases.localAxisAY, Float.valueOf(wheelJointDef.localAxisA.y))).floatValue());
            wheelJointDef.maxMotorTorque = ((Float)MapUtils.getProperty(properties, this.aliases.maxMotorTorque, Float.valueOf(wheelJointDef.maxMotorTorque))).floatValue();
            wheelJointDef.motorSpeed = ((Float)MapUtils.getProperty(properties, this.aliases.motorSpeed, Float.valueOf(wheelJointDef.motorSpeed))).floatValue();
        }
    }

    public static String findAvailableName(String desiredName, ObjectMap<String, ?> map) {
        if(desiredName == null) {
            desiredName = String.valueOf(map.size);
        }

        if(map.containsKey(desiredName)) {
            int duplicate;
            for(duplicate = 1; map.containsKey(desiredName + duplicate); ++duplicate) {
                ;
            }

            desiredName = desiredName + duplicate;
        }

        return desiredName;
    }

    public void reset() {
        this.aliases = new net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser.Aliases();
        this.listener = defaultListener;
        this.unitScale = 1.0F;
        this.tileWidth = 1.0F;
        this.tileHeight = 1.0F;
        this.triangulate = false;
        this.entities.clear();
        this.fixtures.clear();
        this.joints.clear();
        this.heritage = null;
        this.mapProperties = null;
        this.layerProperties = null;
    }

    public float getUnitScale() {
        return this.unitScale;
    }

    public void setUnitScale(float unitScale) {
        this.unitScale = unitScale;
    }

    public boolean isIgnoreMapUnitScale() {
        return this.ignoreMapUnitScale;
    }

    public void setIgnoreMapUnitScale(boolean ignoreMapUnitScale) {
        this.ignoreMapUnitScale = ignoreMapUnitScale;
    }

    public boolean isIgnoreLayerUnitScale() {
        return this.ignoreLayerUnitScale;
    }

    public void setIgnoreLayerUnitScale(boolean ignoreLayerUnitScale) {
        this.ignoreLayerUnitScale = ignoreLayerUnitScale;
    }

    public float getTileWidth() {
        return this.tileWidth;
    }

    public void setTileWidth(float tileWidth) {
        this.tileWidth = tileWidth;
    }

    public float getTileHeight() {
        return this.tileHeight;
    }

    public void setTileHeight(float tileHeight) {
        this.tileHeight = tileHeight;
    }

    public boolean isTriangulate() {
        return this.triangulate;
    }

    public void setTriangulate(boolean triangulate) {
        this.triangulate = triangulate;
    }

    public net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser.Aliases getAliases() {
        return this.aliases;
    }

    public void setAliases(net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser.Aliases aliases) {
        this.aliases = aliases;
    }

    public net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser.Listener getListener() {
        return this.listener;
    }

    public void setListener(net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser.Listener listener) {
        this.listener = (net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser.Listener)(listener != null?listener:defaultListener);
    }

    public ObjectMap<String, Body> getEntities() {
        return this.entities;
    }

    public ObjectMap<String, Fixture> getFixtures() {
        return this.fixtures;
    }

    public ObjectMap<String, Joint> getJoints() {
        return this.joints;
    }

    public MapProperties getHeritage() {
        return this.heritage;
    }

    public void setHeritage(MapProperties heritage) {
        this.heritage = heritage;
    }

    public interface Listener {
        void init(net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser var1);

        void load(Map var1, Array<MapLayer> var2);

        void load(MapLayer var1, Array<MapObject> var2);

        MapObject createObject(MapObject var1);

        MapObject createBody(MapObject var1);

        MapObject createFixtures(MapObject var1);

        MapObject createFixture(MapObject var1);

        MapObject createJoint(MapObject var1);

        void created(Body var1, MapObject var2);

        void created(Fixture var1, MapObject var2);

        void created(Joint var1, MapObject var2);

        public static class Adapter implements net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser.Listener {
            public Adapter() {
            }

            public void init(net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser parser) {
            }

            public void load(Map map, Array<MapLayer> queue) {
                MapLayers layers = map.getLayers();
                queue.ensureCapacity(layers.getCount());
                Iterator var4 = layers.iterator();

                while(var4.hasNext()) {
                    MapLayer layer = (MapLayer)var4.next();
                    queue.add(layer);
                }

            }

            public void load(MapLayer layer, Array<MapObject> queue) {
                MapObjects objects = layer.getObjects();
                queue.ensureCapacity(objects.getCount());
                Iterator var4 = objects.iterator();

                while(var4.hasNext()) {
                    MapObject object = (MapObject)var4.next();
                    queue.add(object);
                }

            }

            public MapObject createObject(MapObject mapObject) {
                return mapObject;
            }

            public MapObject createBody(MapObject mapObject) {
                return mapObject;
            }

            public MapObject createFixtures(MapObject mapObject) {
                return mapObject;
            }

            public MapObject createFixture(MapObject mapObject) {
                return mapObject;
            }

            public MapObject createJoint(MapObject mapObject) {
                return mapObject;
            }

            public void created(Body body, MapObject mapObject) {
            }

            public void created(Fixture fixture, MapObject mapObject) {
            }

            public void created(Joint joint, MapObject mapObject) {
            }
        }
    }

    public static class Aliases {
        public String x = "x";
        public String y = "y";
        public String width = "width";
        public String height = "height";
        public String type = "type";


        public String bodyType = "bodyType";
        public String dynamicBody = "DynamicBody";
        public String kinematicBody = "KinematicBody";
        public String staticBody = "StaticBody";
        public String active = "active";
        public String allowSleep = "allowSleep";
        public String angle = "angle";
        public String angularDamping = "angularDamping";
        public String angularVelocity = "angularVelocity";
        public String awake = "awake";
        public String bullet = "bullet";
        public String fixedRotation = "fixedRotation";
        public String gravityScale = "gravityScale";
        public String linearDamping = "linearDamping";
        public String linearVelocityX = "linearVelocityX";
        public String linearVelocityY = "linearVelocityY";
        public String density = "density";
        public String categoryBits = "categoryBits";
        public String groupIndex = "groupIndex";
        public String maskBits = "maskBits";
        public String friciton = "friction";
        public String isSensor = "isSensor";
        public String restitution = "restitution";
        public String body = "body";
        public String fixture = "fixture";
        public String joint = "joint";
        public String jointType = "jointType";
        public String distanceJoint = "DistanceJoint";
        public String frictionJoint = "FrictionJoint";
        public String gearJoint = "GearJoint";
        public String mouseJoint = "MouseJoint";
        public String prismaticJoint = "PrismaticJoint";
        public String pulleyJoint = "PulleyJoint";
        public String revoluteJoint = "RevoluteJoint";
        public String ropeJoint = "RopeJoint";
        public String weldJoint = "WeldJoint";
        public String wheelJoint = "WheelJoint";
        public String bodyA = "bodyA";
        public String bodyB = "bodyB";
        public String collideConnected = "collideConnected";
        public String dampingRatio = "dampingRatio";
        public String frequencyHz = "frequencyHz";
        public String length = "length";
        public String localAnchorAX = "localAnchorAX";
        public String localAnchorAY = "localAnchorAY";
        public String localAnchorBX = "localAnchorBX";
        public String localAnchorBY = "localAnchorBY";
        public String maxForce = "maxForce";
        public String maxTorque = "maxTorque";
        public String joint1 = "joint1";
        public String joint2 = "joint2";
        public String ratio = "ratio";
        public String targetX = "targetX";
        public String targetY = "targetY";
        public String enableLimit = "enableLimit";
        public String enableMotor = "enableMotor";
        public String localAxisAX = "localAxisAX";
        public String localAxisAY = "localAxisAY";
        public String lowerTranslation = "lowerTranslation";
        public String maxMotorForce = "maxMotorForce";
        public String motorSpeed = "motorSpeed";
        public String referenceAngle = "referenceAngle";
        public String upperTranslation = "upperTranslation";
        public String groundAnchorAX = "groundAnchorAX";
        public String groundAnchorAY = "groundAnchorAY";
        public String groundAnchorBX = "groundAnchorBX";
        public String groundAnchorBY = "groundAnchorBY";
        public String lengthA = "lengthA";
        public String lengthB = "lengthB";
        public String lowerAngle = "lowerAngle";
        public String maxMotorTorque = "maxMotorTorque";
        public String upperAngle = "upperAngle";
        public String maxLength = "maxLength";
        public String object = "object";
        public String unitScale = "unitScale";
        public String userData = "userData";
        public String tileWidth = "tilewidth";
        public String tileHeight = "tileheight";
        public String gravityX = "gravityX";
        public String gravityY = "gravityY";
        public String autoClearForces = "autoClearForces";
        public String orientation = "orientation";
        public String orthogonal = "orthogonal";
        public String isometric = "isometric";
        public String staggered = "staggered";

        public Aliases() {
        }
    }
}
