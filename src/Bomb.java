import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Bomb extends GameObject{

	Handler handler;
	Random r=new Random();
	int choose= 0;
	private BufferedImage bomb_image;
	public Bomb(int x, int y, ID id,Handler handler, SpriteSheet ss) {
		super(x, y, id, ss);
		this.handler=handler;
		bomb_image = ss.grabImage(1, 1,28,32);
	}
	
	
	public void tick() {
	
		x+=velX;
		y+=velY;
		
		choose = r.nextInt(10);
		for(int i=0;i<handler.object.size();i++) {
			GameObject tempObject= handler.object.get(i);
			
			if(tempObject.getId() ==ID.Block) {
		    	if(getBounds().intersects(tempObject.getBounds())) {
		    		x+=(velX*5)* -1;
		    		y+=(velY*5)* -1;
		    		
		    		velX*=-1;
		    		velY*=-1;
		    		   	}
				
			else if(choose==0)
				{
				velX=(r.nextInt(4 - -4) + -4);
				velY=(r.nextInt(4 - -4) + -4);
				}
			}
		}
	}
		
	

	public void render(Graphics g) {
	    g.drawImage(bomb_image,x,y,null);
	}

	public Rectangle getBounds() {
		return new Rectangle(x,y,32,32);
	}
	


}
