package Chess.Game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Chess.Board.Board;
import Chess.Board.Move;
import Chess.Frame.GameWindow;
import Chess.Pieces.*;
import Utils.PieceImages;

public class Game{
	public static final int ENGLISH = 0;
	public static final int ITALIANO = 1;
	public Board board;

	private King wk;
	private King bk;
	private LinkedList<Piece> wPieces = new LinkedList<Piece>();
	private LinkedList<Piece> bPieces = new LinkedList<Piece>();

	private boolean player = true;
	private Piece selectedPiece = null;
	public boolean drag = false;
	private LinkedList<Piece> allPieces = new LinkedList<Piece>();
	private LinkedList<Move> allPlayersMove = new LinkedList<Move>();
	private LinkedList<Move> allEnemysMove = new LinkedList<Move>();
	public Game(){
		new PieceImages();
		board = new Board(this);
		loadFenPosition("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		start();
	}
	public void start(){
		fillPieces();
		generatePlayersTurnMoves(board);
		generateEnemysMoves(board);
		checkPlayersLegalMoves();
	}

	public void draw(Graphics g, int x, int y, JPanel panel){
		drawBoard(g);
		drawPiece(g, panel);
		drawPossibleMoves(g, panel);
		drag(selectedPiece, x, y, g, panel);
		drawKingInCheck(player, g, panel);
	}

	public void generatePlayersTurnMoves(Board board) {
		allPlayersMove = new LinkedList<Move>();
		for (Piece p : allPieces) {
			if (p.isWhite() == player) {
				p.fillAllPseudoLegalMoves(board);
				allPlayersMove.addAll(p.getMoves());
			}
		}
	}

	public void generateEnemysMoves(Board board) {
		allEnemysMove = new LinkedList<Move>();
		for (Piece p : allPieces) {
			if (p.isWhite() != player) {
				p.fillAllPseudoLegalMoves(board);
				allEnemysMove.addAll(p.getMoves());
			}
		}
	}
	public LinkedList<Piece> getAllPieces(){
		return allPieces;
	}
	public void changeSide() {
		player = !player;
		generateEnemysMoves(board);
		generatePlayersTurnMoves(board);
		checkPlayersLegalMoves();
		checkMate();
	}
/*
 * 
	public void randomPlay() {
		if (gameOver) {
			return;
		}
		if (!player) {
			Random r = new Random();
			selectedPiece = bPieces.get(r.nextInt(bPieces.size()));
			while (selectedPiece.getMoves().isEmpty()) {
				selectedPiece = bPieces.get(r.nextInt(bPieces.size()));
			}
			Move m = selectedPiece.getMoves().get(r.nextInt(selectedPiece.getMoves().size()));
			move(m.getxNew(), m.getyNew());
		}
	}
 */

	public void selectPiece(int x, int y){
		if (selectedPiece == null && board.getPiece(x, y) != null && board.getPiece(x, y).isWhite() == player)
			selectedPiece = board.getPiece(x, y);
	}

	public void checkMate(){
		switch(GameWindow.LANGUAGE){
			case ENGLISH:
				if(player){
					for (Piece p : wPieces)
						if (!p.getMoves().isEmpty()) 
							return;
						
				}else{
					for (Piece p : bPieces) 
						if (!p.getMoves().isEmpty()) 
							return;
				}
				if(player){
					if(wk.isInCheck())
						JOptionPane.showMessageDialog(null, "Checkmate! " + (!player ? "White" : "Black") + " wins!");
					else 
						JOptionPane.showMessageDialog(null, "Stalemate ");
				}else{
					if(bk.isInCheck())
						JOptionPane.showMessageDialog(null, "Checkmate! " + (!player ? "White" : "Black") + " wins!");
					else
						JOptionPane.showMessageDialog(null, "Stalemate! ");
				}
				break;
			case ITALIANO:
				if(player){
					for (Piece p : wPieces)
						if (!p.getMoves().isEmpty()) 
							return;
						
				}else{
					for (Piece p : bPieces) 
						if (!p.getMoves().isEmpty()) 
							return;
				}
				if(player){
					if(wk.isInCheck())
						JOptionPane.showMessageDialog(null, "Scacco matto! " + (!player ? "Il bianco" : "Il nero") + " vince!");
					else 
						JOptionPane.showMessageDialog(null, "Stallo! ");
				}else{
					if(bk.isInCheck())
						JOptionPane.showMessageDialog(null, "Scacco matto! " + (!player ? "Il bianco" : "Il nero") + " vince!");
					else
						JOptionPane.showMessageDialog(null, "Stallo! ");
				}
				break;
		}
	}
	public void checkPlayersLegalMoves() {
		LinkedList<Piece> pieces = null;
		if (player) 
			pieces = wPieces;
		else 
			pieces = bPieces;
		for (Piece p : pieces) 
			checkLegalMoves(p);
	}
	public void checkLegalMoves(Piece piece){
		LinkedList<Move> movesToRemove = new LinkedList<Move>();
		Board clonedBoard = board.getNewBoard();
		Piece clonedActive = piece.getClone();

		for(Move move : clonedActive.getMoves()){
			clonedBoard = board.getNewBoard();
			clonedActive = piece.getClone();
			clonedActive.makeMove(move.getxNew(), move.getyNew(), clonedBoard);
			LinkedList<Piece> enemyPieces = new LinkedList<Piece>();
			Piece king = null;
			if(piece.isWhite()){
				enemyPieces = bPieces;
				king = wk;
			}else{
				enemyPieces = wPieces;
				king = bk;
			}
			for (Piece enemyP : enemyPieces){
				Piece clonedEnemyPiece = enemyP.getClone();
				clonedEnemyPiece.fillAllPseudoLegalMoves(clonedBoard);
				for (Move bMove : clonedEnemyPiece.getMoves()){
					if(!(clonedActive instanceof King) && bMove.getxNew() == king.getXcord() && bMove.getyNew() == king.getYcord()
						&& clonedBoard.getGrid()[enemyP.getXcord()][enemyP.getYcord()] == enemyP.getValueInTheboard()){
						movesToRemove.add(move);
					}else if(clonedActive instanceof King){
						if (bMove.getxNew() == clonedActive.getXcord() && bMove.getyNew() == clonedActive.getYcord() 
							&& clonedBoard.getGrid()[enemyP.getXcord()][enemyP.getYcord()] == enemyP.getValueInTheboard()){
							movesToRemove.add(move);
						}
					}
				}
			}
		}
		for(Move move : movesToRemove)
			piece.getMoves().remove(move);
	}
	public void drag(Piece piece, int x, int y, Graphics g, JPanel panel){
		if (piece != null && drag == true) 
			piece.drawDragPiece(g, player, x, y, panel);
	}
	public void move(int x, int y){
		if(selectedPiece != null){
			if (selectedPiece.makeMove(x, y, board)) {
				tryToPromote(selectedPiece);
				changeSide();
				selectedPiece = null;
			}
			drag = false;
		}
	}
	public void drawKingInCheck(boolean isWhite, Graphics g, JPanel panel){
		g.setColor(Color.RED);
		if(isWhite && wk.isInCheck())
			g.drawRect(wk.getXcord() * GameWindow.TILE_SIZE, wk.getYcord() * GameWindow.TILE_SIZE, GameWindow.TILE_SIZE, GameWindow.TILE_SIZE);
		else if(bk.isInCheck())
			g.drawRect(bk.getXcord() * GameWindow.TILE_SIZE, bk.getYcord() * GameWindow.TILE_SIZE, GameWindow.TILE_SIZE, GameWindow.TILE_SIZE);
		panel.revalidate();
		panel.repaint();
	}
	public void drawBoard(Graphics g){
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++){
				if((i + j) % 2 == 1)
					g.setColor(new Color(118, 150, 86));
				else 
					g.setColor(new Color(238, 238, 210));
				g.fillRect(i * GameWindow.TILE_SIZE, j * GameWindow.TILE_SIZE, GameWindow.TILE_SIZE, GameWindow.TILE_SIZE);
			}
	}
	public void tryToPromote(Piece p){
		if (p instanceof Pawn) 
			if (((Pawn) p).madeToTheEnd()) 
				choosePiece(p, showMessageForPromotion());
	}
	public int showMessageForPromotion(){
		Object[] optionsE = { "Queen", "Rook", "Knight", "Bishop" };
		Object[] optionsI = { "Regina", "Torre", "Cavallo", "Alfiere" };
		drag = false;
		switch(GameWindow.LANGUAGE){
			case ENGLISH: 
				return JOptionPane.showOptionDialog(null, "Choose piece to promote to", null, 
				JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, null, optionsE, optionsE[0]);
			case ITALIANO:
				return JOptionPane.showOptionDialog(null, "Scegli il pezzo da promuovere a", null, 
				JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, null, optionsI, optionsI[0]);
			default:
				return JOptionPane.showOptionDialog(null, "Choose piece to promote to", null, 
				JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, null, optionsE, optionsE[0]);
		}
	}
	public void choosePiece(Piece p, int choice){
		switch (choice){
		case 0:
			allPieces.remove(p);
			p = new Queen(p.getXcord(), p.getYcord(), p.isWhite(), board, p.isWhite() ? 8 : -8);
			allPieces.add(p);
			break;
		case 1:
			allPieces.remove(p);
			p = new Rook(p.getXcord(), p.getYcord(), p.isWhite(), board, p.isWhite() ? 5 : -5);
			allPieces.add(p);
			break;
		case 2:
			allPieces.remove(p);
			p = new Knight(p.getXcord(), p.getYcord(), p.isWhite(), board, p.isWhite() ? 3 : -3);
			allPieces.add(p);
			break;
		case 3:
			allPieces.remove(p);
			p = new Bishop(p.getXcord(), p.getYcord(), p.isWhite(), board, p.isWhite() ? 3 : -3);
			allPieces.add(p);
			break;
		default:
			allPieces.remove(p);
			p = new Queen(p.getXcord(), p.getYcord(), p.isWhite(), board, p.isWhite() ? 8 : -8);
			allPieces.add(p);
			break;
		}
		fillPieces();
	}
	public void drawPossibleMoves(Graphics g, JPanel panel){
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3));
		if (selectedPiece != null)
			selectedPiece.showMoves(g2, panel);
	}
	public void drawPiece(Graphics g, JPanel panel){
		for(Piece p : allPieces)
			p.draw(g, false, panel);
	}
	public Piece getSelectedPiece(){
		return selectedPiece;
	}
	public LinkedList<Move> getAllEnemysMove(){
		return allEnemysMove;
	}
	public void setSelectedPiece(Piece p){
        selectedPiece = p;
    }
	public void loadFenPosition(String fen){
		String[] parts = fen.split(" ");
		String position = parts[0];
		int row = 0, col = 0;
		for(char c : position.toCharArray()){
			if (c == '/'){
				row++;
				col = 0;
			}
			if(Character.isLetter(c)){
				if(Character.isLowerCase(c))
					addToBoard(col, row, c, false);
				else 
					addToBoard(col, row, c, true);
				col++;
			}
			if(Character.isDigit(c)) 
				col += Integer.parseInt(String.valueOf(c));
		}
		if(parts[1].equals("w"))
			player = true;
		else
			player = false;
	}
	public void fillPieces(){
		wPieces = new LinkedList<Piece>();
		bPieces = new LinkedList<Piece>();
		for(Piece p : allPieces){
			p.setGame(this);
			if (p.isWhite())
				wPieces.add(p);
			else 
				bPieces.add(p);
		}
	}
	public void addToBoard(int x, int y, char c, boolean isWhite){
		switch(String.valueOf(c).toUpperCase()){
		case "R":
			Rook r = new Rook(x, y, isWhite, board, isWhite ? 5 : -5);
			allPieces.add(r);
			System.out.println(r.toString());
			break;
		case "N":
			allPieces.add(new Knight(x, y, isWhite, board, isWhite ? 3 : -3));
			break;
		case "B":
			allPieces.add(new Bishop(x, y, isWhite, board, isWhite ? 3 : -3));
			break;
		case "Q":
			allPieces.add(new Queen(x, y, isWhite, board, isWhite ? 8 : -8));
			break;
		case "K":
			King king = new King(x, y, isWhite, board, isWhite ? 10 : -10);
			allPieces.add(king);
			if(isWhite)
				wk = king;
			else
				bk = king;
			break;
		case "P":
		allPieces.add(new Pawn(x, y, isWhite, board, isWhite ? 1 : -1));
			break;
		}
	}
    public Board getBoard() {
        return board;
    }
}
