/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Player;

/**
 *
 * @author ug77alw
 */
public class RepaintThread extends Thread{

    private int framerate = 30; //how frequently the sprite will be moved
    public final int oneSecond = 1000;
    private PlayFrame playFrame;
    private boolean threadAlive = true;


        public RepaintThread(PlayFrame playFrame){
            this.playFrame = playFrame;
        }


        public void run() {

            while(threadAlive == true){
            wait(oneSecond/framerate);
                playFrame.setFocusOnSprite();
            }


        }
        public void endThread(){
            this.threadAlive = false;
        }
    /**
         * waits a set number of milliseconds
         * @param n
         */
         public void wait (int n){
            try {
                Thread.sleep(n);
            } catch (InterruptedException e) {}

             /**
             long t0,t1;
             t0=System.currentTimeMillis();
             do{
                 t1=System.currentTimeMillis();
             }
             while (t1-t0<n);
              * **/
        }

}
