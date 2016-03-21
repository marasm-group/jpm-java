package com.marasm.just;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by andreybogdanov on 21.03.16.
 */
public class MainTest {

    static String returnCmdsResult =
            "exit help repos installed refresh search install remove update addrepo rmrepo list ";

    @Test
    public void returnCmds() throws Exception {
        assertEquals(Main.returnCmds(), returnCmdsResult);
    }

    @Test
    public void help() throws Exception {

    }

    @Test
    public void execCMD() throws Exception {

    }

    @Test
    public void interactiveMode() throws Exception {

    }

    @Test
    public void main() throws Exception {

    }
}