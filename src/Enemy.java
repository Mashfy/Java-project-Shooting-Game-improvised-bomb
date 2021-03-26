//
//import java.awt.Graphics;
//import java.awt.Rectangle;
//import java.awt.image.BufferedImage;
//import java.util.Random;
//
//public class Enemy extends GameObject {
//
//	private Handler handler;
//	Random r=new Random();
//	int choose=0;
//	int hp=100;
//	Game game;
//	private BufferedImage[] enemy_image=new BufferedImage[3];
//	Animation anim;
//	
//	public Enemy(int x,int y,ID id,Handler handler,Game game, SpriteSheet ss) {
//		super(x,y,id, ss);
//		this.handler=handler;
//		this.game=game;
//		
//		enemy_image[0] = ss.grabImage(4, 1, 32, 32);
//		enemy_image[1] = ss.grabImage(5, 1, 32, 32);
//		enemy_image[2] = ss.grabImage(6, 1, 32, 32);
//		
//		anim = new Animation(3,enemy_image[0],enemy_image[1],enemy_image[2]);
//	}
//	
//	public void tick() {
//		x+=velX;
//		y+=velY;
//		
//		choose = r.nextInt(10);
//		
//		for(int i=0;i<handler.object.size();i++) {
//			GameObject tempObject= handler.object.get(i);
//			
//			if(tempObject.getId() ==ID.Block) {
//		    	if(getBoundsBig().intersects(tempObject.getBounds())) {
//		    		x+=(velX*5)* -1;
//		    		y+=(velY*5)* -1;
//		    		
//		    		velX*=-1;
//		    		velY*=-1;
//		}
//		    	else if(choose==0) {
//					velX=(r.nextInt(4 - -4) + -4);
//					velY=(r.nextInt(4 - -4) + -4);
//				}
//			}
//			
//			if(tempObject.getId() ==ID.Bullet) {
//				if(getBounds().intersects(tempObject.getBounds())) {
//		    	hp-=50;
//		    	handler.removeObject(tempObject);
//				}
//		}
//   	}
//		anim.runAnimation();
//		if(hp<=0) 
//			{
//			handler.removeObject(this);
//			game.Score++;
//			
//			}
//	
//		
//	}
//	
//
//	
//	public void render(Graphics g) {
//		anim.drawAnimation(g, x, y, 0);
//	}
//
//	public Rectangle getBounds() {
//		return new Rectangle(x,y,32,32);
//	}
//	public Rectangle getBoundsBig() {
//		return new Rectangle(x-16,y-16,64,64);
//	}
//
//}




import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class Enemy extends GameObject {

	private Handler handler;
	private BufferedImage enemy_image;
	private int persuitTimer;
	private int hp;
	private boolean isMoving;
	Game game;
	public Enemy(int x,int y,ID id,Handler handler,Game game, SpriteSheet ss) {
		super(x, y, id, ss);
		this.game=game;
		this.persuitTimer = 0;
		this.hp = 100;
		this.handler = handler;
		this.isMoving = false;
		this.enemy_image = ss.grabImage(4, 1, 32, 32);
		
	}

	@Override
	public void tick() {
		this.x += this.velX;
		this.y += this.velY;

		if (!isMoving) {
			this.persuitTimer++;
		}
		for (int i = 0; i < this.handler.object.size(); i++) {
			GameObject tempObject = this.handler.object.get(i);

			if (tempObject.getId() == ID.Block) {
				if (this.getBoundsBig().intersects(tempObject.getBounds())) {
					this.turnBack();
					isMoving = false;
				}

			}

			if (tempObject.getId() == ID.Bullet) {
				if (this.getBounds().intersects(tempObject.getBounds())) {
					this.hp -= 50;
					this.handler.removeObject(tempObject);
				}
			}

			if (tempObject.getId() == ID.Player) {
				if (this.getBounds().intersects(tempObject.getBounds())) {

				}
				if (this.isMoving) {
					this.persuitPlayer(tempObject);
				}
			}
		}

		if (this.hp <= 0) {
			this.handler.removeObject(this);
			game.Score++;
		}
		// end hp if
		if (this.persuitTimer >= 20) {
			this.isMoving = true;
			this.persuitTimer = 0;
		}
	}

	private void turnBack() {
		this.velX *= -1;
		this.velY *= -1;
	}

	private void persuitPlayer(GameObject player) {
		if(this.x - player.x  < 400 && this.y - player.y < 400) {
			if (this.x > player.getX()) {
				this.velX = -1;

			}
			if (this.x < player.getX()) {
				this.velX = 1;

			}
			if (this.y > player.getY()) {
				this.velY = -1;

			}
			if (this.y < player.getY()) {
				this.velY = 1;

			}
		} else {
			this.velX = 0;
			this.velY = 0;
		}
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(this.enemy_image, x, y, null);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, 32, 32);
	}

	public Rectangle getBoundsBig() {
		return new Rectangle(x - 16, y - 16, 64, 64);
	}
}


