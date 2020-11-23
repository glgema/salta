package com.ggl.salta.clases;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class Aspecto extends Sprite {

    public int precio;
    public boolean desbloqueado;

    public Aspecto(int precio, boolean desbloqueado, Texture texture) {
        super(texture);
        this.precio = precio;
        this.desbloqueado = desbloqueado;
    }


}
