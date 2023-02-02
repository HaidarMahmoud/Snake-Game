import javax.swing.*;

public class GameF extends JFrame{
	GameF(){
		
		
		this.add(new GameP());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Snakey");
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
