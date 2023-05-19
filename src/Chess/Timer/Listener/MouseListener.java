package Chess.Timer.Listener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.event.MouseInputListener;

import Utils.CustomButton;
import Chess.Timer.Gui.EastGui;

public class MouseListener implements MouseInputListener {
	EastGui pnl;
	boolean sel = false;
    public MouseListener(EastGui pnl){
        this.pnl = pnl;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
		int y = e.getY();
        System.out.println("x: " + x + " y: " + y);
        ArrayList<CustomButton> b;
        b = pnl.getButtons();
        CustomButton selectedButton = null;
        for(CustomButton cb : b)
            if(cb.isClicked(x, y))
                selectedButton = cb;
        if(selectedButton != null){
            switch(selectedButton.getName()){
                case "btnNewGame":
                    System.out.println("new game");
                    
                break;
                case "btnUndo":
                    System.out.println("undo");  
                    if(pnl.isTimerRunning(false))pnl.stopTimer(false);
                    else pnl.startTimer(false);
                break;
    
                default: break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub  
    }
 
    
}