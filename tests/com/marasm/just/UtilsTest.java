package com.marasm.just;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Andrey on 04.11.2015.
 */
public class UtilsTest {

    @Test
    public void testGetSettings() throws Exception {

    }

    @Test
    public void testSetSettings() throws Exception {

    }

    @Test
    public void testJust_home() throws Exception {

    }

    @Test
    public void testReposPath() throws Exception {

    }

    @Test
    public void testRegistryPath() throws Exception {

    }

    @Test
    public void testInstalledPath() throws Exception {

    }

    @Test
    public void testCreateDirIfNeeded() throws Exception {

    }

    @Test
    public void testSubFolder() throws Exception {
        assertEquals(Utils.subFolder("folder","subfolder"), "folder/subfolder");
    }

    @Test
    public void testFileExists() throws Exception {

    }

    @Test
    public void testExecShell() throws Exception {
        assertTrue(Utils.execShell("ls"));
        assertTrue(Utils.execShell("pwd"));
        if(System.getProperty("os.name").contains("Windows"))
            assertTrue(Utils.execShell("echo $JAVA_HOME"));
        else
            assertTrue(Utils.execShell("echo %JAVA_HOME%"));
    }
}