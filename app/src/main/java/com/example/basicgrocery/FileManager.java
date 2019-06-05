package com.example.basicgrocery;

import android.content.Context;

import java.io.File;

public class FileManager {

    static void deleteFile(Context context,String fileName){

        File file = new File(context.getFilesDir(), fileName);
        file.delete();

    }

    static void renameFile(Context context, String oldFileName, String newFileName ){

        //TODO: ensure cant rename to existing file
        File oldFile = new File(context.getFilesDir(), oldFileName);
        File newFile = new File(context.getFilesDir(), newFileName);
        oldFile.renameTo(newFile);
        //file.delete();

    }
}
