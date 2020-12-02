package com.ggl.salta.clases;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ggl.salta.Aplication;

import static com.ggl.salta.GameScreen.PPM;
import static com.ggl.salta.SplashScreen.powerUps;

public class Plataforma extends Sprite{

    public Rectangle rect;
    boolean seBorra;
    public Rectangle rectPowerups;
    Vector2 posPowerups;
    public int tipo;
    Sprite muelle;

    public Plataforma (Vector2 pos, Texture texture, boolean seBorra, int tipo){
        super(texture);
        this.rect = new Rectangle(pos.x, pos.y,texture.getWidth() / PPM,texture.getHeight() / PPM);
        super.setRegion(new TextureRegion(getTexture(),0,0,getTexture().getWidth(),getTexture().getHeight()));
        setBounds(0,0,getTexture().getWidth() / PPM,getTexture().getHeight() / PPM);
        super.setPosition(pos.x, pos.y);
        this.seBorra = seBorra;
        ponerPowerUps(tipo);
        this.tipo = tipo;

    }

    public boolean seBorra() {
        return seBorra;
    }

    public void setSeBorra(boolean seBorra) {
        this.seBorra = seBorra;
    }


    public void ponerPowerUps(int tipo){
        posPowerups = new Vector2(rect.x + rect.getWidth()/2 , rect.y + rect.getHeight());

        Texture textMuelle = powerUps.get(Aplication.db.getSelection());
        if(tipo==1) {
            muelle = new Sprite(textMuelle);
            rectPowerups = new Rectangle(posPowerups.x , posPowerups.y, textMuelle.getWidth() / PPM / 2, textMuelle.getHeight() / PPM / 2);
            muelle.setRegion(new TextureRegion(textMuelle,0,0,textMuelle.getWidth(),textMuelle.getHeight()));
            muelle.setBounds(0,0,textMuelle.getWidth() / PPM /2,textMuelle.getHeight() / PPM / 2);
            posPowerups.x = posPowerups.x - muelle.getWidth() / 2;

            muelle.setPosition(posPowerups.x, posPowerups.y);
        }
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        if(tipo == 1 )
            muelle.draw(batch);

    }
}
