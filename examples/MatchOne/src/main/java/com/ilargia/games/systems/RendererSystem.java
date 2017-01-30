package com.ilargia.games.systems;


public class RendererSystem /*implements IExecuteSystem, ISetPool<Pool>*/ {

    public void execute() {

    }
//    private final BitmapFont font;
//    private Group<Entity> _group;
//    private ShapeRenderer sr;
//    private OrthographicCamera cam;
//    private Group<Entity> _groupScore;
//    private Batch batch;
//
//    public RendererSystem(ShapeRenderer sr, OrthographicCamera cam, Batch batch, BitmapFont font) {
//        this.sr = sr;
//        this.cam = cam;
//        this.batch = batch;
//        this.font =  font;
//    }
//
//    @Override
//    public void setPool(Pool pool) {
//        _group = pool.getGroup(Matcher.AllOf(CoreMatcher.View()));
//        _groupScore = pool.getGroup(Matcher.AllOf(CoreMatcher.Score()));
//    }
//
//    @Override
//    public void execute() {
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        cam.update();
//
//        sr.setProjectionMatrix(cam.combined);
//        sr.begin(ShapeRenderer.ShapeType.Filled);
//        sr.setColor(Color.WHITE);
//
//        for (Entity e : _group.getEntities()) {
//            View view = e.getView();
//
//            if(view.shape instanceof Rectangle) {
//                Rectangle ret = (Rectangle) view.shape;
//                sr.rect(ret.x, ret.y, ret.width, ret.height);
//            } else {
//                Circle circle = (Circle) view.shape;
//                sr.circle(circle.x, circle.y, circle.radius);
//            }
//
//        }
//
//        sr.end();
//
//        batch.begin();
//        for (Entity e : _groupScore.getEntities()) {
//            Score score = e.getScore();
//            font.draw(batch, score.text+ " "+ score.points, score.x, score.y);
//        }
//        batch.end();
//    }

}
