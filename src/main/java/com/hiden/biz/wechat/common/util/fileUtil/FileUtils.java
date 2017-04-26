//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hiden.biz.wechat.common.util.fileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    public FileUtils() {
    }

    public static File createTmpFile(InputStream inputStream, String name, String ext) throws IOException {
        FileOutputStream fos = null;

        try {
            File tmpFile = File.createTempFile(name, '.' + ext);
            tmpFile.deleteOnExit();
            fos = new FileOutputStream(tmpFile);
            boolean read = false;
            byte[] bytes = new byte[102400];

            int read1;
            while((read1 = inputStream.read(bytes)) != -1) {
                fos.write(bytes, 0, read1);
            }

            fos.flush();
            File var7 = tmpFile;
            return var7;
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException var17) {
                    ;
                }
            }

            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException var16) {
                    ;
                }
            }

        }
    }
}
