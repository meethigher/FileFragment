package top.meethigher.utils;

import org.junit.Test;


public class FileFragmentTest {

    private final FileFragment fileFragment = new FileFragment();

    @Test
    public void split() {
        fileFragment.splitFile("E:/testFileFragment/20231014_210729.zip", 2048 * 1024 * 1024L);
//        fileFragment.splitFile("E:/testFileFragment/CFW-0.20.30-X64.zip", 20 * 1024 * 1024);
    }


    @Test
    public void merge() {
        fileFragment.mergeFiles("E:/testFileFragment/", "test.zip");
    }

}