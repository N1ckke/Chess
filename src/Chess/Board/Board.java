package Chess.Board;

import java.util.*;

import Chess.Pieces.*;

import Chess.Game.*;

public class Board implements Cloneable{
	public static final int ROWS = 8;
	public static final int COLUMNS = 8;
	
	private int[][] grid;
	private Piece[][] pieces;
	private Piece lastPieceMoved;
	private Move lastMove;
	private Piece died;
	
	private Stack<Move> lastMoves = new Stack<>();
	private Stack<Piece> deadPieces = new Stack<>();
	private LinkedList<Piece> piecesList = new LinkedList<Piece>();
	private Game game;
	public Board(Game game){
		grid = new int[ROWS][COLUMNS];
		pieces = new Piece[ROWS][COLUMNS];
		this.game = game;
	}
	public void setPieceIntoBoard(int x,int y,Piece piece){
		if(piece != null){
			grid[x][y] = piece.getValueInTheboard();
			pieces[x][y] = piece;
			piecesList.add(piece);			
		}else{
			grid[x][y] = 0;
			pieces[x][y] = null;
		}
	}	
	public void updatePieces(int fromX,int fromY,int toX,int toY,Piece piece){
		lastMove = new Move(fromX, fromY, toX, toY, piece);
		lastMoves.add(lastMove);
		if(pieces[toX][toY] != null){
			died = pieces[toX][toY];
			deadPieces.add(died);
			piecesList.remove(died);
			game.getAllPieces().remove(died);
			game.fillPieces();
		}else{
			deadPieces.add(null);
		}
		grid[fromX][fromY] = 0;
		grid[toX][toY] =  piece.getValueInTheboard();
		pieces[fromX][fromY] = null;
		pieces[toX][toY] = piece;
		lastPieceMoved = piece;
	}
	public void undoMove(){
		if(!lastMoves.isEmpty()){
			Move move = lastMoves.pop();
			Piece dead = deadPieces.pop();
			lastPieceMoved = move.getPiece();
			grid[move.getxOld()][move.getyOld()] = move.getPiece().getValueInTheboard();
			pieces[move.getxOld()][move.getyOld()] = move.getPiece();
			move.getPiece().setXcord(move.getxOld());
			move.getPiece().setYcord(move.getyOld());
			if(move.getPiece() instanceof Pawn) 
				if(move.getPiece().getYcord() == (move.getPiece().isWhite() ? 6 : 1))
					((Pawn)	move.getPiece()).setFirstMove(true);
			if(move.getPiece() instanceof Rook) 
				if(((Rook) move.getPiece()).isJustMoved()){
					((Rook) move.getPiece()).setHasMoved(false);
					((Rook) move.getPiece()).setJustMoved(false);
				}
			if(dead != null){
				game.getAllPieces().add(dead);
				game.fillPieces();
				grid[move.getxNew()][move.getyNew()] = dead.getValueInTheboard();
				pieces[move.getxNew()][move.getyNew()] = dead;
				dead.setXcord(move.getxNew());
				dead.setYcord(move.getyNew());
			}
			else{
				grid[move.getxNew()][move.getyNew()] = 0;
				pieces[move.getxNew()][move.getyNew()] = dead;
			}
			game.changeSide();
		}
		return;
	}
	public Piece getPiece(int x,int y){
		return pieces[x][y];
	}
	public int[][] getGrid(){
		return grid;
	}
	public void setGrid(int[][] grid){
		this.grid = grid;
	}
	public int getXY(int x,int y){
		return grid[x][y];
	}
	public void setXY(int x,int y,int value){
		 grid[x][y] = value ;
	}
	public Board getNewBoard(){
		Board b = new Board(game);
		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++) 
				if(this.getPiece(i, j) != null) 
					b.setPieceIntoBoard(i, j, this.getPiece(i, j).getClone());
		return b;
	}
	public void printBoard(){
		for(int i=0; i<8; i++) 
			for(int j=0; j<8; j++)
				System.out.print(grid[j][i] +  "  ");
	}
}
