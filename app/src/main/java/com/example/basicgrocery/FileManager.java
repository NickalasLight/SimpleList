package com.example.basicgrocery;

import android.content.Context;

import java.io.File;

public class FileManager {

    static void deleteFile(Context context,String fileName){

        File file = new File(context.getFilesDir(), fileName);
        file.delete();

    }
}
