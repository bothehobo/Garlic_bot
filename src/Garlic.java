/*

SUPER DUPER SECRET CODE, DO NOT LOOK UNLESS YOU PLAY MINECRAFT PROFESSIONALLY
THIS CODE WILL GRAB A LOT OF GARLIC IN THE DRAYNOR CABINET, DON'T ASK ME WHY I WAS ASKED TO MAKE THIS
//NEJP
 */

//will comment for the noobas who are reading this

//all the imports are all the libraries that I'm using for osbot, most are pretty easy to understand
//these libraries are other code packaged in a way where I can access their functions
import java.awt.Graphics2D;


import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;

import org.osbot.rs07.utility.ConditionalSleep;
import org.osbot.rs07.api.Walking;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.WalkingEvent;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import java.util.*;
import org.osbot.rs07.script.MethodProvider;


//script manifest is a basic json file that gives out the basic info like author, name and info on the script
@ScriptManifest(author = "Nej", name = "Garlic", info = "Just an empty script :(", version = 0.1, logo = "")
public final class Garlic extends Script  {

    //the best way to explain a state is by thinking of a state as a group of actions
    //so like Idle is a state of doing nothing
    //Moving to an area is also a state etc, so I'll initialize the state that I'm going to use
    private State state;

    //Here are all the states that are needed for the script to work
    //bank is banking the garlic, walk to garlic is walking to the house, climb stairs is from sfirst floor to clicking
    //on the stairs etc etc
    private enum State {
        Bank, Garlic, WalkToGarlic, WalkToBank, ClimbStairs, WalkToCupboard
    }

    //initialize all the areas, OSRS is sliced into a 3d coordinate system, a single tile is
    //going to be a 1x1x1 that can be represented by an x, a y, and a z
    //here are all the areas I use
    Area GarlicHouse = new Area(new Position(3100, 3270, 0), new Position(3097, 3268, 0));
    Area NextToCupboard2 = new Area(new Position(3097, 3269, 1), new Position(3097, 3268, 1));
    Area Bank = Banks.DRAYNOR;
    Area UpstairsGarlic = new Area(new Position(3102, 3266, 1), new Position(3102, 3267, 1));
    Area NextToCupboard = new Area(new Position(3096, 3270, 1), new Position(3097,3269, 1));

    //get states is a whole bunch of if statements that checks where the player is and what step they are on when getting
    //the garlic
    private State getState(){
        //so if the player is in draynor bank and their inventory is full, return the state == "Bank"
        if (getInventory().isFull() && Bank.contains(myPlayer())) {
            return state.Bank;
        }


        //if the inventory is full and the the player is not in the bank, walk to the bank
        if (getInventory().isFull() && !Bank.contains(myPlayer())) {
            return state.WalkToBank;
        }

        //if the inventory is not full and the player is not in the garlic house go into this if
        if (!getInventory().isFull() && !GarlicHouse.contains(myPlayer())) {
            //if the player is not upstairs in the garlic house
            if(!UpstairsGarlic.contains(myPlayer())){
                //and if the player is not next to the cupboard, walk to the cupboard
                if (!NextToCupboard.contains(myPlayer())) {
                    return state.WalkToGarlic;
                }
                else
                {
                    //otherwise return the gather garlic state
                    return state.Garlic;
                }
            }
            else
            {
                //if they're upstairs and are not next to the garlic, walk to the cupboard
                return state.WalkToCupboard;
            }

        }

        //if the inventory is not full and the player is in the first floor, return climb stairs
        if(!getInventory().isFull() && GarlicHouse.contains(myPlayer())) {
            return state.ClimbStairs;
        }

        //if the inventory is not full and they're upstairs, walk to garlic
        if(!getInventory().isFull() && UpstairsGarlic.contains(myPlayer())) {
            return state.WalkToCupboard;
        }

        //if they're next to the cupboard, gather garlic
        if(!getInventory().isFull() && NextToCupboard.contains(myPlayer())) {
            return state.Garlic;
        }
        //if none of the if statements are true, go and bank

        return state.Bank;

    }

    @Override
    //on loop is constantly being called
    public int onLoop() throws InterruptedException {
        //first get the state from getstate
        state = getState();

        //switch is just initializing the state
        switch (state) {
            //if the state is bank do this etc
            case Bank:
                if(!getBank().isOpen()) {
                    getBank().open();
                    sleep(random(300, 800));
                }
                else {
                    getBank().depositAll();
                    sleep(random(200, 1500));
                }

            case WalkToBank:
                getWalking().webWalk(Bank);

                break;

            case ClimbStairs:
                RS2Object stairs = objects.getObjects().closest("staircase");
                if (stairs != null) {
                    stairs.interact("climb-up");
                    sleep(random(200, 500));
                }

                break;

            case WalkToGarlic:
                getWalking().webWalk(GarlicHouse);

                break;

            case WalkToCupboard:
                getWalking().webWalk(NextToCupboard);

                break;

            case Garlic:
                RS2Object cupboard = getObjects().closest("cupboard");
                if (cupboard != null) {
                    //Arrays.asList(cupboard.getDefinition().getActions()).contains("open");
                    if(cupboard.getDefinition().getActions()[0].equals("Open")) {
                        cupboard.interact("Open");
                        sleep(random(300, 500));
                    }
                    else {
                        cupboard.interact("Search");
                        sleep(random(100));
                    }
                }

        }
        return random(150, 175);


    }

    public void onStart() {

    }

}