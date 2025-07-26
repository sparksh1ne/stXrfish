package com.tseziy;

import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "welcome to " + Colors.YELLOW + "stXrfish" + Colors.RESET + "\ntype \"help\" for more info.");
        commandPrompt();
    }

    private static void commandPrompt() {
        Parser parser = new Parser();

        Scanner in = new Scanner(System.in);
        System.out.print(">>> ");
        String prompt = in.nextLine();

        if (prompt.equals("help")) {
            System.out.println(Colors.GREEN + "list of available commands" + Colors.RESET + ":\nexit\nhelp" + Colors.YELLOW + "\nconnect" + Colors.RESET + "\ndisconnect");
            commandPrompt();
        }
        else if (prompt.equals("connect")) {
            tip();
            parser.parseHTTP();
            commandPrompt();
        }
        else if (prompt.equals("disconnect")) {
            tip();
            parser.disconnectHTTP();
            commandPrompt();
        }
        else if (prompt.equals("exit")) {
            in.close();
            System.exit(0);
        }
        else {
            System.err.println("unknown command. type \"help\" for more info.");
            commandPrompt();
        }
    }

    private static void tip() {
        System.out.println(Colors.GREEN + "tip" + Colors.RESET + ": it's recommended to restart the session or reboot the system after successfully connecting/disconnecting.");
    }
}
