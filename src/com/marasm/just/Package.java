package com.marasm.just;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.List;
import org.apache.commons.io.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.marasm.just.Utils.execShell;
import static com.marasm.just.Utils.fileExists;
import static com.marasm.just.Utils.kDependencies;

/**
 * Created by Andrey Bogdanov on 15.11.2015.
 */

public class Package {

    final static String installPackageCMD = "cd %@ && ./install";
    final static String removePackageCMD = "cd %@ && ./remove";

    JSONObject info;
    JSONArray dependencies,inDependencies;
    String name;
    String author;
    String version;
    String path;

    boolean registerAsInstalled()
    {
        File src = new File(this.path);
        File dst = new File(Utils.subFolder(Utils.installedPath(), this.name));
        try{
            FileUtils.copyFile(src,dst);
            return true;
        }
        catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean unregisterFromInstalled()
    {
        File packageToUnregister = new File(Utils.installedPath(), this.name);
        return packageToUnregister.delete();
    }

    boolean install()
    {
        if(new File(Utils.subFolder(Utils.installedPath(), this.name)).exists())
        {
            System.out.println("Package " + this.name + " is already installed");
            return false;
        }
        System.out.println("Installing package " + this.name);
        boolean result = this.registerAsInstalled();
        // TODO: Dependencies installation
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

    static boolean remove()
    {
        return false;
    }

    static boolean update()
    {
        return false;
    }

    static List installed()
    {
        return null;
    }

    static Package packageWithRepo(Repository repo, String name)
    {
        return packageWithPath(Utils.subFolder(repo.path, name));
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
}
