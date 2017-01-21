package com.ilargia.games.entitas.interfaces;

public interface ISystem {

    interface IInitializeSystem extends ISystem {
        void initialize();
    }

    interface IExecuteSystem extends ISystem {
        void execute(float deltaTime);
    }

    interface ICleanupSystem extends ISystem {
        public void cleanup();
    }

    interface IRenderSystem extends ISystem {
        public void render();
    }

    interface ITearDownSystem extends ISystem {
        public void tearDown();
    }


}