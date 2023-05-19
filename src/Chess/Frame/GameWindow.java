package Chess.Frame;

import javax.swing.JFrame;

import Chess.Board.Board;
import Chess.Timer.Gui.EastGui;

import java.awt.Dimension;
public class GameWindow extends JFrame{
	public static final int TILE_SIZE = 96;
	public static final int WIDTH = TILE_SIZE * Board.ROWS;
	public static final int HEIGTH = TILE_SIZE * Board.COLUMNS;
	public static int LANGUAGE;
	public GameWindow(int language){
        GameWindow.LANGUAGE = language;
		//this.add(new EastGui(), WIDTH, 0);
		this.add(new GamePanel());
		this.setTitle("Chess");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		this.getContentPane().setPreferredSize(new Dimension(WIDTH + 400, HEIGTH));
		this.pack();
		this.setLocationRelativeTo(null);
	}
}
