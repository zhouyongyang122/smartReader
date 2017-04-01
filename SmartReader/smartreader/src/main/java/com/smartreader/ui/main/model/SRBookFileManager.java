package com.smartreader.ui.main.model;

import android.util.Log;

import com.smartreader.SRApplication;
import com.smartreader.utils.ZYFileUtils;
import com.smartreader.utils.ZYLog;
import com.smartreader.utils.ZYUrlUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by ZY on 17/3/31.
 */

public class SRBookFileManager {

    private static SRBookFileManager instance;

    private SRBookFileManager() {

    }

    public static SRBookFileManager getInstance() {
        if (instance == null) {
            instance = new SRBookFileManager();
        }
        return instance;
    }

    public static void unZip(String zipPath, String unZipPath) throws IOException {
        OutputStream os = null;
        InputStream is = null;
        ZipFile zfile = new ZipFile(zipPath);
        Enumeration zList = zfile.entries();
        File unZipFileDir = new File(unZipPath);
        ZYFileUtils.delete(unZipFileDir);
        if (!unZipFileDir.exists()) {
            unZipFileDir.mkdirs();
        }
        byte[] buf = new byte[10240];
        ZipEntry ze = null;
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
//            ZYLog.e("Unzip: ", "=" + ze.getName());
            String outPath = unZipPath + ze.getName();
            File outFile = new File(outPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            os = new BufferedOutputStream(new FileOutputStream(outFile));
            is = new BufferedInputStream(zfile.getInputStream(ze));
            int readLen = 0;
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                os.write(buf, 0, readLen);
            }
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (Exception e) {

            }
        }
        if (zfile != null) {
            zfile.close();
        }

        //删除zip文件
        ZYFileUtils.delete(zipPath);

    }

    public static String getBookBgPathByPath(String bookPath) {
        return bookPath + "title.jpg";
    }

    public static String getBookBgById(String book_id) {
        return getBookPath(book_id) + "title.jpg";
    }

    public static String getBookJsonPathByPath(String bookPath) {
        return bookPath + "book.json";
    }

    public static String getBookJsonPathById(String book_id) {
        return getBookPath(book_id) + "book.json";
    }

    public static String getMp3PathByPath(String bookPath, String mp3Name) {
        return bookPath + "mp3" + File.separator + mp3Name;
    }

    public String getMp3PathById(String book_id, String mp3Name) {
        return getBookPath(book_id) + "mp3" + File.separator + mp3Name;
    }

    public static String getPageBgPathByPath(String bookPath, String imgName) {
        return bookPath + "bookpage" + File.separator + imgName;
    }

    public static String getPageBgPathById(String book_id, String imgName) {
        return getBookPath(book_id) + "bookpage" + File.separator + imgName;
    }

    public static String getBookPath(String book_id) {
        return SRApplication.BOOK_ROOT_DIR + book_id + File.separator;
    }

    public static String getBookZipPath(String book_id) {
        return SRApplication.BOOK_ZIP_ROOT_DIR + book_id + ".zip";
    }
}
