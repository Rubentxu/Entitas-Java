package ilargia.egdx.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Array;

public class SliceTransition extends RenderTransition {

    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int UP_DOWN = 3;
    private int direction;
    private Interpolation easing;
    private Array<Integer> sliceIndex = new Array<Integer>();


    public SliceTransition(float duration, int direction, int numSlices, Interpolation easing, Batch batch) {
        super(duration, batch);
        this.duration = duration;
        this.direction = direction;
        this.easing = easing;
        this.sliceIndex.clear();
        for (int i = 0; i < numSlices; i++)
            this.sliceIndex.add(i);
        this.sliceIndex.shuffle();

    }

    @Override
    public void render() {
        float w = current.getWidth();
        float h = current.getHeight();
        float x = 0;
        float y = 0;
        int sliceWidth = (int) (w / sliceIndex.size);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(current, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, current.getWidth(), current.getHeight(),
                false, true);
        if (easing != null) alpha = easing.apply(alpha);
        for (int i = 0; i < sliceIndex.size; i++) {

            x = i * sliceWidth;

            float offsetY = h * (1 + sliceIndex.get(i) / (float) sliceIndex.size);
            switch (direction) {
                case UP:
                    y = -offsetY + offsetY * alpha;
                    break;
                case DOWN:
                    y = offsetY - offsetY * alpha;
                    break;
                case UP_DOWN:
                    if (i % 2 == 0) {
                        y = -offsetY + offsetY * alpha;
                    } else {
                        y = offsetY - offsetY * alpha;
                    }
                    break;
            }
            batch.draw(next, x, y, 0, 0, sliceWidth, h, 1, 1, 0, i * sliceWidth, 0, sliceWidth,
                    next.getHeight(), false, true);
        }
        batch.end();
    }


}
