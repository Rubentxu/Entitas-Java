package com.ilargia.games.egdx.logicbricks.system.actuator;


import com.ilargia.games.egdx.logicbricks.component.actuator.CameraActuator;
import com.ilargia.games.egdx.logicbricks.component.actuator.ParticleEffectActuator;
import com.ilargia.games.egdx.logicbricks.component.actuator.TextureActuator;
import com.ilargia.games.egdx.logicbricks.component.actuator.VelocityActuator;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorContext;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorMatcher;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorEntity;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.group.Group;


public class ActuatorSystem implements IExecuteSystem {
    private final Group<ActuatorEntity> groupVelocity;
    private final Group<ActuatorEntity> groupTexture;
    private final Group<ActuatorEntity> groupCamera;
    private final Group<ActuatorEntity> groupEffect;

    public ActuatorSystem(Entitas entitas) {
        ActuatorContext context = entitas.actuator;
        this.groupTexture = context.getGroup(ActuatorMatcher.TextureActuator());;
        this.groupVelocity = context.getGroup(ActuatorMatcher.VelocityActuator());
        this.groupCamera = context.getGroup(ActuatorMatcher.CameraActuator());
        this.groupEffect = context.getGroup(ActuatorMatcher.ParticleEffectActuator());
    }

    @Override
    public void execute(float deltaTime) {
        for (ActuatorEntity e : groupTexture.getEntities()) {
            if(e.getLink().isOpen) {
                TextureActuator textureActuator = e.getTextureActuator();
                textureActuator.actuator.execute(e.getLink().ownerEntity);
            }
        }

        for (ActuatorEntity e : groupVelocity.getEntities()) {
            if(e.getLink().isOpen) {
                VelocityActuator velocityActuator = e.getVelocityActuator();
                velocityActuator.actuator.execute(e.getLink().ownerEntity);
            }

        }

        for (ActuatorEntity e : groupCamera.getEntities()) {
            if(e.getLink().isOpen) {
                CameraActuator cameraActuator = e.getCameraActuator();
                cameraActuator.actuator.execute(e.getLink().ownerEntity);
            }
        }

        for (ActuatorEntity e : groupEffect.getEntities()) {
            if(e.getLink().isOpen) {
                ParticleEffectActuator effectActuator = e.getParticleEffectActuator();
                effectActuator.actuator.execute(e.getLink().ownerEntity);
            }
        }


    }

}


