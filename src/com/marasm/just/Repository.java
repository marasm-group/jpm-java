package com.marasm.just;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import static com.marasm.just.Utils.execShell;
import static com.marasm.just.Utils.substitutePath;

/**
 * Created by Sergey Rump on 15.11.2015.
 */
public class Repository
{
    static final String addRepoCMD="cd \"%@\" && git clone \"%@\" --depth=1";
    static final String refreshRepoCMD="cd \"%@\" && git fetch --all --depth=1 && git reset --hard FETCH_HEAD && git pull --depth=1";

    JSONObject info;
    String name;
    String path;

    boolean refresh()
    {
        System.out.println("refreshing repository '"+name+"'");
        String cmd=substitutePath(refreshRepoCMD,path);
        return execShell(cmd);
    }
    boolean remove()
    {
        System.out.println("removing repository '"+name+"'");
        try {
            FileUtils.deleteDirectory(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    Package findPackage(String name){return null;}
    ArrayList<Package> findPackages(String Name){return new ArrayList<Package>();}
    ArrayList<Package> allPackages(){return new ArrayList<Package>();}
    static Repository repoNamed(String name)
    {
        if(!exists(name)){return null;}
        Repository r=new Repository();
        r.name=name;
        r.path=pathForRepoNamed(name);
        try {
            String infoStr=FileUtils.readFileToString(new File(r.path));
            r.info=new JSONObject(infoStr);
            return r;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    ArrayList<Repository> all(){return new ArrayList<Repository>();}
    static boolean exists(String name)
    {
        File f=new File(pathForRepoNamed(name));
        return f.exists();
    }
    static boolean add(String remote)
    {
        System.out.println("adding repository '"+remote+"'");
        String cmd=Utils.substitutePathAndRemote(addRepoCMD,Utils.reposPath(),remote);
        return execShell(cmd);
    }
    static String pathForRepoNamed(String name){return Utils.subFolder(Utils.reposPath(),name);}
}
