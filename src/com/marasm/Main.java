package com.marasm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import com.marasm.Utils;

public class Main {

    static String just_logo = "" +
    "############################################\n" +
    "#    $$$$$\\                       $$\\      #\n" +
    "#    \\__$$ |                      $$ |     #\n" +
    "#       $$ |$$\\   $$\\  $$$$$$$\\ $$$$$$\\    #\n" +
    "#       $$ |$$ |  $$ |$$  _____|\\_$$  _|   #\n" +
    "# $$\\   $$ |$$ |  $$ |\\$$$$$$\\    $$ |     #\n" +
    "# $$ |  $$ |$$ |  $$ | \\____$$\\   $$ |$$\\  #\n" +
    "# \\$$$$$$  |\\$$$$$$  |$$$$$$$  |  \\$$$$  | #\n" +
    //"#  \\______\/  \\______\/ \\_______\/    \\____\/  #\n" +
    "############################################\n";

    static String[] cmds = {"exit", "help", "repos", "installed", "refresh", "search",
                            "install", "remove", "update", "addrepo", "rmrepo", "list"};
    static String returnCmds()
    {
        StringBuilder sb = new StringBuilder();
        for ( String command : cmds)
        {
            sb.append(command + " ");
        }
        return sb.toString();
    }

    static void help(String str)
    {
        if(str == null || str.length() == 0)
        {
            System.out.println("Actions:\n" + returnCmds());
            System.out.println("type help <action> for more info\n");
            return;
        }
        if(str.equals("exit")){System.out.println("usage: exit\nexit just package manager"); return;}
        if(str.equals("help")){System.out.println("usage: help [command]\nprint help message"); return;}
        if(str.equals("refresh")){System.out.println("usage: refresh\nrefresh packages info in all repositories"); return;}
        if(str.equals("search")){System.out.println("usage: search <package>\nsearch for package in all repos"); return;}
        if(str.equals("repos")){System.out.println("usage: repos\nlist all repos"); return;}
        if(str.equals("installed")){System.out.println("usage: installed\nlist all installed packages"); return;}
        if(str.equals("install")){System.out.println("usage: install <package>\ninstall package"); return;}
        if(str.equals("remove")){System.out.println("usage: remove <package>\nremove package"); return;}
        if(str.equals("update")){System.out.println("usage: update\nupdate all packages"); return;}
        if(str.equals("addrepo")){System.out.println("usage: addrepo <repo address>\nadd local or remote repository"); return;}
        if(str.equals("rmrepo")){System.out.println("usage: rmrepo <repo name>\nremove repository"); return;}
        if(str.equals("list")){System.out.println("usage: list [repo name]\nlist packages in repo or in all connected repos"); return;}
        else {
            System.out.println(str + " is not a just package manager command");
            return;
        }
    }

    static void execCMD(String cmd, String[] args)
    {
        if(cmd.equals("exit"))
        {
            System.exit(0);
        }
        if(cmd.equals("refresh"))
        {
            // TODO: Add after Repository class is implemented
            System.out.println("Command is not yet implemented");
            return;
        }
        if(cmd.equals("installed"))
        {
            // TODO: Add after Package class is implemented
            System.out.println("Command is not yet implemented");
            return;
        }
        if(cmd.equals("update"))
        {
            // TODO: Add after Package class is implemented
            System.out.println("Command is not yet implemented");
            return;
        }
        if(cmd.equals("repos"))
        {
            // TODO: Add after Repository class is implemented
            System.out.println("Command is not yet implemented");
            return;
        }
        if(cmd.equals("help"))
        {
            if(args.length == 0)
                help(null);
            else if(args.length == 1)
                help(args[0]);
            else;
        }
        if(cmd.equals("list"))
        {
            // TODO: Add after Repository class is implemented
            System.out.println("Command is not yet implemented");
            return;
        }
        if(cmd.equals("search"))
        {
            // TODO: Add after Repository class is implemented
            System.out.println("Command is not yet implemented");
            return;
        }
        if(cmd.equals("install"))
        {
            // TODO: Add after Repository and Package classes are implemented
            System.out.println("Command is not yet implemented");
            return;
        }
        if(cmd.equals("remove"))
        {
            // TODO: Add after Package class is implemented
            System.out.println("Command is not yet implemented");
            return;
        }
        if(cmd.equals("addrepo"))
        {
            // TODO: Add after Repository class is implemented
            System.out.println("Command is not yet implemented");
            return;
        }
        if(cmd.equals("remrepo"))
        {
            // TODO: Add after Repository class is implemented
            System.out.println("Command is not yet implemented");
            return;
        }
    }

    static void interactiveMode() throws IOException
    {
        System.out.println(just_logo);

        for(;;)
        {
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            try {
                String inputString = consoleInput.readLine();
                String[] args = inputString.split(" ");
                String[] cmdArgs = new String[args.length - 1];

                for(int i = 0;i < args.length - 1; i++)
                {
                    cmdArgs[i] = args[i+1];
                }
                execCMD(args[0], cmdArgs);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public static void main(String[] args) throws IOException {
	// write your code here
        String command;

        if(args.length > 0)
        {
            String[] cmdArgs = new String[args.length - 1];
            command = args[0];
            for(int i = 0;i < args.length - 1; i++)
            {
                cmdArgs[i] = args[i+1];
            }
            execCMD(command, cmdArgs);
        }
        else
        {
            interactiveMode();
        }
    }
}
