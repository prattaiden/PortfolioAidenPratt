package com.example.synthesizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;


import static org.junit.jupiter.api.Assertions.*;

class AudioClipTest {


    AudioClip testclip = new AudioClip();
    @Test
    public void TestsGetandSet() {
         //ArrayList<Integer> test = new ArrayList<>();

        testclip.setSample(1, 5);
        testclip.getSample(1);
        Assertions.assertEquals(5, 5);

        for( int i = Short.MIN_VALUE ; i < Short.MAX_VALUE; i++){
            int index = 0;
            testclip.setSample(index, i);
            int get = testclip.getSample(index);
            Assertions.assertEquals(get, i);
            index++;
            System.out.println(get);
        }

    }
}

