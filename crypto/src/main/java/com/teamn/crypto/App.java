package com.teamn.crypto;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import static java.lang.System.out;

/**
 * Hello world!
 *
 */
public class App implements Serializable
{
    private Game game;
    private Player player;
    private ArrayList<Player> players;
    private String playersFile;
    private Scanner scanner;
    private boolean allowGuesses;
    private LeaderBoard leaderBoard;

    public App(){
        playersFile = "players.txt";
        players = loadPlayers();
        leaderBoard = new LeaderBoard(10, players);
        scanner = new Scanner(System.in);
        allowGuesses = true;
    }

    /**
     * Runs the program and contains the main program flow logic.
     */
    public void run(){
        printWelcomeMessage();
        Action choice = Action.GENERATE_NEW_CRYPTOGRAM;
        userLogin(); // so we then have a player

        while(choice != Action.EXIT_GAME){
            switch (choice){
                case GENERATE_NEW_CRYPTOGRAM:
                    newgame();
                    allowGuesses = true;
                    break;
                case ENTER_LETTER:
                    enterLetter();
                    break;
                case UNDO_LETTER:
                    undoLetter();
                    break;
                case SAVE_GAME:
                    saveCurrentGame();
                    break;
                case VIEW_LEADERBOARD:
                    viewLeaderBoard();
                    break;
                case SHOW_FREQUENCIES:
                    printFrequencies();
                    break;
                case HINT_REQUEST:
                    game.giveHint();
                    break;
                case SHOW_SOLUTION:
                    showSolution();
                    allowGuesses = false;
            }

            checkIfGameHasFinished();
            game.printGameState();
            printMenu(allowGuesses);
            choice = getUserChoice(allowGuesses);
        }

        savePlayers();
        printFarewellMessage();
    }


    /**
     * Prints cryptogram solution
     */
    private void showSolution() {
        if(this.game != null && this.game.getCryptogram() != null) {
            out.println("Cryptogram solution: " + this.game.getCryptogram().getPhrase());
        }
        else {
            out.println("Cryptogram doesn't exist");
        }
    }

    /**
     * Prints the leaderboard to the screen
     */
    private void viewLeaderBoard() {
        leaderBoard.update();
        out.println(leaderBoard.printableLeaderBoard());
    }

    /*
     * Prints the letter frequencies for the current game
     */
    private void printFrequencies() {
        if (this.game == null || this.game.getCryptogram() == null) return;

        String cypherText;
        Map<String, String> internalFrequencies;
        if (this.game.cryptogramType(NumberCryptogram.class)) {
            cypherText = String.join(",", this.game.getCryptogram().getCypherText());
            internalFrequencies = FrequencyManager.getInternalFrequenciesForNumbersString(cypherText.replace(", ", ""));
        } else {
            cypherText = String.join(" ", this.game.getCryptogram().getCypherText());
            internalFrequencies = FrequencyManager.getInternalFrequenciesForCharactersString(cypherText);
        }

        Map<String, String> englishFrequencies = FrequencyManager.getEnglishFrequencies();

        System.out.println("Cryptogram letters frequencies:");
        StringBuilder sb = new StringBuilder();
        for (String key: internalFrequencies.keySet()) {
            sb.append(key).append(":").append(internalFrequencies.get(key)).append("%").append("\n");
        } System.out.println(sb.toString());

        System.out.println("English letters frequencies:");
        sb = new StringBuilder();
        for (String key: englishFrequencies.keySet()) {
            sb.append(key.toUpperCase()).append(":").append(englishFrequencies.get(key)).append("%").append("\n");
        } System.out.println(sb.toString());

     }

    /**
     * Takes a name from the user and loads the approiprate Player object
     */
    private void userLogin() {
        String username = getUserName();

        for(Player p: players){
            if(p.getUsername().equals(username)){
                player = p;
                out.println("Loaded player: " + username);
                return;
            }
        }

        out.println("Could not find an existing player called " + username + "\nCreating new player: " + username);
        player = new Player(username);
        players.add(player);
    }

    /**
     * Check if the game has finished and if the users final set of
     * guesses are all correct or not. Print the appropriate messages
     * if so
     */
    private void checkIfGameHasFinished() {
        if(game.hasGuessesForAllCypherCharacters()){
            if(game.userGuessesAreCorrect()){
                printSuccess();
                allowGuesses = false;
            } else {
                printFailure();
            }
        }
    }

    /**
     * Prints a welcome message
     */
    private void printWelcomeMessage(){
        out.println("\n################################");
        out.println("###### Group N Cryptogram ######");
        out.println("################################\n");
    }

    /**
     * Prints a farewell message
     */
    private void printFarewellMessage(){
        out.println("\n################################");
        out.println("############ Goodbye ###########");
        out.println("################################\n");
    }

    /**
     * Prints the menu
     */
    private void printMenu(boolean isAllowedToMakeGuesses){
        out.println("\nWhat would you like to do?");
        if(isAllowedToMakeGuesses) {
            out.println("\t1) Enter a letter");
            out.println("\t2) Undo a letter");
            out.println("\t3) Save game");
            out.println("\t4) Get a hint");
        }
        out.println("\t5) Generate a new cryptogram (stop playing current game)");
        out.println("\t6) Exit the program");
        out.println("\t7) View leaderboard");
        out.println("\t8) Show letter frequencies");
        out.println("\t9) Show solution (give up!)");
    }

    /**
     * Save the current game to a file. If a save file already exists,
     * ask the user if they would like to overwrite. If they don't
     * want to overwrite they don't get to save.
     */
    public void saveCurrentGame() {
        String filename = game.getCurrentPlayer().getUsername() + ".txt";
        File filechecker = new File(filename);
        if (filechecker.exists()){
            out.println("An existing game file has been found for this player, do you " +
                    "wish to overwrite it?");
            while (true){
                String answer = scanner.nextLine();
                if (answer.split("\\s+")[0].equalsIgnoreCase("yes")) {
                    try {
                        saveObject(filename, game);
                        out.println("Overwrite successful!");
                        return;
                    } catch (IOException e) {
                        out.println("A file overwrite failure occurred");
                        e.printStackTrace();//Used for debugging and if it breaks
                    }
                }
                else if (answer.equalsIgnoreCase("no")){
                    out.println("File not saved.");
                    return;
                }
                else {
                    out.println("Invalid command, please say 'yes' or 'no' to " +
                            "overwriting the existing player file");
                }
            }
        } else{
            out.println("No previous game file detected, saving game");
            try {
                saveObject(filename, game);
                out.println("Game saved successfully!");
            } catch (IOException e) {
                out.println("A file creation failure occured");
                e.printStackTrace();//Used for debugging and if it breaks
            }
        }
    }

    /**
     * Serialize an object and save to file
     *
     * @param filename the name of the file
     * @param obj the object to serialize and save
     * @throws IOException
     */
    private void saveObject(String filename, Object obj) throws IOException {
        FileOutputStream file_out = new FileOutputStream(filename);
        ObjectOutputStream obj_out = new ObjectOutputStream(file_out);
        obj_out.writeObject(obj);
        obj_out.close();
        file_out.close();
    }

    /**
     * Loads a previously saved game for a user
     *
     * @param username the user who's game is to be loaded
     * @return the game
     */
    public Game loadGame(String username) {
        Game loadedGame;

        try {
            String file = username + ".txt";
            FileInputStream file_in = new FileInputStream(file);
            ObjectInputStream obj_in = new ObjectInputStream(file_in);
            loadedGame = (Game)obj_in.readObject();
            updatePlayer(loadedGame);
            obj_in.close();
            file_in.close();
        }catch (IOException | ClassNotFoundException e) {
            out.print("File hasn't been found or does not exist, please try again\n");
            return null;
        }

        return loadedGame;
    }

    /**
     * Takes a loaded game which contains a player object and inserts the correct and
     * up to date player object.
     *
     * @param loadedGame
     */
    private void updatePlayer(Game loadedGame) {
        String name = loadedGame.getCurrentPlayer().getUsername();

        for(Player p: players){
            if(p.getUsername().equals(name)){
                loadedGame.setCurrentPlayer(player);
            }
        }
    }

    /**
     * Prints a success message
     */
    private void printSuccess() {
        out.println("\n****** You have successfully completed the cryptogram! ******\n");
    }

    /**
     * Prints a failure message
     */
    private void printFailure() {
        out.println("\n****** Your cryptogram solution is incorrect! ******\n");
    }

    /**
     * Asks the game object to generate a newgame with type chosen
     * by player
     */
    private void newgame(){
        String input;
        char firstChar;
        allowGuesses = true;
        String filename = player.getUsername() + ".txt";
        File file = new File(filename);
        if(file.exists()) {
            out.print("You were part of the way through a game when you last played. Would you like to continue this game?\nType 'yes' or 'no' ");
            out.flush();
            do {
                input = scanner.nextLine();
                if (input.equalsIgnoreCase("yes")) {
                    game = loadGame(player.getUsername());
                    if(game != null) {
                        file.delete();
                        return;
                    } else {
                        out.println("Game file is corrupt, cannot load the game. Starting a new game instead ...");
                        break;
                    }
                } else if (input.equalsIgnoreCase("no")) {
                    break;
                } else {
                    out.println("Invalid command, please type 'yes' or 'no'");
                }
            } while (true);
        }

        game = new Game(player, "sentences");

        do {
            out.println("Would you like to play a letter cryptogram or a number cryptogram? (Type either L or N)");
            input = scanner.next();
            if(scanner.hasNextLine())
                scanner.nextLine();
            firstChar=Character.toUpperCase(input.charAt(0));
            if(firstChar != 'L' && firstChar != 'N')
                out.println("Input not recognised, must enter either L or N");
        } while(firstChar != 'L' && firstChar != 'N');
        if(firstChar=='L')
            game.generateCryptogram(Game.GameType.LETTER);
        else
            game.generateCryptogram(Game.GameType.NUMBER);

    }

    /**
     * Asks the user to enter his name
     * @return user's entered username
     */
    private String getUserName() {
        String username = null;
        do {
            out.print("Enter your username: ");
            System.out.flush();
            String line = scanner.nextLine();
            String[] tokens = line.split("\\s+");
            if(tokens.length != 1 || tokens[0].equals("")){
                out.println("Sorry, your username must be exactly one word and contain no whitespace.");
            } else {
                username = tokens[0];
            }
        } while(username == null);

        return username;
    }

    /**
     * Get the users menu choice
     * @return the action chose from the menu
     */
    private Action getUserChoice(boolean isAllowedToMakeGuess){
        int minInput = isAllowedToMakeGuess ? 1 : 5;

        int input = 0;
        Action choice = null;
        while(choice == null){
            out.println("\nChoice: ");
            try {
                input = scanner.nextInt();
                if(scanner.hasNextLine())
                    scanner.nextLine();
            } catch (InputMismatchException e){
                choice = null;
                scanner.next();
                if(scanner.hasNextLine())
                    scanner.nextLine();
            }
            if(input >= minInput && input <= Action.values().length)
                choice = Action.values()[input-1];
            else
                out.println("invalid option chosen - choose an option between " + minInput + " and " + Action.values().length);
        }
        out.println();
        return choice;
    }

    /**
     * Asks the user to guess a letter for the cryptogram
     */
    private void enterLetter(){

        String cypherchar;
        do {
            cypherchar = takeCypherCharInput("What cypher character are you making a guess for?");
            if (!game.cryptogramContainsCharacter(cypherchar)) {
                out.println("The cryptogram does not contain " + cypherchar + ". Please try another.");
                continue;
            } break;

        } while(true);

        char plainchar = takeCharacterInput("What character do you think it should be?");

        if(!game.enterLetter(cypherchar, plainchar))
            out.println("\nYou have already entered a guess for that character. " +
                    "You will need to undo this before you can guess again.\n");
    }

    /**
     * Asks a user to enter a letter for a cypher character whose mapping he wants removed
     * If mapping is present, the entered choice gets removed from the cryptogram mapping
     */
    private void undoLetter(){
        String cypherchar;
        do {
            cypherchar = takeCypherCharInput("Which cypher character do you want to undo?");

            if(!game.cryptogramContainsCharacter(cypherchar)) {
                out.println("The cryptogram does not contain this cypher character please pick another.");
            }

        } while(!game.cryptogramContainsCharacter(cypherchar));

        if(game.undoLetter(cypherchar)){
            out.println("Removed player choice for " + cypherchar);
        } else {
            System.out.println("There has not been a guess made for " + cypherchar + " so there's nothing to undo.");
        }
    }

    /**
     * Take input from the user and check it is a valid cypher character
     * for the current cryptogram
     * @param prompt to be displayed when asking for input
     * @return the validated cypher character as a String
     */
    private String takeCypherCharInput(String prompt){
        String cyphercharinput = null;
        while(!game.isValidCypherCharacter(cyphercharinput)){
            out.println(prompt);
            cyphercharinput = scanner.next().toUpperCase();
            if(scanner.hasNextLine())
                scanner.nextLine();
            if(!game.isValidCypherCharacter(cyphercharinput))
                out.println("\""+ cyphercharinput + "\" is not in the cyphers alphabet, try again!");
        }
        assert cyphercharinput != null;
        return cyphercharinput;
    }

    /**
     * Take input from user and check it is a valid alphabetic character
     * @param prompt to be displayed when asking for input
     * @return the validated character
     */
    private char takeCharacterInput(String prompt){
        String cyphercharinput = null;
        while(notValidChar(cyphercharinput)){
            out.println(prompt);
            cyphercharinput = scanner.next();
            if(scanner.hasNextLine())
                scanner.nextLine();
            if(notValidChar(cyphercharinput))
                out.println("You must enter a single alphabetic character, try again!");
        }
        return Character.toUpperCase(cyphercharinput.charAt(0));
    }

    /**
     * @param charString
     * @return true if the string IS NOT an alphabetic character
     */
    private boolean notValidChar(String charString){
        if(charString != null && charString.length()==1) {
            char c = charString.charAt(0);
            return !Character.isAlphabetic(c);
        }
        return true;
    }

    /**
     * Load all players from file
     * @return list of all playeres
     */
    public ArrayList<Player> loadPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        try {
            FileInputStream fin = new FileInputStream(playersFile);
            ObjectInputStream oin = new ObjectInputStream(fin);

            players = (ArrayList<Player>) oin.readObject();

            System.out.println("Players within the DB: ");
            for(Player player : players) {
                System.out.println(player.toString());  //print all loaded players
            }

            oin.close();
            fin.close();
        } catch (FileNotFoundException e) {
            System.out.println("Players record file does not exist. Creating new one.");
        } catch (IOException e) {
            System.out.println("The file containing player information is corrupted. Previous players not loaded.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return players;
    }

    /**
     * save the current player into a players file
     */
    public void savePlayers() {
        try {
            saveObject(playersFile, players);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Possible menu actions
     */
    public enum Action {
        ENTER_LETTER, UNDO_LETTER, SAVE_GAME, HINT_REQUEST,
        GENERATE_NEW_CRYPTOGRAM, EXIT_GAME, VIEW_LEADERBOARD,
        SHOW_FREQUENCIES, SHOW_SOLUTION
    }

    public static void main( String[] args )
    {
        new App().run();
    }

}
