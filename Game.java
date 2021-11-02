/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Ari Kaminski
 * @version Nov 1 2021
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office, kitchen, courtyard, jail;
        Room pantry, hallway, library, garden, medic, graveyard, crypt;
        
        // create the rooms
        outside = new Room("outside the main entrance of the university","","");
        theater = new Room("in a lecture theater","","");
        pub = new Room("in the campus pub","a pint of beer","light");
        lab = new Room("in a computing lab","","");
        office = new Room("in the security office","security baton","medium");
        kitchen = new Room("in the campus kitchen","","");
        courtyard = new Room("in the campus courtyard","","");
        pantry = new Room("in the pantry","","");
        hallway = new Room("in the main campus hallway","","");
        library = new Room("in the campus library","book of learning","heavy");
        garden = new Room("in the campus garden","flower","light");
        medic = new Room("in the campus medical office","medicine","light");
        graveyard = new Room("in the graveyard next to the campus, careful!","","");
        crypt = new Room("Oh no! You fell into the crypt. Game over","","");
        jail = new Room("in the campus jail. You need a key to get out","","");
        
        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", hallway);
        outside.setExit("west", pub);

        theater.setExit("west", outside);
        theater.setExit("east", library);
        theater.setExit("southwest", hallway);
        
        library.setExit("west", theater);
        
        medic.setExit("east", hallway);
        

        pub.setExit("east", outside);
        pub.setExit("west", kitchen);
        pub.setExit("southwest", courtyard);
        
        kitchen.setExit("south",courtyard);
        kitchen.setExit("east", pub);
        kitchen.setExit("down", pantry);
        
        pantry.setExit("up", kitchen);
        
        courtyard.setExit("north", kitchen);
        courtyard.setExit("northwest", pub);
        courtyard.setExit("south", garden);
        
        garden.setExit("north", courtyard);
        garden.setExit("northwest", hallway);
        garden.setExit("south", graveyard);
        
        graveyard.setExit("south", crypt);
        graveyard.setExit("north", garden);

        hallway.setExit("north", outside);
        hallway.setExit("east", office);
        hallway.setExit("northeast", theater);
        hallway.setExit("west", medic);
        hallway.setExit("southwest", garden);
        
        office.setExit("west", hallway);
        office.setExit("east", jail);

        currentRoom = outside;  // start game outside
    }
    /**
     * Look around and see what room you are in
     */
    
    private void look(){
        System.out.println(currentRoom.getLongDescription());
        
    }
    
    /**
     * Eat some food
     */
    private void eat(){
        System.out.println("You eat some food and your hunger is satisfied");
        
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;
                
            case LOOK:
                look();
                break;
                
            case EAT:
                eat();
                break;
            
                
            case QUIT:
                wantToQuit = quit(command);
                break;
                
            
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
