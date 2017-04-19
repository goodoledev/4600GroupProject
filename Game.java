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
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.08
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    public int fuelBar; 
    
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
       Room entrance,left1,left2,left3,portalLeft, mid1, mid2, mid3, mid4, right1, right2, right3, farRight1, farRight2, exit;
              
        // create the rooms
        entrance = new Room("You find yourself at the entrance to to a poorly lit cavern.","Enter the dungeon, true Adventure awaits!");
        left1 = new Room("A roaring blaze blocks your path!","The flame illuminates the path from which you came.");
        left2 = new Room("A skeleton lies on the floor, fear grips you. Tread carefully.", "You notice the skeleton seems like he was trying to travel North...");
        left3 = new Room("Beautiful mounds of gold flash before your eyes! You step closer and then everything vanishes...","Fools have perished here, you can feel it. But something urges you to press on...");
        portalLeft = new Room("You hear a strange humming noise and what looks to be a portal. Where does it lead?","A voice tells you, 'do not confuse the fear of the unknown with the fear of change...'");
        mid1 = new Room("Darkness washes over you.","Do not fear, moving forward will lead to progress.");
        mid2 = new Room("BATS rush in and swarm you! 2 doors are outlined in the distance, make a decision quickly!","Strange noises flood in from the West.");
        mid3 = new Room("Eerie silence deafens you... ","You notice a strange mural of a Sun to the east. When you move closer to the mural sounds of water come from the North");
        mid4 = new Room("Water flows in from the ceiling into a small underground creek.","You seem to have found a dead end. What went wrong? Try retracing your steps");
        
        right1 = new Room("There's a sad subtle crying noise nearby...","Do you wish to follow the noise? Choose wisely...");
        right2 = new Room("You see a trail of Egyptian Scarab Beetles swarming over the remains of an adventurer on the east side of the wall","The Egyptian considered these beetles lucky, do you wish to follow the trail?");
        right3 = new Room("An intense cold blankets the room, you feel your breath catch in your chest. In the corner a dark figure catches your eye...","Fear seems to empower the figure, but faint barking of what sounds like an old friend echo from the southern side of the room...");
        
        farRight1 = new Room("There's a little girl crying in the corner of the room...","There's blood flowing down her eyes as she yells TURN BACK!!!");
        farRight2 = new Room("You find yourself in a place dark, but you feel invigorated. Nothing can stop you now.",
        "Faint markings on the north wall depict an image of a phoenix rising from the ashes. What does it mean?");
        
        exit = new Room("Light shines from the ceiling illuminating a stone stair case in front of you. Painted on the walls are images that you cannot describe.",
        "You have been in darkness far too long the light is so inviting");
        
        // initialise room exits
        entrance.setExit("north", mid1);
        
        
        mid1.setExit("north", mid2);
        mid1.setExit("east", right1);
        
        mid2.setExit("north", mid3);
        mid2.setExit("west", left2);
        mid2.setExit("south",mid1);
        
        mid3.setExit("north", mid4);
        mid3.setExit("south",mid2);
        mid3.setExit("east",right3);
        
        mid4.setExit("south", mid3);
        

        left1.setExit("north", left2);
        

        left2.setExit("east", mid2);
        left2.setExit("north", left3);
        left2.setExit("south",left1);
        
        left3.setExit("south", left2);
        left3.setExit("west", portalLeft);
        
        portalLeft.setExit("portal", farRight2);
        portalLeft.setExit("east", left3);
        
        
        right1.setExit("west", mid1);
        right1.setExit("east", farRight1);
        
        right2.setExit("north", right3);
        right2.setExit("east", farRight2);
        
        right3.setExit("west", mid3);
        right3.setExit("south", right2);
        
        farRight1.setExit("west", right1);
        
        farRight2.setExit("west", right2);
        farRight2.setExit("north", exit);


        exit.setExit("north", exit);
        currentRoom = entrance;  // start game at entrance
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        fuelBar = 110;
        printWelcome();
        
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (!finished) {
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
        System.out.println("Welcome to the DEATH OF THE CATACOMBS");
        System.out.println("Find your way out. You are given a lamp to travel to different parts of the Catacombs.");
        System.out.println("For every room you enter, you use a bar of your fuel level. You must escape before your fuel bar is exhausted.");
        System.out.println("Each room contains clues to help you on your journey. Type 'inspect' to gain information about your surrounding."); 
        System.out.println("Listen to the hints carefully and choose your faith wisely!");
        System.out.println("One wrong turn can lead you to death!");
        System.out.println("Your main objective is to venture off and find the exit. GOOD LUCK!!");
        System.out.println("Type in 'go' along with the direction you want to go to play, 'help' for a list of commands,");
        System.out.println("or 'quit' to end the game...");
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

        if(command.isUnknown()) 
        {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord().toLowerCase();
        if (commandWord.equals("help")) 
        {
            printHelp();
        }
        else if(commandWord.equals("inspect")){
            System.out.println(currentRoom.getHint());
        }
        else if (commandWord.equals("go")) 
        {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) 
        {
            wantToQuit = quit(command);
        }
        // else command not recognised.
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
        System.out.println("You are a eager thrill seeker and somehow ended up in the Death of Catacombs. You need to find your way out.");
        System.out.println();
        System.out.println("Your command words are: ");
        parser.showCommands();
        System.out.println();
        System.out.println("Command words are limited based on your room location.");
        System.out.println("Best of Luck!!!");
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Please enter north or east?");

        }  

        String direction = command.getSecondWord().toLowerCase();
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);
        
        
        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
            fuelBar=fuelBar - 10;
            System.out.println("Fuel Bar Level: " + fuelBar);
            if (fuelBar == 0)
            {
                System.out.println("Your lamp has run out of fuel and the room is pitch dark!!!");
                System.out.println("There's no hope for you to find the exit and you've been consumed by the dead...");
                System.out.println("GAME OVER!!!!!");
                System.exit(69);
            }
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
