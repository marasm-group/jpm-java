package com.marasm.just;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.apache.commons.io.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.marasm.just.Utils.*;

/**
 * Created by Andrey Bogdanov on 15.11.2015.
 */

public class Package {

    final static String installPackageCMD = "cd %@ && ./install";
    final static String removePackageCMD = "cd %@ && ./remove";
    final static String updatePackageCMD = "cd %@ && ./update"; //Should be run only if update script exists, otherwise just run install once again

    JSONObject info;
    JSONArray dependencies,inDependencies;
    String name;
    String author;
    String version;
    String path;
    Repository repo=null;

    boolean registerAsInstalled()
    {
        File src = new File(this.path);
        File dst = new File(Utils.subFolder(Utils.installedPath(), this.name));
        try{
            FileUtils.copyDirectory(src,dst);
            return Utils.setExecutable(dst);
        }
        catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean unregisterFromInstalled()
    {
        File packageToUnregister = new File(Utils.installedPath(), this.name);
        try {
            FileUtils.deleteDirectory(packageToUnregister);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean install()
    {
        boolean res=install_internal();
        if(!res)
        {
            unregisterFromInstalled();
        }
        return res;
    }
    private boolean install_internal()
    {
        if(new File(Utils.subFolder(Utils.installedPath(), this.name)).exists())
        {
            System.out.println("Package " + this.name + " is already installed");
            return false;
        }
        File pkg = new File(this.path);
        System.out.println("Installing package " + this.name + " with size " + pkg.length());
        boolean result = this.registerAsInstalled();
        if(this.dependencies != null)
        {
            if(this.dependencies.length() > 0)
            {
                for(int i = 0; i < this.dependencies.length(); i++)
                {
                    String depName = this.dependencies.getString(i);
                    ArrayList<Package> foundPackages = Repository.findPackages(depName);
                    if(foundPackages != null)
                    {
                        if(foundPackages.size() > 0)
                        {
                            Package toInstall = this.selectPackage(foundPackages);
                            if(toInstall == null)
                            {
                                System.out.println("Failed to install dependency " + depName + " for " + this.name);
                                this.unregisterFromInstalled();
                                return false;
                            }
                            toInstall.install();
                        }
                    }
                    Package pack = installedPackage(depName);
                    if(pack != null)
                    {
                        // TODO: check this stuff
                        if(pack.inDependencies == null) {
                            pack.inDependencies = new JSONArray();
                        }
                        JSONObject obj = new JSONObject();
                        obj.put("name", this.name);
                        pack.inDependencies.put(obj.toString());
                        try {
                            FileWriter writer = new FileWriter(Utils.subFolder(Utils.installedPath(), depName +
                                    "/inDependencies.json"));
                            pack.inDependencies.write(writer);
                            writer.close();
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }
            }
        }
        if(!result) return false;
        String cmd = new String("cd " + Utils.subFolder(Utils.installedPath(), this.name) + " && ./install");
        if(Utils.execShell(cmd) != true)
        {
            this.unregisterFromInstalled();
            System.out.println("Installation of package " + this.name + " failed");
            return false;
        }
        return false;
    }

    static boolean remove()//TODO: implement
    {
        return false;
    }
    static boolean updateAvailable()//TODO: implement
    {
        return  false;
    }
    static boolean update()//TODO: implement
    {
        return false;
    }

    static ArrayList<Package> installed()
    {
        // TODO: check this. I highly doubt this will work.
        ArrayList<String> names = Utils.arrayOfFoldersInFolder(Utils.installedPath());
        ArrayList<Package> packages = new ArrayList<Package>();
        for(int i = 0; i < names.size(); i++)
        {
            String name = names.get(i);
            Package r = packageWithPath(Utils.subFolder(Utils.installedPath(), name));
            if(r != null)
                packages.add(r);
        }
        return packages;
    }

    static Package packageWithRepo(Repository repo, String name)
    {
        return packageWithPath(Utils.subFolder(repo.path, name));
    }

    static Package installedPackage(String name)
    {
        return packageWithPath(Utils.subFolder(Utils.installedPath(), name));
    }

    static Package packageWithPath(String path)
    {
        if(!fileExists(path)){return null;}
        Package p=new Package();
        p.path=path;
        p.name=Utils.lastPathComponent(path);
        try {
            String infoStr=FileUtils.readFileToString(new File(Utils.subFolder(p.path,"info.json")));
            p.info=new JSONObject(infoStr);
            p.version=p.info.getString(Utils.kVersion);
            try{p.dependencies=p.info.getJSONArray(Utils.kDependencies);}
            catch (JSONException e){}
            if(fileExists(Utils.subFolder(path,"inDependencies.json")))
            {
                try {
                    String inDepStr=FileUtils.readFileToString(new File(Utils.subFolder(p.path,"inDependencies.json")));
                    p.inDependencies=new JSONArray(inDepStr);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return p;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Package selectPackage(ArrayList<Package> packageList)
    {
        if(packageList.size() < 1)
            return null;
        if(packageList.size() == 1)
            return packageList.get(0);
        if(Utils.getSettings(Utils.kAutomatic).toString() == "automatic")
        {
            // TODO: discuss importance of this automatic installation type
            return packageList.get(0);
        }
        for(;;)
        {
            for(int i = 0; i < packageList.size(); i++)
            {
                System.out.println(i + ": " + packageList.get(i));
            }
            System.out.print("select package (0-" + (packageList.size() - 1) + ":");
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            try{
                String inputString = consoleInput.readLine();
                for(;inputString != null;)
                {
                    System.out.print("select package (0-" + (packageList.size() - 1) + ":");
                    inputString = consoleInput.readLine();
                    int chosenIndex = Integer.parseInt(inputString);
                    if(chosenIndex >= 0 && chosenIndex < packageList.size())
                    {
                        return packageList.get(chosenIndex);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            catch(NumberFormatException nfe){
                nfe.printStackTrace();
                return null;
            }
        }
    }
    public String toString()
    {
        String res=name+" "+version;
        if(repo!=null){res+=" ("+repo.name+")";}
        return res;
    }
}
