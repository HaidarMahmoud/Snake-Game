import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Random;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;



public class GameP extends JPanel implements ActionListener{

	static final int Screen_Width = 650;
	static final int Screen_Height = 650;
	static final int Unit_Size = 25;
	static final int Units = (Screen_Width*Screen_Height)/(Unit_Size*Unit_Size);
	static final int Delay = 65;
	final int y[] = new int [Units];
	final int x[] = new int [Units];
	int Parts = 6;
	int PointsEaten;
	int PointsX;
	int PointsY;
	char direction = 'D';
	boolean run = false;
	Timer timer;
	Random random;
	Image apple;
	File soundFile;
	AudioInputStream soundStream;
	Clip soundClip;
	
	GameP(){
		random = new Random();
		this.setPreferredSize(new Dimension(Screen_Width, Screen_Height));
		this.setBackground(Color.getHSBColor(0, 0, 0));
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		apple = new ImageIcon("pngegg.png").getImage();
		
		 try {
		      soundFile = new File("point.wav");
		      soundStream = AudioSystem.getAudioInputStream(soundFile);
		      soundClip = AudioSystem.getClip();
		      soundClip.open(soundStream);
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		 
	    
		GameStart();
	}
	
	public void GameStart() {
		 Parts = 6;

		    for (int i = 0; i < Parts; i++) {
		      x[i] = 200 - i * 10;
		      y[i] = 200;
		    }
		    
		NewPoint();
 		run = true;
		timer = new Timer(Delay,this);
		timer.start();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Draw(g);
	}
	
	public void Draw(Graphics g) {
		
		if(run) {
			g.drawImage(apple, PointsX, PointsY, this);
		
			for (int i = 0; i<Parts; i++) {
				if (i==0) {
					g.setColor(new Color(102,255,102));
					g.fillRect(x[i],y[i],Unit_Size, Unit_Size);
				}
				else {
					g.setColor(new Color(0,102,0));
					g.fillRect(x[i],y[i],Unit_Size,Unit_Size );
					
				}
			}
			
			 g.setColor(Color.WHITE);
	         g.setFont(new Font("Arial", Font.BOLD, 14));
	         g.drawString("Score: " + (PointsEaten ), Screen_Width -80, 20);
			
		}
		else {
			End(g);
//			add(restartButton);
		}
	}
	

	public void NewPoint() {
		PointsX = random.nextInt((int)(Screen_Width/Unit_Size))*Unit_Size;
		PointsY = random.nextInt((int)(Screen_Height/Unit_Size))*Unit_Size;
	}
	public void Move() {
		for (int i = Parts; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'W' : 
			y[0] = y[0] - Unit_Size;
			break;
		case 'A' : 
			x[0] = x[0] - Unit_Size;
			break;
		case 'S' : 
			y[0] = y[0] + Unit_Size;
			break;
		case 'D' : 
			x[0] = x[0] + Unit_Size;
			break;
		}
		
	}
	
	
	public void CheckPoint() {
		if ((x[0] == PointsX) && (y[0] == PointsY)) {
			Parts++;
			PointsEaten++;
			NewPoint();
			if ((x[0] == PointsX) && (y[0] == PointsY)) {
				PointsEaten++;
				GameStart();
				soundClip.start();
			    }
		}
	}
	
	public void CheckCollision() {
		//checks for collision between head and body 
		for (int i = Parts; i>0;i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				run = false;
			}
		}
		//checks head touching borders
		if (x[0] < 0) {
			run = false;
		}
		if (x[0] > Screen_Width) {
			run = false;
		}
		if (y[0] < 0) {
			run = false;
		}
		if (y[0] > Screen_Height) {
			run = false;
		}
		if (!run) {
			timer.stop();
		}
	}
	
	public void End(Graphics g) {
		g.setColor(Color.white);
		g.setFont( new Font("Arial", Font.BOLD, 25));
		FontMetrics M = getFontMetrics(g.getFont());
		g.drawString("Score: " + PointsEaten, (Screen_Width - M.stringWidth("Score: " + PointsEaten))/2, g.getFont().getSize());
		
		g.setFont( new Font("Arial", Font.BOLD, 40));
        g.setColor(Color.white);
		FontMetrics M2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (Screen_Width - M2.stringWidth("Game Over"))/2, Screen_Height/2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (run) {
			Move();
			CheckPoint();
			CheckCollision();	
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction !='D') {
					direction = 'A';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction !='A') {
					direction = 'D';
					
			}
				break;
			
			case KeyEvent.VK_UP:
				if(direction !='S') {
					direction = 'W';
					
			}
				break;
				
			case KeyEvent.VK_DOWN:
				if(direction !='W') {
					direction = 'S';
					
			}
				break;
		}
	}
}
}
