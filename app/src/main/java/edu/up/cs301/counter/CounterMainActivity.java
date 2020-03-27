package edu.up.cs301.counter;

import java.util.ArrayList;

import edu.up.cs301.counter.infoMessage.CounterState;
import edu.up.cs301.counter.players.CounterComputerPlayer1;
import edu.up.cs301.counter.players.CounterComputerPlayer2;
import edu.up.cs301.counter.players.CounterHumanPlayer;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.infoMessage.GameState;
import edu.up.cs301.game.GameFramework.players.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.gameConfiguration.GameConfig;
import edu.up.cs301.game.GameFramework.gameConfiguration.GamePlayerType;
import edu.up.cs301.game.GameFramework.utilities.Logger;
import edu.up.cs301.game.GameFramework.utilities.Saving;

/**
 * this is the primary activity for Counter game
 * 
 * @author Andrew M. Nuxoll
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class CounterMainActivity extends GameMainActivity {
	//Tag for logging
	private static final String TAG = "CounterMainActivity";
	// the port number that this game will use when playing over the network
	public static final int PORT_NUMBER = 2234;

	/**
	 * Create the default configuration for this game:
	 * - one human player vs. one computer player
	 * - minimum of 1 player, maximum of 2
	 * - one kind of computer player and one kind of human player available
	 * 
	 * @return
	 * 		the new configuration object, representing the default configuration
	 */
	@Override
	public GameConfig createDefaultConfig() {
		
		// Define the allowed player types
		ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();
		
		// a human player player type (player type 0)
		playerTypes.add(new GamePlayerType("Local Human Player") {
			public GamePlayer createPlayer(String name) {
				return new CounterHumanPlayer(name);
			}});
		
		// a computer player type (player type 1)
		playerTypes.add(new GamePlayerType("Computer Player") {
			public GamePlayer createPlayer(String name) {
				return new CounterComputerPlayer1(name);
			}});
		
		// a computer player type (player type 2)
		playerTypes.add(new GamePlayerType("Computer Player (GUI)") {
			public GamePlayer createPlayer(String name) {
				return new CounterComputerPlayer2(name);
			}});

		// Create a game configuration class for Counter:
		// - player types as given above
		// - from 1 to 2 players
		// - name of game is "Counter Game"
		// - port number as defined above
		GameConfig defaultConfig = new GameConfig(playerTypes, 1, 2, "Counter Game",
				PORT_NUMBER);

		// Add the default players to the configuration
		defaultConfig.addPlayer("Human", 0); // player 1: a human player
		defaultConfig.addPlayer("Computer", 1); // player 2: a computer player
		
		// Set the default remote-player setup:
		// - player name: "Remote Player"
		// - IP code: (empty string)
		// - default player type: human player
		defaultConfig.setRemoteData("Remote Player", "", 0);
		
		// return the configuration
		return defaultConfig;
	}//createDefaultConfig

	/**
	 * createLocalGame
	 *
	 * Creates a new game that runs on the server tablet,
	 * @param gameState
	 * 				the gameState for this game or null for a new game
	 *
	 * @return a new, game-specific instance of a sub-class of the LocalGame
	 *         class.
	 */
	@Override
	public LocalGame createLocalGame(GameState gameState) {
		if(gameState == null) return new CounterLocalGame();
		return new CounterLocalGame((CounterState) gameState);
	}

	/**
	 * saveGame, adds this games prepend to the filename
	 *
	 * @param gameName
	 * 				Desired save name
	 * @return String representation of the save
	 */
	@Override
	public GameState saveGame(String gameName) {
		return super.saveGame(getGameString(gameName));
	}

	/**
	 * loadGame, adds this games prepend to the desire file to open and creates the game specific state
	 * @param gameName
	 * 				The file to open
	 * @return The loaded GameState
	 */
	@Override
	public GameState loadGame(String gameName){
		String appName = getGameString(gameName);
		super.loadGame(appName);
		Logger.log(TAG, "Loading: " + gameName);
		return (GameState) new CounterState((CounterState) Saving.readFromFile(appName, this.getApplicationContext()));
	}

}
