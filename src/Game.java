
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable  {
    
    private static final long serialVersionUID = 1L;
    
    private boolean isRunning=false;
    private Thread thread;
    private Handler handler;
    private Camera camera;
    private SpriteSheet ss,ss2;
    
    private BufferedImage level = null;
    private BufferedImage sprite_Sheet = null;
    private BufferedImage sprite_Sheet2= null;
    private BufferedImage floor = null;
    
    public int ammo=100;
    public int hp=100;
    public int Score=0;
     
    public Game(){
        new Window(1000, 563, "Shooting Game", this);
        start();
        
        handler = new Handler();
         camera= new Camera(0,0); 
        this.addKeyListener(new KeyInput(handler));
        
        BufferedImageLoader loader = new BufferedImageLoader();
        level = loader.loadImage("/wizard_level.png");
        sprite_Sheet = loader.loadImage("/wizard_images.png");
        sprite_Sheet2 =loader.loadImage("/bombimage.png");
        
        ss = new SpriteSheet(sprite_Sheet);
        ss2= new SpriteSheet(sprite_Sheet2);
        floor = ss.grabImage(4, 2, 32, 32);
        
        this.addMouseListener(new MouseInput(handler,camera,this,ss));
        loadLevel(level);
    }
    
    private void start(){
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }
    private void stop(){
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void run(){
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountofTicks = 60.0;
        double ns = 1000000000 / amountofTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
     //   int frames =0;
        while(isRunning){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1){
                tick();
                delta--;
            }
            render();
       //     frames++;
            
            if (System.currentTimeMillis() - timer > 1000){
                timer += 1000;
         //       frames = 0;
            }
        }
        stop();
    }
    
    public void tick(){
    	
    	for(int i=0;i<handler.object.size();i++) {
    		if(handler.object.get(i).getId()==ID.Player) {
    			camera.tick(handler.object.get(i));
    		}
    	}
        handler.tick();
    }
    
    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        
        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;
        //////////////////////////

        
        g2d.translate(-camera.getX(), -camera.getY() );    /// all things between g2d being translated (texture, object)
        
        for(int xx=0;xx<30*72;xx+=32) {
        	for(int yy=0;yy<30*41;yy+=32) {
        		g.drawImage(floor, xx, yy, null);
        	}
        }
       
     
        handler.render(g);
        
        g2d.translate(camera.getX(), camera.getY() );
        
        g.setColor(Color.gray);
        g.fillRect(5, 5, 200, 32);
        g.setColor(Color.green);
        g.fillRect(5, 5, hp*2, 32);
        g.setColor(Color.black);
        g.drawRect(5, 5, 200, 32);
        
        g.setColor(Color.white);
    	g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        g.drawString("Ammo: "+ ammo, 5, 60);
        
        g.setFont(new Font("TimesRoman", Font.BOLD, 25));
        g.drawString("Score: "+ Score, 890, 30);
        
        
        if(hp<=0)  {
        	g.setColor(Color.black);
        	g.fillOval(80,210, 800, 110);
        	
        	g.setColor(Color.red);
        	g.setFont(new Font("TimesRoman", Font.BOLD, 50));
            
        	g.drawString("Dead Man Can't Shoot!!", 200, 280);
        }
        
        if(Score>=22) {
        	g.setColor(Color.black);
        	g.fillOval(80,210, 900, 110);
        	
        	g.setColor(Color.orange);
        	g.setFont(new Font("TimesRoman", Font.BOLD, 50));
        	
            
        	g.drawString("Congratulation on your brilliant success", 100, 280);
        	
        }
        
        
        /////////////////////////
        g.dispose();
        bs.show();
       }
    
    //Loading the level
    private void loadLevel(BufferedImage image){
    	int w = image.getWidth();
    	int h= image.getHeight();
    	
    	for(int xx = 0;xx < w;xx++){
    		for(int yy =0;yy<h;yy++){
    			int pixel = image.getRGB(xx,yy);
    			int red = (pixel>>16) & 0xff;
    		 	int green = (pixel>>8) & 0xff;
    			int blue = (pixel) & 0xff;
    			
    			if(red==255 && green==0 && blue==0) 
    			      handler.addObject(new Block(xx*32,yy*32,ID.Block, ss));
    			if(red==0  && green==0 && blue == 255 )
    				handler.addObject(new Wizard(xx*32,yy*32,ID.Player,handler,this, ss));
    			
    			if(red==0 && green==255 && blue ==0)
    				handler.addObject(new Enemy(xx*32,yy*32,ID.Enemy,handler,this, ss));
    			
    			if(red==0  && green==255 && blue ==255)
    				handler.addObject(new Crate(xx*32,yy*32,ID.Crate, ss));
    			if(red==255 && green==255 && blue ==255)
    		     	handler.addObject(new Bomb(xx*32,yy*32,ID.Bomb,handler,ss2));
    			
    		}
    	}
    	
    }
    
    public static void main(String[] args) {
        new Game();
    }

    
}
