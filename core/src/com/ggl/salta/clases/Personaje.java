package com.ggl.salta.clases;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.applet.Applet;

import static com.ggl.salta.GameScreen.PPM;
import static com.ggl.salta.PreferencesScreen.prefs;
import static com.ggl.salta.SplashScreen.botar;

public class Personaje extends Sprite {

    public Vector2 posicion;
    public Rectangle rect;

    // Box2d
    public World world;
    public Body b2body;

    public Personaje(Texture texture, Vector2 posicion, World world) {
        super(texture);
        super.setRegion(new TextureRegion(getTexture(),0,0,getTexture().getWidth(),getTexture().getHeight()));
        setBounds(0,0,getTexture().getWidth()/2 / PPM,getTexture().getHeight()/2 / PPM);
        this.posicion = posicion;

        this.world = world;

        rect = new Rectangle(posicion.x,posicion.y,(super.getTexture().getWidth())/2 / PPM, (super.getTexture().getHeight())/2 / PPM);
        definePersonaje();
    }


    public void definePersonaje(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(posicion.x / PPM, posicion.y / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((rect.getWidth()/2) , (rect.getHeight()/2) );

        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void saltar(){
        if(prefs.getBoolean("sound"))
            botar.play(1);


        b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, 8.5f ));
    }

    public void cogerPorwerups(int tipo){
        if(tipo == 1) {
            b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, 8.5f * 2 ));
        }

    }
}
