package ilargia.egdx.api.managers;

import ilargia.egdx.api.managers.data.PointerState;

public interface RaycasterManager<V2,V3> extends Manager {

    Integer raycast(String raycaster, PointerState<V2,V3> pointerState);

    Integer raycast(String raycaster, PointerState<V2,V3> pointerState, PointerState<V2,V3> pointerState2);
}
