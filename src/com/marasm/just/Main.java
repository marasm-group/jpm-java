package com.marasm.just;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import com.marasm.just.Utils;

public class Main
{

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
            System.out.println(str + " is not a valid just package manager command");
            return;
        }
    }

    static void execCMD(String cmd, String[] args)
    {
        boolean result=false;
        switch (cmd)
        {
            case "exit":
                result=true;
                System.exit(0);
                break;
            case "refresh":
                // TODO: Add after Repository class is implemented
                System.out.println("Command is not yet implemented");
                break;
            case "installed":
                // TODO: Add after Package class is implemented
                System.out.println("Command is not yet implemented");
                break;
            case "update":
                // TODO: Add after Package class is implemented
                System.out.println("Command is not yet implemented");
                break;
            case "repos":
                // TODO: Add after Repository class is implemented
                System.out.println("Command is not yet implemented");
                break;
            case "help":
                result=true;
                if(args.length == 0)
                    help(null);
                else if(args.length == 1)
                    help(args[0]);
                else;
                break;
            case "list":
                // TODO: Add after Repository class is implemented
                System.out.println("Command is not yet implemented");
                break;
            case "search":
                // TODO: Add after Repository class is implemented
                System.out.println("Command is not yet implemented");
                break;
            case "install":
                // TODO: Add after Repository and Package classes are implemented
                System.out.println("Command is not yet implemented");
                break;
            case "remove":
                // TODO: Add after Package class is implemented
                System.out.println("Command is not yet implemented");
                if(args.length<1){System.out.println("Too few arguments");break;}
                break;
            case "addrepo":
                if(args.length<1){System.out.println("Too few arguments");break;}
                result=Repository.add(args[0]);
                break;
            case "rmrepo":
                if(args.length<1){System.out.println("Too few arguments");break;}
                Repository r=Repository.repoNamed(args[0]);
                if(r==null)
                {
                    System.out.print("No repo '"+args[0]+"'");
                    break;
                }
                result=r.remove();
                break;
            default:
                System.out.println("Unknown command '"+cmd+"'");
                break;
        }
        if(result){System.out.println("Done OK!");}
        else{System.out.println("Command failed!");}
    }

    static void interactiveMode() throws IOException
    {
        System.out.println(just_logo);

        for(;;)
        {
            System.out.print("just> ");
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

    public static void main(String[] args) throws IOException
    {
        Utils.createDirIfNeeded(Utils.just_home());
        Utils.createDirIfNeeded(Utils.registryPath());
        Utils.createDirIfNeeded(Utils.reposPath());
        Utils.createDirIfNeeded(Utils.installedPath());
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
