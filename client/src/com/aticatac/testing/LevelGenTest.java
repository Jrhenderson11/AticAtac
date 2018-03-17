package com.aticatac.testing;

import com.aticatac.world.utils.LevelGen;
import org.junit.Test;

import static org.junit.Assert.*;

public class LevelGenTest {

    @Test
    public void get() {
        if (!LevelGen.connected(LevelGen.get(100, 100))) {
            fail("Level Generated is not connected");
        }
    }
}