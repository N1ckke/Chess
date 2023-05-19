package Chess.Timer.Gui;

import javax.swing.*;

import Chess.Frame.GameWindow;
import Chess.Timer.Listener.MouseListener;
import Chess.Timer.Thread.Cronometro;
import Utils.CustomButton;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

public class EastGui extends JPanel{

    private final static int WIDTH = 400;
    private final static int HEIGHT = 768;
    private ArrayList<CustomButton> buttons;
    private Cronometro timerPlayer, timerOpponent;
    private JLabel lblTimerPlayer, lblTimerOpponent;

    public EastGui(){

        lblTimerPlayer = new JLabel();
        lblTimerOpponent = new JLabel();

        timerPlayer = new Cronometro(this, true);
        timerOpponent = new Cronometro(this, false);

        
        this.setLayout(null);

        
        lblTimerPlayer.setSize(50, 30);
        lblTimerPlayer.setLocation(350, 728);
        lblTimerOpponent.setSize(50, 30);
        lblTimerOpponent.setLocation(350, 5);
        

        this.add(lblTimerPlayer);
        this.add(lblTimerOpponent);



        
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
        buttons = new ArrayList<CustomButton>();
        createButtons();
        addMouseListener(new MouseListener(this));
        
        
        

        
        
    }


    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        
            drawButtons(g);
        }
    



    private void createButtons(){
        buttons.add(new CustomButton("OpponentImage", "/Chess/Timer/img/UserImg.png", GameWindow.WIDTH + 5, 5, 34, 34,true));
        buttons.add(new CustomButton("PlayerImage", "/Chess/Timer/img/UserImg.png", GameWindow.WIDTH + 5, 728, 34, 34,true));
        buttons.add(new CustomButton("btnUndo", "/Chess/Timer/img/btnUndo.png", GameWindow.WIDTH + 50, 450, 100, 60,true));
        buttons.add(new CustomButton("btnNewGame", "/Chess/Timer/img/btnNewGame.png", GameWindow.WIDTH + 175, 450, 200, 60,true));
        buttons.add(new CustomButton("Opponent", "/Chess/Timer/img/Opponent.png", GameWindow.WIDTH + 39, 5, 70, 40,true));
        buttons.add(new CustomButton("Player", "/Chess/Timer/img/Player.png", GameWindow.WIDTH + 39, 728, 70, 40,true));
    }
    private void drawButtons(Graphics g){
        for (CustomButton customButton : buttons) {
            
            customButton.displayOnPanel(g);
        }
    }

    public ArrayList<CustomButton> getButtons(){
        return buttons;
    }

    public void setTimerPlayer(int min, int sec){
        lblTimerPlayer.setText(String.format("%02d:%02d\n", min, sec));
    }

    public void setTimerOpponent(int min, int sec){
        lblTimerOpponent.setText(String.format("%02d:%02d\n", min, sec));
    }

    public void startTimer(boolean isWhite){
        if(isWhite)timerPlayer.startTimer();
        else timerOpponent.startTimer();
    }

    public boolean isTimerRunning(boolean isWhite){
        if(isWhite)return timerPlayer.isRunning();
        else return timerOpponent.isRunning();
    }

    public void stopTimer(boolean isWhite){
        if(isWhite)timerPlayer.stopTimer();
        else timerOpponent.stopTimer();
    }
}