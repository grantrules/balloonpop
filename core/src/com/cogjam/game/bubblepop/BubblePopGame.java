package com.cogjam.game.bubblepop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

import com.cogjam.game.bubblepop.utils.GameConstant;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/*

load screen: title, "start", company
on start, delete balloons. 


http://web.archive.org/web/20121218213317/http://devmaster.net/posts/3100/shader-effects-glow-and-bloom

*/

public class BubblePopGame extends ApplicationAdapter {
    SpriteBatch batch;
    BitmapFont font;

    String vertexShader;
    String fragmentShader;
    ShaderProgram shaderProgram;

    private OrthographicCamera camera;

    private long lastBalloonTime;
    private long lastLevelTime;
    private long gameOverTime;
    private boolean gameover = false;


    private Sound pop;

    private boolean mainmenuActive = true;
    private boolean levelActive = false;

   private void spawnBalloon() {


        Sprite b2 = new Sprite(bFactory.getRandomTextureRegion());

       BalloonActor bal = bFactory.acquireBalloon(b2);


       Integer x = MathUtils.random(1,100-GameConstant.BALLOON_WIDTH);

        b2.setBounds(x, -10, GameConstant.BALLOON_WIDTH, GameConstant.BALLOON_WIDTH * bFactory.getBalloonRatio());
        b2.setPosition(x, -1 * GameConstant.BALLOON_WIDTH * bFactory.getBalloonRatio());

        balloons_created++;
        lastBalloonTime = TimeUtils.nanoTime();
    }

    private int level = 1;
    private int balloons_created = 0;
    private int popped = 0;
    private int escaped = 0;
    //private Array<Sprite> sprites;
    //private TextureRegion[]     regions = new TextureRegion[5];
    private Texture mainmenuTex;
    private Sprite mainmenuSprite;
    private Texture gameoverTex;
    private Sprite gameoverSprite;

    private BalloonActorFactory bFactory;


    @Override
    public void create() {

        batch = new SpriteBatch();

        mainmenuTex = new Texture("start.png");
        mainmenuSprite = new Sprite(mainmenuTex);
        mainmenuSprite.setBounds(25,25,50,25);

        font = new BitmapFont(false); // fcuk fonts
        font.setColor(Color.RED);

        gameoverTex = new Texture("gameover.png");
        gameoverSprite = new Sprite(gameoverTex);
        gameoverSprite.setBounds(0,80,100,100);

        //vertexShader = Gdx.files.internal("vertexShader.glsl").readString();
        //fragmentShader = Gdx.files.internal("fragmentShader.glsl").readString();
        //shaderProgram = new ShaderProgram(vertexShader,fragmentShader);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(100, 100 * h / w);
        camera.position.set(50f, 50f * h / w,0);
        camera.update();

        bFactory = new BalloonActorFactory();

        //balloons = new Array<Ellipse>();
        //sprites = new Array<Sprite>();
        pop = Gdx.audio.newSound(Gdx.files.internal("pop.mp3"));

    }

    private boolean gameActive() {
        return !mainmenuActive && !levelActive;
    }



    @Override
    public void render() {
        Gdx.gl.glClearColor(0.5f, 0.5f, 1, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // shader. set to null if we want no shader. two batches if we want some thing shaded
        //batch.setShader(shaderProgram);

        if (TimeUtils.nanoTime() - lastBalloonTime > 1000000000 && (balloons_created < level * 10 || gameover || mainmenuActive)) {
            spawnBalloon();
        }

        //Iterator<Ellipse> it = balloons.iterator();
        Iterator<BalloonActor> it = bFactory.getActiveBalloons().iterator();

        while (it.hasNext()) {
            BalloonActor bal = it.next();
            if (!mainmenuActive && !levelActive && Gdx.input.isTouched() && bal.isInBounds(transformX(Gdx.input.getX()), transformY(Gdx.input.getY()))) {
            //popped!
                pop.play(1.0f);
                popped++;
                it.remove();


            } else {
                // move it up *** WAS bal.y not bal.getY() when using Ellipse
                bal.setY(bal.getY() + 5 * (level / 2 + 1) * Gdx.graphics.getDeltaTime());
                if (bal.getY() > camera.viewportHeight) {
                    it.remove();
                    escaped++;
                    if (!mainmenuActive && !levelActive && !gameover && escaped > GameConstant.MAX_ESCAPED_BALLOONS) {
                        // GAME OVER MAN, GAME OVER
                        gameOverTime = TimeUtils.nanoTime();
                        gameover = true;
                    }
                }
            }
            if (!mainmenuActive && !levelActive && !gameover && bFactory.getActiveBalloons().size == 0 && balloons_created == (level * 10)) {

                    newlevel();

            }



        }

        for (BalloonActor bal : bFactory.getActiveBalloons()) {
            bal.draw(batch, 1);
        }

        if (levelActive) {
            if ((TimeUtils.nanoTime() - lastLevelTime) / 1000000000 > 5) {
                levelActive = false;
            }
            font.draw(batch, "Level " + level,25, camera.viewportHeight / 2);
        }

        if (mainmenuActive) {
            if (Gdx.input.isTouched() && mainmenuSprite.getBoundingRectangle().contains(transformX(Gdx.input.getX()), transformY(Gdx.input.getY()))) {
                mainmenuActive = false;
                level = 0;
                newlevel();
            }
            mainmenuSprite.draw(batch);
        }

        if (gameover) {
            if ((TimeUtils.nanoTime() - gameOverTime) / 1000000000 > 5) {
                gameover = false;
                mainmenuActive = true;
            }

            gameoverSprite.setBounds(20,100,100,100);
            gameoverSprite.draw(batch);



        }

        //font.draw(batch, "Start!", 5, 5);

        batch.end();
    }

    private void newlevel() {
        level++;
        balloons_created = 0;
        popped = 0;
        escaped = 0;
        bFactory.clear();
        lastLevelTime = TimeUtils.nanoTime();
        levelActive = true;
    }

    private float transformX(float f) {
        return 100 * f / Gdx.graphics.getWidth();
    }

    private float transformY(float f) {
        return camera.viewportHeight - (camera.viewportHeight * f / Gdx.graphics.getHeight());
    }
}
