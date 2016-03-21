package com.marasm.just;
import javax.annotation.processing.FilerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Object;
import java.lang.reflect.Array;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.io.File;

/**
 * Created by Andrey Bogdanov on 31.10.15.
 */
public class Utils {

    final static String kName = "name";
    final static String kAuthor = "author";
    final static String kVersion = "version";
    final static String kDependencies = "dependencies";
    final static String kIsLibrary = "isLibrary";
    final static String kAutomatic = "automatic";

    static Hashtable<String, Object> just_settings;

    static Object getSettings (Object key)
    {
        if(just_settings == null)
        {
            return null;
        }
        return just_settings.get(key);
    }

    static void SetSettings(Hashtable<String, Object> set)
    {
        just_settings = set;
    }

    public static String mainJarLocation()
    {
        return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsoluteFile().getParent().replaceAll("\\%20"," ");
    }
    static String just_home()
    {
        return mainJarLocation();
    }

    static String reposPath()
    {
        return just_home().concat("/repositories");
    }

    static String registryPath()
    {
        return just_home().concat("/registry");
    }

    static String installedPath()
    {
        return just_home().concat("/installed");
    }

    static boolean createDirIfNeeded(String directory)
    {
        File newDir = new File(directory);

        if(!newDir.exists())
        {
            try{
                newDir.mkdir();
                //System.out.println("Directory created");
            }
            catch(SecurityException se)
            {
                //System.out.println(se.toString());
                return false;
            }
        }
        return true;
    }

    static String subFolder(String folder, String subfolder)
    {
        return new String(folder + "/" + subfolder);
    }

    static boolean fileExists(String path)
    {
        return new File(path).exists();
    }

    static boolean execShell(String cmd)
    {
        // TODO: Windows cmd support. Right now it doesn't work.
        StringBuffer output =  new StringBuffer();
        String[] cmdArr = new String[]{"/bin/bash", "-c",cmd};
        Process process;
        try {
            process = Runtime.getRuntime().exec(cmdArr);
            process.waitFor();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = "";
            while((line = bReader.readLine()) != null)
            {
                output.append(line + "\n");
            }
        }
        catch (IOException fe)
        {
            System.out.println(fe.toString());
            return false;
        }
        catch (InterruptedException ie)
        {
            System.out.println(ie.toString());
            return false;
        }
        System.out.println(output.toString());
        return true;
    }

    static ArrayList<String> arrayOfFoldersInFolder(String folder)
    {
        ArrayList<String> contents = new ArrayList<String>();

        File dir = new File(folder);
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(dir.listFiles()));

        for(File tmp : files)
        {
            if(tmp.isDirectory() && tmp.getName() != ".git")
                contents.add(tmp.getAbsolutePath());
        }

        return contents;
    }
    static String substitutePath(String cmd,String path)
    {
        return cmd.replaceFirst("\\%\\@",path);
    }
    static String substitutePathAndRemote(String cmd,String path,String remote)
    {
        return cmd.replaceFirst("\\%\\@",path).replaceFirst("\\%\\@",remote);
    }
    static String lastPathComponent(String path)
    {
        if (null != path && path.length() > 0 )
        {
            int endIndex = path.lastIndexOf("/");
            if(endIndex==path.length())
            {
                return lastPathComponent(path.substring(0,endIndex));
            }
            if (endIndex != -1)
            {
                return path.substring(endIndex+1, path.length());
            }
        }
        return "";
    }
}
