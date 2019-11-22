/*

THIS CODE WILL GRAB A LOT OF GARLIC IN THE DRAYNOR CABINET, DON'T ASK ME WHY I WAS ASKED TO MAKE THIS
//NEJP
 */


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


@ScriptManifest(author = "Nej", name = "Garlic", info = "Just an empty script :(", version = 0.1, logo = "")
public final class Garlic extends Script  {


    private State state;


    private enum State {
        Bank, Garlic, WalkToGarlic, WalkToBank, ClimbStairs, WalkToCupboard
    }


    Area GarlicHouse = new Area(new Position(3100, 3270, 0), new Position(3097, 3268, 0));
    Area NextToCupboard2 = new Area(new Position(3097, 3269, 1), new Position(3097, 3268, 1));
    Area Bank = Banks.DRAYNOR;
    Area UpstairsGarlic = new Area(new Position(3102, 3266, 1), new Position(3102, 3267, 1));
    Area NextToCupboard = new Area(new Position(3096, 3270, 1), new Position(3097,3269, 1));

    private State getState(){
        if (getInventory().isFull() && Bank.contains(myPlayer())) {
            return state.Bank;
        }


        if (getInventory().isFull() && !Bank.contains(myPlayer())) {
            return state.WalkToBank;
        }

        if (!getInventory().isFull() && !GarlicHouse.contains(myPlayer())) {
            if(!UpstairsGarlic.contains(myPlayer())){
                if (!NextToCupboard.contains(myPlayer())) {
                    return state.WalkToGarlic;
                }
                else
                {
                    return state.Garlic;
                }
            }
            else
            {
                return state.WalkToCupboard;
            }

        }

        if(!getInventory().isFull() && GarlicHouse.contains(myPlayer())) {
            return state.ClimbStairs;
        }

        if(!getInventory().isFull() && UpstairsGarlic.contains(myPlayer())) {
            return state.WalkToCupboard;
        }

        if(!getInventory().isFull() && NextToCupboard.contains(myPlayer())) {
            return state.Garlic;
        }

        return state.Bank;

    }

    @Override
    public int onLoop() throws InterruptedException {
        state = getState();

        switch (state) {
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
