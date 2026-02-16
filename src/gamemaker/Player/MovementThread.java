/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemaker.Player;

import gamemaker.Coordinate;
import gamemaker.GameMap;
import gamemaker.Sprite;
import java.awt.Point;

/**
 *
 * @author ug77alw
 */
public class MovementThread extends Thread {
         private GameMap gameMap;
         private GameMap tempMap;
         private int counter = 0;

         private final int MOVE_NORM = 60;
         private final int MOVE_FAST = 200;

         private double moveSpeed = MOVE_NORM; //speed in pixels per second when sprite is at 1 perspective scale
         private int switchSpeed = 3; //how many times the picture will change in a second
        
         //how frequently the sprite position will be updated.
         //NOTE: this will not repaint at this rate. The repaint
         //handler must do this.
         private int framerate = 50;


         private boolean threadAlive = true;
         private boolean move = false;

         public final int oneSecond = 1000;
         public final int DIRECTION_N = 1;
         public final int DIRECTION_NE = 2;
         public final int DIRECTION_E = 3;
         public final int DIRECTION_SE = 4;
         public final int DIRECTION_S = 5;
         public final int DIRECTION_SW = 6;
         public final int DIRECTION_W = 7;
         public final int DIRECTION_NW = 8;

         private int currentDirection;
         private Point newPosition = new Point(0,0);




        // private  PlayFrame playFrame;


         public void setMoveFast(){
             this.moveSpeed = MOVE_FAST;
             switchSpeed = 10;
         }
         public void setMoveNormal(){
             this.moveSpeed = MOVE_NORM;
             switchSpeed = 3;
         }

         public MovementThread(GameMap gameMap) {
             this.gameMap = gameMap;
             this.currentDirection = DIRECTION_S;
             tempMap = null;
             //this.playFrame = playFrame;
             
         }
         public void setMap(GameMap newMap){
             tempMap = newMap;
         }

         public void setMove(boolean canMove){
             
         
                 
             
             this.move = canMove;
         }

         /**
          * set the speed that the sprite moves
          * @param speed in 100 pixels per second
          */
         public void setMoveSpeed(int speed){
            moveSpeed = speed;
         }
         public void setDirection(int direction){
             this.currentDirection = direction;


             Sprite s = gameMap.getSpritePanel().getSprite();
             if(currentDirection == DIRECTION_N){
                s.setSpriteFacing(s.NORTH);
             }else if(currentDirection == DIRECTION_S){
                s.setSpriteFacing(s.SOUTH);
             }else if(currentDirection == DIRECTION_E){
                s.setSpriteFacing(s.EAST);
             }else if(currentDirection == DIRECTION_W){
                s.setSpriteFacing(s.WEST);
             }else if(currentDirection == DIRECTION_NE){
                s.setSpriteFacing(s.NORTHEAST);
             }else if(currentDirection == DIRECTION_SE){
                s.setSpriteFacing(s.SOUTHEAST);
             }else if(currentDirection == DIRECTION_SW){
                s.setSpriteFacing(s.SOUTHWEST);
             }else if(currentDirection == DIRECTION_NW){
                s.setSpriteFacing(s.NORTHWEST);
             }
         }

         /**
          *
          * @param speed - how many times the picture will change in a second

          */
         public void setSwitchSpeed(int speed){
            switchSpeed = speed;
         }


         
         private void moveSpriteIncrement(){




             double x = gameMap.getSpritePanel().getSpritePosition().getX();
             double y = gameMap.getSpritePanel().getSpritePosition().getY();
             Point p = new Point();
             p.setLocation(x, y);
             Double currentPerspective = gameMap.getSpritePanel().getPerspective().getPerspectiveAt(p);
             Double newMoveSpeed = moveSpeed*currentPerspective;

             if(currentPerspective < 1){
                 newMoveSpeed = (1.0/moveSpeed)*(currentPerspective+0.0);
             }

             
             

             //double edge = Math.sqrt(c/2); //opposite edge size
             double c = newMoveSpeed/(framerate + 0.0); //hypotenuse
            double edge = c * 0.6; // lowers speed slightly if walking diagonal
            //double edge = c * 0.6;
            double movedist = Math.round(newMoveSpeed/framerate);
           // double movedist = newMoveSpeed/(framerate + 0.0);
             if(movedist < 1){
             
                 if(moveSpeed == MOVE_FAST){
                      movedist = 3;
                 }else{
                      movedist = 1;
                 }
            
             }

       

             if(edge < 1){
               
                 if(moveSpeed == MOVE_FAST){
                      edge = 3;
                 }else{
                      edge = 1;
                 }
            
             }
   

             if(currentDirection == DIRECTION_N){
                y -= movedist;
             }else if(currentDirection == DIRECTION_S){
                y += movedist;
             }else if(currentDirection == DIRECTION_E){
                x += movedist;
             }else if(currentDirection == DIRECTION_W){
                x -= movedist;
             }else if(currentDirection == DIRECTION_NE){
                 y -= edge;
                 x += edge;
             }else if(currentDirection == DIRECTION_SE){
                 y += edge;
                 x += edge;
             }else if(currentDirection == DIRECTION_SW){
                 y += edge;
                 x -= edge;
             }else if(currentDirection == DIRECTION_NW){
                 y -= edge;
                 x -= edge;
             }else{
                 System.out.println("problem here");
             }
            //the new position based on how far the sprite can move per frame
             newPosition = new Point();
             newPosition.setLocation(x, y);
                  
           
             Boolean canMove = gameMap.getWalkableAreaPanel().isPointWithin(newPosition);
             //only moves if there is a walkable area to move into
            if(canMove == true){
             //if(true){ //always moves for testing
                 gameMap.getSpritePanel().setSpritePosition(newPosition);
                    
                 
               
             }

           
             

         }

         /**
          * handles the changing of the sprite image over time
          */
         private void stepHandler(){
             counter = counter + 1;
             if(counter >= framerate/switchSpeed){
                    gameMap.getSpritePanel().getSprite().takeStep();
                    counter = 0;

                  
             }
         }

       
        public void endThread(){
            this.threadAlive = false;
        }


         @Override
         public void run() {
                boolean canStop = true;
                 // compute primes larger than minPrime
                 while(threadAlive == true){
                    //ensures comparisons and movements etc happen at a specific framerate

                
                    wait(oneSecond/framerate);
                   
              
                    if(this.move == true && gameMap.getDialoguePanel().isDialogueDisplayed() != true){
                        //wait(oneSecond/framerate);
                        
                        moveSpriteIncrement();
                        stepHandler();

                        gameMap.getSpritePanel().repaint();
                        canStop = true;
                    }else if(canStop == true && gameMap.getDialoguePanel().isDialogueDisplayed() != true){
                       //wait(oneSecond/framerate);
                        canStop = false;
                        gameMap.getSpritePanel().getSprite().stopWalk();
                        gameMap.getSpritePanel().repaint();
                        counter = 32000000; //makes sure it changes to walking straight away on next  stepHandler()
                    }
                    if(tempMap != null){
                       //wait(oneSecond/framerate);
                        this.gameMap = this.tempMap;
                        tempMap = null;
                    }

                 }
            




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
