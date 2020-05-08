package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.DB.DBConnection;

import java.util.Random;

public class Hoverfly extends ApplicationAdapter {
	SpriteBatch batch;
	Texture backGround;
	Circle birdCircle;
	Texture gameOver;
//	ShapeRenderer shapeRenderer;

	Texture[] bird;
	int flapState = 0;
	float birdY = 0;
	BitmapFont font;
	float velocity = 0;
	int score = 0;
	int scoringTube = 0;
	int gameState = 0;
	float gravity = 2;
	Texture topTube;
	Texture bottomTube;
	//multiplied by 1.5 --> 400 to 600
	float gap = 600;
	float maxTubeOffset;
	Random random;
	float tubeVelocity = 9 ;
	int numOfTubes = 4;
	float [] tubeX = new float[numOfTubes];
	float [] tubeOffset = new float[numOfTubes];
	float distanceBetweenTubes;
	Rectangle [] topTubeRectangles;
	Rectangle [] bottomTubeRectangles;



	@Override
	public void create() {

		batch = new SpriteBatch();
//		shapeRenderer = new ShapeRenderer();
		backGround = new Texture("bg2.png");
		gameOver = new Texture("gameover.png");
		birdCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.RED);
		font.getData().setScale(13);
		bird = new Texture[2];
		bird[0] = new Texture("bee2.png");
		bird[1] = new Texture("bee2.png");


		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		//multiplied by 1.5 --> 100 to 150

		maxTubeOffset = Gdx.graphics.getHeight() / 2 -gap /2 -150;
		random = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 5/8;

		topTubeRectangles = new Rectangle[numOfTubes];

		bottomTubeRectangles = new Rectangle[numOfTubes];
		DBConnection dbConnection = new DBConnection();
		dbConnection.startConnection();
		startGame();


	}


	public void startGame(){

		birdY = Gdx.graphics.getHeight() / 2 - bird[0].getHeight()/2;

		for(int i = 0; i <numOfTubes; i++){
			//multiplied by 1.5 --> 200 to 300
			tubeOffset [i]= (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap  - 300);

			tubeX [i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth()/2 + Gdx.graphics.getWidth()+ i * distanceBetweenTubes;

			topTubeRectangles [i] = new Rectangle();
			bottomTubeRectangles [i] = new Rectangle();
		}

	}
	@Override
	public void render() {
		batch.begin();
		batch.draw(backGround, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gameState == 1){

			if (tubeX[scoringTube]< Gdx.graphics.getWidth()/2){
				score++;

				Gdx.app.log("Scoring", String.valueOf(score));

				if (scoringTube < numOfTubes-1){
					scoringTube++;
				} else {
					scoringTube = 0;
				}
			}

			if (Gdx.input.justTouched()) {

//				Gdx.app.log("Touched", "Yep!");


				///////////////////////
//				if (flapState == 0) {
//					flapState = 1;
//
//
//				} else {
//					flapState = 0;
//				}
				//////////////////////

			velocity = -20;



			}

			for(int i = 0; i < numOfTubes; i++) {

				if(tubeX [i]< -topTube.getWidth()){
					tubeX [i ] +=numOfTubes * distanceBetweenTubes;
					tubeOffset [i]= (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap  - 300);

				} else{
					tubeX [i] = tubeX [i] - tubeVelocity;

				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset [i]);

				topTubeRectangles [i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles [i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset [i], bottomTube.getWidth(), bottomTube.getHeight());



			}


			if(birdY > 0 || velocity < 0) {
				velocity = velocity + gravity;

				birdY -= velocity;
			}



	} else if(gameState == 0) {

			if (Gdx.input.justTouched()) {
				Gdx.app.log("Touched", "Yep!");
				gameState = 1;
			}
		} else if(gameState == 2){

			batch.draw(gameOver, Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2, Gdx.graphics.getHeight()/2 - gameOver.getHeight()/2 );
			if (Gdx.input.justTouched()) {
				Gdx.app.log("Touched", "Yep!");
				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}
		}
		if (flapState == 0) {
			flapState = 1;

		} else {
			flapState = 0;
		}

		batch.draw(bird[flapState], Gdx.graphics.getWidth() / 2 - bird[flapState].getWidth()/2, birdY);
		font.draw(batch, String.valueOf(score), 100, 200);

		birdCircle.set(Gdx.graphics.getWidth()/2, birdY + bird[flapState].getHeight()/2,bird[flapState].getWidth()/2 );

//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.RED);
//		shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius );

		for(int i =0; i < numOfTubes; i ++){
//			shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
//			shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset [i], bottomTube.getWidth(), bottomTube.getHeight());

			if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])  ){

				Gdx.app.log("Collision", "Yes");
				gameState = 2;
			}
		}

//		shapeRenderer.end();
		batch.end();


	}
	

}
