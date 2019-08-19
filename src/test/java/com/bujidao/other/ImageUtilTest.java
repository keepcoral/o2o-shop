package com.bujidao.other;

import com.bujidao.dto.ImageHolder;
import com.bujidao.util.ImageUtil;
import com.bujidao.util.PathUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageUtilTest {
    @Test
    public void  testAddImage() throws FileNotFoundException {
        String testFilePath="D:/IDM缓存/test.jpg";
        File file=new File(testFilePath);
        ImageHolder imageHolder=new ImageHolder(file.getName(),new FileInputStream(file));
        ImageUtil.generateNormalImg(imageHolder,"/");
    }
}
