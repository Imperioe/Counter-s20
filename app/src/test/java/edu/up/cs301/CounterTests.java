package edu.up.cs301;

import android.view.MotionEvent;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import edu.up.cs301.game.GameFramework.actionMessage.MyNameIsAction;
import edu.up.cs301.game.GameFramework.actionMessage.ReadyAction;
import edu.up.cs301.game.GameFramework.players.GamePlayer;
import edu.up.cs301.game.R;
import edu.up.cs301.counter.CounterLocalGame;
import edu.up.cs301.counter.CounterMainActivity;
import edu.up.cs301.counter.infoMessage.CounterState;
import edu.up.cs301.counter.counterActionMessage.CounterMoveAction;

import static org.junit.Assert.*;

/* @author Eric Imperio
 * @version 2020
 * Use this as a template to make your own tests
 * These are go tests to use
 * Additional tests are good as well
 * NOTE: Avoid tests that simply check one action.
 *    Example: You know that the following will set the expected value.
 *        a = b + 2;
 */
@RunWith(RobolectricTestRunner.class)
public class CounterTests {

    public CounterMainActivity activity;

    @Before
    public void setup() throws Exception {
        activity = Robolectric.buildActivity(CounterMainActivity.class).create().resume().get();
    }

    //This does a full game to verify it works
    // Notice that it includes invalid moves
    // You can do it this way or have multiple unit tests that do this
    // Sometimes easier to just have one since this is turn-based
    @Test
    public void test_checkGamePlay() {
        //TODO: Modify the following for your game
        //Starting the game
        View view = activity.findViewById(R.id.playGameButton);
        activity.onClick(view);
        //Getting the created game
        CounterLocalGame counterLocalGame = (CounterLocalGame) activity.getGame();
        //Getting the players
        GamePlayer[] gamePlayers = counterLocalGame.getPlayers();
        //Sending the names of the players to the game
        for (GamePlayer gp : gamePlayers) {
            counterLocalGame.sendAction(new MyNameIsAction(gp, gp.getClass().toString()));
        }
        //Telling the game everyone is ready
        for (GamePlayer gp : gamePlayers) {
            counterLocalGame.sendAction(new ReadyAction(gp));
        }
        //TODO: Start Making moves here
        GamePlayer player1 = gamePlayers[0];
        GamePlayer player2 = gamePlayers[1];
        //Can I make two moves in a row?
        counterLocalGame.sendAction(new CounterMoveAction(player1, true));
        counterLocalGame.sendAction(new CounterMoveAction(player1, true));
        //Setting the expected outcome of the two lines above
        CounterState match = new CounterState(2);
        //Testing that I couldn't make two moves in a row
        assertTrue("Game States were not equal", ((CounterState) counterLocalGame.getGameState()).equals(match));
        //Make sure player two can play
        counterLocalGame.sendAction(new CounterMoveAction(player2, false));
        //Setting the expected outcome of the line above
        match = new CounterState(1);
        //Testing that I couldn't make two moves in a row
        assertTrue("Game States were not equal", ((CounterState) counterLocalGame.getGameState()).equals(match));
        //Make sure players can add and subtract
        counterLocalGame.sendAction(new CounterMoveAction(player1, false));
        counterLocalGame.sendAction(new CounterMoveAction(player2, true));
        //Setting the expected outcome of the two lines above
        match = new CounterState(1);
        //Testing that I couldn't make two moves in a row
        assertTrue("Game States were not equal", ((CounterState) counterLocalGame.getGameState()).equals(match));
        //Get to a finished game
        counterLocalGame.sendAction(new CounterMoveAction(player1, true));
        counterLocalGame.sendAction(new CounterMoveAction(player1, true));
        counterLocalGame.sendAction(new CounterMoveAction(player1, true));
        counterLocalGame.sendAction(new CounterMoveAction(player1, true));
        counterLocalGame.sendAction(new CounterMoveAction(player1, true));
        counterLocalGame.sendAction(new CounterMoveAction(player1, true));
        counterLocalGame.sendAction(new CounterMoveAction(player1, true));
        counterLocalGame.sendAction(new CounterMoveAction(player1, true));
        counterLocalGame.sendAction(new CounterMoveAction(player1, true));
        //Expected Changes from the lines above
        match = new CounterState(10);
        //Check those worked
        assertTrue("Game States were not equal", ((CounterState) counterLocalGame.getGameState()).equals(match));
        //Make sure player 1 won
        assertEquals("Player 1 did not win", 0, counterLocalGame.whoWon());
        //Check if you can add after game over
        counterLocalGame.sendAction(new CounterMoveAction(player1, true));
        assertTrue("Game States were not equal", ((CounterState) counterLocalGame.getGameState()).equals(match));
        //Check if you can substract after game over
        counterLocalGame.sendAction(new CounterMoveAction(player2, false));
        assertTrue("Game States were not equal", ((CounterState) counterLocalGame.getGameState()).equals(match));
    }

    //Tests focused on the state: copy constructors and equals
    //copy cons:  empty default state, in progress state, full board state
    //This tests the copy constructor when nothing is set
    @Test
    public void test_CopyConstructorOfState_Empty(){
        CounterState counterState = new CounterState(0);
        CounterState copyState = new CounterState(counterState);
        assertTrue("Copy Constructor did not produce equal States", counterState.equals(copyState));
    }

    //Make state that looks like a game that'd be in progress
    @Test
    public void test_CopyConstructorOfState_InProgress(){
        CounterState counterState = new CounterState(10);
        CounterState copyState = new CounterState(counterState);
        assertTrue("Copy Constructor did not produce equal States", counterState.equals(copyState));
    }

    // Make a state that has all values set to something (preferably not default)
    @Test
    public void test_CopyConstructorOfState_Full(){
        CounterState counterState = new CounterState(20);
        CounterState copyState = new CounterState(counterState);
        assertTrue("Copy Constructor did not produce equal States", counterState.equals(copyState));
    }

    //These follow the same structure as copy but they test your equals method
    // Copy might fail because your equals is wrong
    // DO NOT make equals use copy while copy is using equals. You won't know which is broken easily.
    //Equals
    @Test
    public void test_Equals_State_Empty(){
        CounterState counterState = new CounterState(0);
        CounterState otherState = new CounterState(0);
        assertTrue("Equals method did not agree the States where equal", counterState.equals(otherState));
    }

    @Test
    public void test_Equals_State_InProgress(){
        CounterState counterState = new CounterState(-5);
        CounterState otherState = new CounterState(-5);
        assertTrue("Equals method did not agree the States where equal", counterState.equals(otherState));
    }

    @Test
    public void test_Equals_State_Full(){
        CounterState counterState = new CounterState(-15);
        CounterState otherState = new CounterState(-15);
        assertTrue("Equals method did not agree the States where equal", counterState.equals(otherState));
    }
}
