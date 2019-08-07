package br.edu.ufvjm.gestorvirtual;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class Routines<T> {

    public static<T> void fadeIn(T element)
    {
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(element, "alpha", 0, 1f);
        fadeIn.setDuration(500);
        fadeIn.start();
    }
    public static<T> void fadeOut(T element)
    {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(element, "alpha", 1f, 0);
        fadeOut.setDuration(500);
        fadeOut.start();
    }
    public static void cleanDataDirectory(Context m) throws IOException
    {
        File f = m.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(f.exists())
        {
            FileUtils.cleanDirectory(f);
        }
    }
}
