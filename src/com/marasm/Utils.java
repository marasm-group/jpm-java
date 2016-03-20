package com.marasm;
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
 * Created by abogdanov on 31.10.15.
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

    static String just_home()
    {
        return System.getProperty("user.dir");
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

        Process process;
        try {
            process = Runtime.getRuntime().exec(cmd);
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
}
