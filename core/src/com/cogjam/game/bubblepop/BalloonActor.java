/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cogjam.game.bubblepop;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cogjam.game.bubblepop.utils.GameConstant;

/**
 *
 * @author grant
 */
public class BalloonActor extends Actor {
    
    private Ellipse bounding;
    private Sprite sprite;
    
    public BalloonActor() {
        sprite = new Sprite();
    }

    public BalloonActor(Sprite s) {
        sprite = s;
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        
    }
    
    @Override
    public void act(float delta) {
        
    }

    public void pop() {
        GameConstant.pop.play();
    }

    public void update() {

    }

    // coordinates must be transformed
    public boolean isInBounds(float x, float y) {
        return sprite.getBoundingRectangle().contains(x, y);
    }
    
}
