package com.marasm.just;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

import com.marasm.just.Utils;
import com.sun.corba.se.impl.javax.rmi.CORBA.Util;
import org.apache.commons.io.FilenameUtils;

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
        if(cmd.length()<=0){return;}
        boolean result=false;
        switch (cmd)
        {
            case "exit":
                result=true;
                System.exit(0);
                break;
            case "refresh":
                result = refreshCmd(args);
                break;
            case "installed":
                ArrayList<Package> installed=Package.installed();
                if(installed!=null && installed.size()>0)
                {
                    for(Package p:installed)
                    {
                        System.out.println(""+p);
                    }
                }else {
                    System.out.println("No packages installed!");
                }
                result=true;
                break;
            case "update":
                result = updateCmd(args);
                break;
            case "repos":
                result=true;
                ArrayList<String>repos=Utils.arrayOfFoldersInFolder(Utils.reposPath());
                if(repos.size()==0){System.out.println("No repos yet");break;}
                System.out.println("repos:");
                for(String r:repos)
                {
                    Repository re=Repository.repoNamed(Utils.lastPathComponent(r));
                    if(re!=null){System.out.println(re.name+" "+re.path);}
                    else{System.out.println("ERROR: invalid repository "+r);}
                }
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
                if(args.length<1){System.out.println("Too few arguments");break;}
                Repository r=Repository.repoNamed(args[0]);
                if(r==null){System.out.println("No repo named '"+args[0]+"'");break;}
                ArrayList<Package>allPackages=r.allPackages();
                for(Package p:allPackages)
                {
                    System.out.println(""+p);
                }
                result=true;
                break;
            case "search":
                if(args.length<1){System.out.println("Too few arguments");break;}
                for(String arg:args)
                {
                    ArrayList<Package>found=Repository.findPackages(arg);
                    if(found.size()<=0){
                        System.out.println(arg+": not found!");
                    }else{
                        System.out.println(arg+":");
                        for(Package p:found)
                        {
                            System.out.println("\t"+p);
                        }
                    }
                }
                result=true;
                break;
            case "install":
                if(args.length<1){System.out.println("Too few arguments");break;}
                result=true;
                for(String arg:args)
                {
                    ArrayList<Package> found=Repository.findPackages(arg);
                    if(found.size()<=0)
                    {
                        System.out.println("Package '"+arg+"' not found");
                        result=false;
                        break;
                    }
                    Package p=Package.selectPackage(found);
                    if(p!=null)
                    {
                        if(!p.install()){System.out.println("failed to install '"+arg+"'");result=false;break;}
                    }
                    else {System.out.println("failed to install '"+arg+"'");result=false;break;}
                }
                break;
            case "remove":
                System.out.println("Command is not yet implemented");
                if(args.length<1){System.out.println("Too few arguments");break;}
                int failsCount = 0;
                for(String name : args)
                {
                    Repository repo = Repository.repoNamed(name);
                    if(repo != null)
                    {
                        if(!repo.remove())
                        {
                            System.out.println("Repository "+name+" was not removed");
                            failsCount++;
                        }
                    }
                    else
                    {
                        System.out.println("Repository "+name+" not found");
                        failsCount++;
                    }
                }
                break;
            case "addrepo":
                if(args.length<1){System.out.println("Too few arguments");break;}
                result=Repository.add(args[0]);
                break;
            case "rmrepo":
                if(args.length<1){System.out.println("Too few arguments");break;}
                r = Repository.repoNamed(args[0]);
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

    static void interactiveMode()
    {
        System.out.println(just_logo);

        for(;;)
        {
            System.out.print("just> ");
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            try {
                String inputString = consoleInput.readLine();
                if(inputString==null){return;}
                String[] args = inputString.split(" ");
                if(args.length<=0){continue;}
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
    static boolean updateCmd(String[] args)
    {
        // TODO: implement
        return false;
    }
    static boolean refreshCmd(String[] args)
    {
        boolean result = false;
        if (args.length <= 0)
        {
            ArrayList<Repository> allrepos = Repository.all();
            if (allrepos.size() <= 0)
            {
                System.out.println("No repositories to refresh");
            }
            int failsCount = 0;
            for(Repository repo : allrepos)
            {
                if(!repo.refresh())
                {
                    System.out.println("Error while refreshing "+repo.name);
                    failsCount++;
                }
            }
            result = failsCount < allrepos.size();
        }
        else
        {
            int failsCount = 0;
            for(String repoName : args)
            {
                Repository repo = Repository.repoNamed(repoName);
                if(repo == null)
                {
                    System.out.println("Repository "+repoName+" was not found");
                }
                else
                {
                    if(!repo.refresh())
                    {
                        System.out.println("Error while refreshing "+repo.name);
                        failsCount++;
                    }
                }
            }
            result = failsCount < args.length;
        }
        return result;
    }

    public static void main(String[] args)
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
