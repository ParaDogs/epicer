package com.paradogs.epicer.utils;

import com.paradogs.epicer.model.Recipe;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Utils
{
    private static void createFile(String path)
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(new ArrayList<Recipe>());
            out.close();
            fileOut.close();
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
    }

    public static void serialize(List<Recipe> list, String path)
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(list);
            out.close();
            fileOut.close();
        }
        catch (FileNotFoundException e) {createFile(path);}
        catch (IOException e) {e.printStackTrace();}
    }

    public static List<Recipe> deserialize(String path)
    {
        List<Recipe> list = new ArrayList<Recipe>();
        try
        {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            list = (List<Recipe>) in.readObject();
            in.close();
            fileIn.close();
        }
        catch (FileNotFoundException e) {createFile(path);}
        catch (IOException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
        return list;
    }
}
