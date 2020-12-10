package com.ggl.salta;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;

import static com.ggl.salta.PreferencesScreen.prefs;
import static com.ggl.salta.SplashScreen.music;
import static java.lang.Math.floor;
import static java.lang.Math.round;

public class MainScreen implements Screen {
    private Stage stage;
    private Aplication game;

    public static float PPM = 100;

    Array<Texture> pelotas = new Array<>();

    SpriteBatch batch;

    OrthographicCamera camera;

    World world;

    Box2DDebugRenderer debugRenderer;

    Body ground;
    Body paredIzq;
    Body paredDer;
    Body techo;

    Array<Body> pelotasBodies = new Array<>();

    Array<Sprite> pelotasSprites = new Array<>();

    TextButton.TextButtonStyle textButtonStyle;

    // font
    public static BitmapFont font;
    public static BitmapFont font2;

    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/LemonMilk.otf"));
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

    public MainScreen(Aplication game) {
        this.game = game;
    }

    private void generatePelotas() {
        for (int i = 0; i < pelotas.size; i++) {

            Sprite sprite = new Sprite(pelotas.get(i));
            if (i == 2 || i == 4)
                sprite.setSize(sprite.getWidth() / PPM , sprite.getHeight() /PPM);
            else
                sprite.setSize(sprite.getWidth()*2 / PPM , sprite.getHeight()*2 /PPM);

            pelotasSprites.add(sprite);

            pelotasBodies.add(createBody( 0,i));
        }
    }

    private Body createBody( float rotation, int i) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(0 / PPM, 0 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.restitution = 1;
        fdef.density = 0.1f;

        CircleShape shape = new CircleShape();
        if (i == 2 || i == 4)
            shape.setRadius((pelotas.get(0).getWidth()/2) / PPM);
        else
            shape.setRadius((pelotas.get(0).getWidth()) / PPM);

        fdef.shape = shape;

        body.setSleepingAllowed(false);
        body.setAwake(true);
        body.setActive(true);


        body.createFixture(fdef).setUserData(this);

        body.setTransform(50/PPM,50 /PPM, rotation);

        return body;
    }
    @Override
    public void show() {
        Box2D.init();

        if(prefs.getBoolean("music") && !music.isPlaying()) {
            music.play();
            music.setVolume(prefs.getFloat("vol"));
            music.setLooping(true);
        }

        batch = new SpriteBatch();

        camera = new OrthographicCamera();

        camera.setToOrtho(false, Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM );
        camera.update();

        pelotas.add( new Texture("pelotas/pj1.png"));
        pelotas.add( new Texture("pelotas/pj2.png"));
        pelotas.add( new Texture("pelotas/pj3.png"));
        pelotas.add( new Texture("pelotas/pj4.png"));
        pelotas.add( new Texture("pelotas/pj5.png"));

        world = new World(new Vector2(0, -10), true);

        debugRenderer = new Box2DDebugRenderer();

        generatePelotas();


        // FONT
        int size = (int)(Gdx.graphics.getHeight() * 0.05f);

        parameter.size = size;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);

        size = (int)(Gdx.graphics.getHeight() * 0.035f);
        parameter.size = size;
        font2 = generator.generateFont(parameter);

        Skin skin = new Skin(Gdx.files.internal("skins/flatearthui/flat-earth-ui.json"));

        textButtonStyle = skin.get("default", TextButton.TextButtonStyle.class);
        textButtonStyle.font = font;


        float btWidth = Gdx.graphics.getWidth()*0.7f;
        float btHeight = Gdx.graphics.getHeight()*0.10f;
        float btPad = Gdx.graphics.getWidth()*0.02f;

        if (!VisUI.isLoaded())
            VisUI.load(VisUI.SkinScale.X2);

        stage = new Stage();

        VisImage image = new VisImage(new Texture("trofeo_salta.png"));
        stage.addActor(image);

        VisTable table = new VisTable(false);
        table.setFillParent(true);
        stage.addActor(table);

        TextButton playButton = new TextButton("JUGAR",textButtonStyle);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ChoosePjScreen());
                dispose();
            }
        });

        TextButton settings = new TextButton("AJUSTES",textButtonStyle);
        settings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new PreferencesScreen(game));
                dispose();
                // Salir del juego
            }
        });

        // Añade filas a la tabla y añade los componentes
        table.row();
        table.add(image).center().width(btWidth).height(btWidth).padBottom(btPad*6);
        table.row();
        table.add(playButton).center().width(btWidth).height(btHeight).pad(btPad);
        table.row();
        table.add(settings).center().width(btWidth).height(btHeight).pad(btPad);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1/60f,6,2);

        debugRenderer.render(world,camera.combined);

        batch.begin();

        for (int i = 0; i < pelotasBodies.size; i++) {

            Body body = pelotasBodies.get(i);

            Vector2 position = body.getPosition();

            float degrees = (float) Math.toDegrees(body.getAngle());

            drawSprite(pelotasSprites.get(i), position.x - pelotasSprites.get(i).getWidth()/2, position.y - pelotasSprites.get(i).getWidth()/2, degrees);
        }

        batch.end();


        stage.act(dt);
        stage.draw();


        // Gravedad
        int vg=round( Gdx.input.getAccelerometerX());
        int vh=round( Gdx.input.getAccelerometerY());
        System.out.println(vg);
        world.setGravity(new Vector2(-vg,-vh));

    }
    private void drawSprite(Sprite sprite, float x, float y, float degrees) {
        sprite.setPosition(x, y);

        sprite.setOrigin( sprite.getWidth()/2,  sprite.getHeight()/2);

        sprite.setRotation(degrees);

        sprite.draw(batch);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);

        batch.setProjectionMatrix(camera.combined);

        createGround();
    }

    private void createGround() {
        if (ground != null) world.destroyBody(ground);

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1;

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(camera.viewportWidth, 1 / PPM);

        fixtureDef.shape = shape;

        ground = world.createBody(bodyDef);
        ground.createFixture(fixtureDef);

        ground.setTransform(0 / PPM, -1 / PPM, 0);

        // techo

        techo = world.createBody(bodyDef);
        techo.createFixture(fixtureDef);

        techo.setTransform( 0 / PPM, camera.viewportHeight +1/PPM, 0);

        // pared izq

        shape.setAsBox(1 / PPM, camera.viewportHeight);

        fixtureDef.shape = shape;
        paredIzq = world.createBody(bodyDef);
        paredIzq.createFixture(fixtureDef);

        paredIzq.setTransform(-1 / PPM, 0 / PPM, 0);

        // pared der

        fixtureDef.shape = shape;
        paredDer = world.createBody(bodyDef);
        paredDer.createFixture(fixtureDef);

        paredDer.setTransform(camera.viewportWidth , 0 / PPM, 0);

        shape.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();

        world.dispose();

        debugRenderer.dispose();
    }
}