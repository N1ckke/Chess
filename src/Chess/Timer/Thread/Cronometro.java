package Chess.Timer.Thread;

import Chess.Timer.Gui.EastGui;

public class Cronometro extends Thread {
    private int countdownMinutes;
    private int countdownSeconds;
    private EastGui pnl;
    private boolean running;
    private boolean isWhite;

    public Cronometro(EastGui pnl, boolean isWhite) {
        this.countdownMinutes = 5;
        this.countdownSeconds = 0;
        this.pnl = pnl;
        running = false;
        this.isWhite = isWhite;
        if(isWhite){
            pnl.setTimerPlayer(5, 0);
        }else pnl.setTimerOpponent(5, 0);

    }

    @Override
    public void run() {
       while(true){
        
        
        
        if (countdownMinutes >= 0 && running) {
            
            if (countdownSeconds == 0) {
                if (countdownMinutes == 0) {
                    System.out.println("Tempo scaduto!");
                    break;
                } else {
                    countdownMinutes--;
                    countdownSeconds = 60;
                }
            } else {
                countdownSeconds--;
                if(isWhite){
                    pnl.setTimerPlayer(countdownMinutes, countdownSeconds);
                }else pnl.setTimerOpponent(countdownMinutes, countdownSeconds);
            }
    
                
            }else if(countdownMinutes < 0)break;


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
       }


       
    }

    public synchronized void stopTimer() {
        running = false;
    }

    public synchronized void startTimer() {
        running = true;
        if(!this.isAlive())this.start();
        
    }

    public boolean isRunning() {
        return running;
    }

    
    

}
