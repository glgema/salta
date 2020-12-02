package com.ggl.salta;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.ggl.salta.clases.Database;

public class Aplication extends Game {

	public Boolean isMultiReady;
	public static Database db;
	public Aplication(Database db){
		this.db = db;
		db.onCreate();
	}

	@Override
	public void create () {
		super.setScreen(new SplashScreen(this));
		isMultiReady = false;
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
}
