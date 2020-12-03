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
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

import static com.ggl.salta.GameScreen.contMonedas;

public class BonusScreen implements Screen {

    SpriteBatch batchHUD;
    private BitmapFont fontContMonedas;
    Texture textureMoneda;

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

    // Font
    public static BitmapFont font;

    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/LemonMilk.otf"));
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

    int cont = 0;

    private void generatePelotas() {

        int cont = Aplication.db.getSelection();

        Sprite sprite = new Sprite(pelotas.get(cont));
        if (cont == 2 || cont == 4)
            sprite.setSize(sprite.getWidth() / PPM , sprite.getHeight() /PPM);
        else
            sprite.setSize(sprite.getWidth()*2 / PPM , sprite.getHeight()*2 /PPM);

        pelotasSprites.add(sprite);

        pelotasBodies.add(createBody( 0,cont));
    }

    private Body createBody( float rotation, int i) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(0 / PPM, 0 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.restitution = 0.6f;
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

        body.setTransform(500/PPM,50 /PPM, rotation);

        return body;
    }

    Texture fondo;
    Rectangle rect;
    @Override
    public void show() {
        Box2D.init();

        batch = new SpriteBatch();
        batchHUD = new SpriteBatch();

        camera = new OrthographicCamera();

        camera.setToOrtho(false, Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM );
        camera.update();

        pelotas.add( new Texture("pelotas/pj1.png"));
        pelotas.add( new Texture("pelotas/pj2.png"));
        pelotas.add( new Texture("pelotas/pj3.png"));
        pelotas.add( new Texture("pelotas/pj4.png"));
        pelotas.add( new Texture("pelotas/pj5.png"));

        textureMoneda = new Texture(Gdx.files.internal("trofeo.png"),true);
        textureMoneda.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        int border = (int)(Gdx.graphics.getHeight() * 0.003f);
        int size = (int)(Gdx.graphics.getHeight() * 0.025f);

        parameter.size = size;
        parameter.color = Color.WHITE;
        parameter.borderWidth = border;
        fontContMonedas = generator.generateFont(parameter);
        // FONDO
        fondo = new Texture("fondos/fondoBonus.png");
        rect = new Rectangle(0,0 , Gdx.graphics.getWidth() / PPM, fondo.getHeight()/PPM );

        world = new World(new Vector2(0, -10), true);

        debugRenderer = new Box2DDebugRenderer();

        generatePelotas();

        // FONT
        int s = (int)(Gdx.graphics.getHeight() * 0.05f);

        parameter.size = s;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);


        Skin skin = new Skin(Gdx.files.internal("skins/flatearthui/flat-earth-ui.json"));

        textButtonStyle = skin.get("default", TextButton.TextButtonStyle.class);
        textButtonStyle.font = font;


        float btWidth = Gdx.graphics.getWidth()*0.7f;
        float btHeight = Gdx.graphics.getHeight()*0.10f;
        float btPad = Gdx.graphics.getWidth()*0.02f;


    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1/60f,6,2);

        debugRenderer.render(world,camera.combined);

        batch.begin();

        batch.draw(fondo,0,0,Gdx.graphics.getWidth() / PPM, fondo.getHeight()/PPM );

        for (int i = 0; i < pelotasBodies.size; i++) {

            Body body = pelotasBodies.get(i);

            Vector2 position = body.getPosition();

            float degrees = (float) Math.toDegrees(body.getAngle());

            drawSprite(pelotasSprites.get(i), position.x - pelotasSprites.get(i).getWidth()/2, position.y - pelotasSprites.get(i).getWidth()/2, degrees);
        }

        batch.end();

        float widthMoneda = Gdx.graphics.getWidth()*0.06f;

        batchHUD.begin();

        batchHUD.draw(textureMoneda, Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() - Gdx.graphics.getHeight()*0.06f, widthMoneda, widthMoneda);
        fontContMonedas.draw(batchHUD, "" + (cont + contMonedas), Gdx.graphics.getWidth() * 0.07f + widthMoneda,  Gdx.graphics.getHeight() - Gdx.graphics.getHeight()*0.035f );

        batchHUD.end();

        actualizar();
    }
    public void actualizar(){
        input();

        // SUELO
        if((pelotasBodies.get(0).getPosition().y-((pelotas.get(0).getWidth()/2) /PPM) <= 0.9F && pelotasBodies.get(0).getLinearVelocity().y < 0) )
            if(cont>0) {
                //Aplication.db.addTrofeos(contMonedas);
                contMonedas += cont;
                GameScreen.altura ++;
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
                dispose();
            }
    }

    public void input(){
        Vector2 vc;
        Circle cir;
        if(Gdx.input.justTouched()) {
            vc = new Vector2(getMousePosInGameWorld().x,getMousePosInGameWorld().y );

            if(rect.contains(vc)){
                cir = new Circle();
                cir.setPosition(pelotasBodies.get(0).getPosition());
                cir.setRadius((pelotas.get(0).getWidth()*1.4f)/ PPM);
                if (cir.contains(vc)){
                    cont ++;
                    pelotasBodies.get(0).setLinearVelocity(new Vector2(getDirection(vc).x*8, 15f ));
                }
            }
        }
    }
    public Vector2 getDirection(Vector2 position){
        Vector2 ballCentre = pelotasBodies.get(0).getPosition();

        Vector2 direction = ballCentre.sub(position);
        direction.nor();

        return direction;
    }
    public Vector3 getMousePosInGameWorld() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    private void drawSprite(Sprite sprite, float x, float y, float degrees) {
        sprite.setPosition(x, y);

        sprite.setOrigin( sprite.getWidth()/2,  sprite.getHeight()/2);

        sprite.setRotation(degrees);

        sprite.draw(batch);
    }

    @Override
    public void resize(int width, int height) {

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

        //Suelo
        ground = world.createBody(bodyDef);
        ground.createFixture(fixtureDef);

        ground.setTransform(0 / PPM, -1 / PPM, 0);

        // Techo
        techo = world.createBody(bodyDef);
        techo.createFixture(fixtureDef);

        techo.setTransform( 0 / PPM, camera.viewportHeight +1/PPM, 0);

        // Pared izq
        shape.setAsBox(1 / PPM, camera.viewportHeight);

        fixtureDef.shape = shape;
        paredIzq = world.createBody(bodyDef);
        paredIzq.createFixture(fixtureDef);

        paredIzq.setTransform(-1 / PPM, 0 / PPM, 0);

        // Pared der
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

        world.dispose();

        debugRenderer.dispose();
    }
}
