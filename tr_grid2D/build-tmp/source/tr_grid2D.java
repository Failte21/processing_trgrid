import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.svg.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class tr_grid2D extends PApplet {



char 	tileCount;
float 	tileWidth;
float[] x_array, y_array;
float[] prev_x3, prev_x4, prev_y3, prev_y4;
float 	randomRangeX, randomRangeY, randomRangeG;
char 	actRandomSeed;
char 	line_nb, dot_nb;
char 	randomNb;
int 	bg_color, stroke_color;
boolean doSave;

public void setup() {
	
	tileCount = 15;
	tileWidth = (width - 100) / tileCount;
	x_array = new float[5];
	y_array = new float[5];
	prev_x3 = new float[tileCount];
	prev_y3 = new float[tileCount];
	prev_x4 = new float[tileCount];
	prev_y4 = new float[tileCount];
	line_nb = 15;
	dot_nb = 15;
	randomRangeG = 100;
	actRandomSeed = 0;
	bg_color = color(255, 255, 255);
	stroke_color = color(0, 0, 0);
	doSave = false;
	
}

public void draw() {
	if (doSave){
		beginRecord(SVG, "export/" + str(year()) + str(month()) + str(day())
		 + str(hour()) + str(minute()) + str(second()) + ".svg");
	}
	translate(50, 50);
	noFill();	
	background(bg_color);
	stroke(stroke_color);
	randomSeed(actRandomSeed);
	//strokeWeight(2);
	strokeWeight(1);
	randomRangeX = map(mouseX, 0, width, 0, randomRangeG);
	randomRangeY = map(mouseY, 0, height, 0, randomRangeG);
	for (int i = 0; i < tileCount; i++){
		prev_x3[i] = tileWidth * i + random(-randomRangeX, randomRangeY);
		prev_x4[i] = tileWidth + tileWidth * i + random(-randomRangeX, randomRangeY);
		prev_y3[i] = 0;
		prev_x3[i] = 0;
	}
	for (int y = 0; y < 5; y++){ 
		x_array[4] = random(-randomRangeY, randomRangeY); 
		y_array[4] = tileWidth * y + tileWidth + random(-randomRangeY, randomRangeY);
		x_array[1] = random(-randomRangeX, randomRangeX);
		y_array[1] = random(-randomRangeY, randomRangeY);
		for (int x = 0; x < 5; x++){
			fillArray(x, y);
			drawTriangle();
		}
	}
	if (doSave){
		doSave = false;
		endRecord();
		print("saved");
	}
}

public void fillArray(int x, int y){
	if (y > 0){
		x_array[0] = prev_x3[x]; 
		x_array[1] = prev_x4[x]; 
		y_array[0] = prev_y3[x]; 
		y_array[1] = prev_y4[x];	
	}else{
		x_array[0] = x_array[1];
		y_array[0] = y_array[1];
		x_array[1] = tileWidth + tileWidth * x + random(-randomRangeY, randomRangeY);
		y_array[1] = tileWidth * y + random(-randomRangeY, randomRangeY);
	}
	x_array[2] = tileWidth * x + tileWidth / 2 + random(-randomRangeX, randomRangeX); 
	y_array[2] = tileWidth * y + tileWidth / 2 + random(-randomRangeY, randomRangeY);
	x_array[3] = x_array[4];
	y_array[3] = y_array[4];
	y_array[4] = tileWidth + tileWidth * y + random(-randomRangeY, randomRangeY);
	x_array[4] = tileWidth + tileWidth * x + random(-randomRangeY, randomRangeY);
	prev_x3[x] = x_array[3];
	prev_x4[x] = x_array[4];
	prev_y3[x] = y_array[3];
	prev_y4[x] = y_array[4];
}

public void drawTriangle(){
	// randomNb = int(random(10, 20));
	// line_nb = randomNb;
	// dot_nb = randomNb;
	// line_nb = 25;
	// dot_nb = 25;
	// top triangle
	// triangle(x_array[0], y_array[0], x_array[2], y_array[2], x_array[1], y_array[1]);
	// // left triangle
	// triangle(x_array[0], y_array[0], x_array[2], y_array[2], x_array[3], y_array[3]);
	// // right triangle
	// triangle(x_array[1], y_array[1], x_array[2], y_array[2], x_array[4], y_array[4]);
	// // bottom triangle
	// triangle(x_array[3], y_array[3], x_array[2], y_array[2], x_array[4], y_array[4]);
	draw_lines(x_array[0], y_array[0], x_array[1], y_array[1], x_array[2], y_array[2]);
	draw_lines(x_array[0], y_array[0], x_array[3], y_array[3], x_array[2], y_array[2]);
	draw_lines(x_array[1], y_array[1], x_array[4], y_array[4], x_array[2], y_array[2]);
	draw_lines(x_array[3], y_array[3], x_array[4], y_array[4], x_array[2], y_array[2]);
}

public void draw_lines(float x1, float y1, float x2, float y2, float x3, float y3){
	float[] x_gap, y_gap;

	x_gap = new float[2];
	y_gap = new float[2];
	x_gap[0] = (x3 - x1) / line_nb;
	x_gap[1] = (x3 - x2) / line_nb;
	y_gap[0] = (y3 - y1) / line_nb;
	y_gap[1] = (y3 - y2) / line_nb;
	for (int i = 0; i < line_nb; i++){
		// line(x1 + x_gap[0] * i, y1 + y_gap[0] * i, x2 + x_gap[1] * i, y2 + y_gap[1] * i);
		draw_dots(x1 + x_gap[0] * i, y1 + y_gap[0] * i, x2 + x_gap[1] * i, y2 + y_gap[1] * i);
	}
}

public void draw_dots(float x1, float y1, float x2, float y2){
	float dot_gap_x;
	float dot_gap_y;

	dot_gap_x = (x2 - x1) / dot_nb;
	dot_gap_y = (y2 - y1) / dot_nb;
	for (int i = 0; i < dot_nb; i++)
		point(x1 + dot_gap_x * i, y1 + dot_gap_y * i);
}

public void mouseClicked(){
	actRandomSeed++;
}

public void keyReleased(){
	if (key == '1'){
		stroke_color = color(0, 0, 0);
		bg_color = color(255, 255, 255);
	}else if (key == '2'){
		stroke_color = color(255, 255, 255);
		bg_color = color(0, 0, 0);
	}else if (key == '3'){
		stroke_color = color(255, 0, 0);
		bg_color = color(0, 0, 255);
	}else if (key == '4'){
		stroke_color = color(255, 0, 0);
		bg_color = color(0, 255, 0);
	}else if (key == '5'){
		stroke_color = color(0, 255, 0);
		bg_color = color(255, 0, 0);
	}else if (key == '6'){
		stroke_color = color(0, 255, 0);
		bg_color = color(0, 0, 255);
	}else if (key == '7'){
		stroke_color = color(0, 0, 255);
		bg_color = color(255, 0, 0);
	}else if (key == '8'){
		stroke_color = color(0, 0, 255);
		bg_color = color(0, 255, 0);
	}else if(key == 's'){
		doSave = true;
	}
}
  public void settings() { 	size(1000, 1000); 	smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "tr_grid2D" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
