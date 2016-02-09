/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cogjam.game.bubblepop.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author grant
 */
public class GameConstant {
    
    public static int SPEED_BASE = 1;
    public static float SPEED_MULTIPLIER = 1.0f;
    public static int MAX_ESCAPED_BALLOONS = 5;
    public static int BALLOON_WIDTH = 15;
    public static Sound pop = Gdx.audio.newSound(Gdx.files.internal("pop.mp3"));

    
}
