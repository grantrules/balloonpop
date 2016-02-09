package com.cogjam.game.bubblepop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * Created by REGISTER2 on 2/8/2016.
 */
public class BalloonActorFactory {

    // shit ain't finished, don't make this true
    private static boolean POOLING = false;

    private Array<BalloonActor> inactiveBalloons;
    private Array<BalloonActor> activeBalloons;
    private Texture img;
    private TextureRegion[]     regions = new TextureRegion[5];

    public float getBalloonRatio() {
        return balloonRatio;
    }

    private float balloonRatio;




    public BalloonActorFactory() {
        inactiveBalloons = new Array<BalloonActor>();
        activeBalloons = new Array<BalloonActor>();

        img = new Texture("balloons.png");


        for (int i = 0;i < 5; i++){
            regions[i] = new TextureRegion(img, img.getWidth() / 5 * i, 0, img.getWidth() /5, img.getHeight());
        }

        balloonRatio = regions[0].getRegionHeight() / regions[0].getRegionWidth();
    }

    public Array<BalloonActor> getActiveBalloons() {
        return activeBalloons;
    }

    public BalloonActor acquireBalloon() {
        return acquireBalloon(null);
    }

    public BalloonActor acquireBalloon(Sprite sprite) {
        BalloonActor bal;
        if (inactiveBalloons.size == 0 || !POOLING) {
            bal = new BalloonActor();
        } else {
            bal = inactiveBalloons.pop();
        }
        activeBalloons.add(bal);
        return bal;

    }

    public void deactivateBalloon(BalloonActor bal) {

        activeBalloons.removeValue(bal, true);
        if (POOLING) {
            inactiveBalloons.add(bal);
        }

    }

    public void destroyBalloon(BalloonActor bal) {
        activeBalloons.removeValue(bal, true);
        if (POOLING) {
            inactiveBalloons.removeValue(bal, true);
        }
    }

    public TextureRegion getRandomTextureRegion(){
        return regions[MathUtils.random(0, regions.length - 1)];
    }

    public void clear() {
        activeBalloons.clear();
        if (POOLING) {
            inactiveBalloons.clear();
        }
    }

}
