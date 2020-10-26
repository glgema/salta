package com.ggl.salta.clases;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static com.ggl.salta.GameScreen.PPM;

public class Plataforma extends Sprite{

    public Rectangle rect;

    public Plataforma (Vector2 pos, Texture texture){
        super(texture);
        this.rect = new Rectangle(pos.x,pos.y,texture.getWidth() / PPM,texture.getHeight() / PPM);
        super.setRegion(new TextureRegion(getTexture(),0,0,getTexture().getWidth(),getTexture().getHeight()));
        setBounds(0,0,getTexture().getWidth() / PPM,getTexture().getHeight() / PPM);
        super.setPosition(pos.x,pos.y);
    }
}
