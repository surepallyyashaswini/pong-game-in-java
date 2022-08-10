Ball.java
package pongGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;


public class Ball implements Runnable {

	//global variables
	int x, y, xDirection, yDirection;
	
	
	int p1score, p2score;
	
	Paddle p1 = new Paddle(10, 25, 1);
	Paddle p2 = new Paddle(485, 25, 2);
	
	Rectangle ball;

	
	public Ball(int x, int y){
		p1score = p2score = 0;
		this.x = x;
		this.y = y;
		
		//Set ball moving randomly
		Random r = new Random();
		int rXDir = r.nextInt(1);
		if (rXDir == 0)
			rXDir--;
		setXDirection(rXDir);
		
		int rYDir = r.nextInt(1);
		if (rYDir == 0)
			rYDir--;
		setYDirection(rYDir);
		
		//create "ball"
		ball = new Rectangle(this.x, this.y, 15, 15);
	}
	
	public void setXDirection(int xDir){
		xDirection = xDir;
	}
	public void setYDirection(int yDir){
		yDirection = yDir;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(ball.x, ball.y, ball.width, ball.height);
	}
	
	public void collision(){
        if(ball.intersects(p1.paddle))
            setXDirection(+1);
        if(ball.intersects(p2.paddle))
            setXDirection(-1);
	}	
	public void move() {
		collision();
		ball.x += xDirection;
		ball.y += yDirection;
		//bounce the ball when it hits the edge of the screen
		if (ball.x <= 0) {
			setXDirection(+1);
			p2score++;
			
	}
		if (ball.x >= 485) {
			setXDirection(-1);
			p1score++;
		}
		
		if (ball.y <= 15) {
			setYDirection(+1);
		}
		
		if (ball.y >= 385) {
			setYDirection(-1);
		}
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				move();
				Thread.sleep(8);
			}
		}catch(Exception e) { System.err.println(e.getMessage()); }

	}

}
Paddle.java
package pongGame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;


public class Paddle implements Runnable{
	
	int x, y, yDirection, id;
	
	Rectangle paddle;
	
	public Paddle(int x, int y, int id){
		this.x = x;
		this.y = y;
		this.id = id;
		paddle = new Rectangle(x, y, 10, 50);
		}
		
	public void keyPressed(KeyEvent e) {
		switch(id) {
		default:
			System.out.println("Please enter a Valid ID in paddle contructor");
			break;
		case 1:
			if(e.getKeyCode() == KeyEvent.VK_W) {
				setYDirection(-1);
			if(e.getKeyCode() == KeyEvent.VK_S) {
				setYDirection(1);
			}
			break;
			}
		case 2:
			if(e.getKeyCode() == KeyEvent.VK_UP) {
				setYDirection(-1);
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				setYDirection(1);
			}
			break;
	}
	}
	
	public void keyReleased(KeyEvent e) {
		switch(id) {
		default:
			System.out.println("Please enter a Valid ID in paddle contructor");
			break;
		case 1:
		if(e.getKeyCode() == e.VK_UP) {
			setYDirection(0);
		}
		if(e.getKeyCode() == e.VK_DOWN) {
			setYDirection(0);
		}
		break;
		case 2:
			
		if(e.getKeyCode() == e.VK_W) {
			setYDirection(0);
		}
		if(e.getKeyCode() == e.VK_S) {
			setYDirection(0);
		}
		break;
	}
	}
	public void setYDirection(int yDir) {
		yDirection = yDir;
	}
	
	public void move() {
	 	paddle.y += yDirection;
	 	if (paddle.y <= 15)
	 		paddle.y = 15;
	 	if (paddle.y >= 340);
	 		paddle.y = 340;
	}
	public void draw(Graphics g) {
		switch(id) {
		default:
			System.out.println("Please enter a Valid ID in paddle contructor");
			break;
		case 1:
			g.setColor(Color.CYAN);
			g.fillRect(paddle.x, paddle.y, paddle.width, paddle.height);
			break;
		case 2:
			g.setColor(Color.pink);
			g.fillRect(paddle.x, paddle.y, paddle.width, paddle.height);
			break;
		}
	}
	@Override
	public void run() {
		try {
			while(true) {
				move();
				Thread.sleep(7);
			}
		} catch(Exception e) { System.err.println(e.getMessage()); }
	}


	

}
Pong.java
package pongGame;


import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;


public class Pong extends JFrame {
	
	//screen size variables.
	int gWidth = 500;
	int gHeight = 400;
	Dimension screenSize = new Dimension(gWidth, gHeight);
	
	Image dbImage;
	Graphics dbGraphics;
	
	//ball object
	static Ball b = new Ball(250, 200);
	
	
	//constructor for window
	public Pong() {
		this.setTitle("Pong!");
		this.setSize(screenSize);
		this.setResizable(false);
		this.setVisible(true);
		this.setBackground(Color.DARK_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(new AL());
	}
	
	public static void main(String[] args) {
		Pong pg = new Pong();
		
		//create and start threads.
		Thread ball = new Thread(b);
		ball.start();
		Thread p1 = new Thread(b.p1);
		Thread p2 = new Thread(b.p2);
		p2.start();
		p1.start();
		
	}
	
	@Override
	public void paint(Graphics g) {
		dbImage = createImage(getWidth(), getHeight());
		dbGraphics = dbImage.getGraphics();
		draw(dbGraphics);
		g.drawImage(dbImage, 0, 0, this);
	}
	
	public void draw(Graphics g) {
		b.draw(g);
		b.p1.draw(g);
		b.p2.draw(g);
		
		g.setColor(Color.WHITE);
		g.drawString(""+b.p1score, 15, 20);
		g.drawString(""+b.p2score, 385, 20);
		
		repaint();
	}
	
	public class AL extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			b.p1.keyPressed(e);
			b.p2.keyPressed(e);
		}
		@Override
		public void keyReleased(KeyEvent e) {
			b.p1.keyReleased(e);
			b.p2.keyReleased(e);
		}
		
	}
}
