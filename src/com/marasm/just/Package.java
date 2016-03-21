package com.marasm.just;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.List;
import org.apache.commons.io.*;
import org.json.JSONObject;

import static com.marasm.just.Utils.execShell;

/**
 * Created by Andrey Bogdanov on 15.11.2015.
 */

public class Package {

    final static String installPackageCMD = "cd %@ && ./install";
    final static String removePackageCMD = "cd %@ && ./remove";

    JSONObject info;
    List dependencies;
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

    static Package packageWithRepo(String repo, String name)
    {
        return null;
        //return packageWithPath(Utils.subFolder(r.path, name));
    }
}
