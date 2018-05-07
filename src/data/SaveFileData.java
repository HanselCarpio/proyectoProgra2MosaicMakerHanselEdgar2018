/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import domain.PartsImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hansel
 */
public class SaveFileData {

    public SaveFileData() {
    }

    public void saveProject(PartsImage[][] matrizImage, PartsImage[][] matrizMosaic, String path) throws IOException, ClassNotFoundException {
        File file;
        //si el archivo existe, si no, crea uno nuevo
        if(new File(path).exists()){
            file = new File(path);
        }else{
            file=new File(path);
        }
        //guarda las matrices en un arrayList, primero la imagen luego el mosaico
        List<PartsImage[][]> partsImageses = new ArrayList<>();
        partsImageses.add(matrizImage);
        partsImageses.add(matrizMosaic);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        objectOutputStream.writeUnshared(partsImageses);
        objectOutputStream.close();
    } // save

    public List<PartsImage[][]> recover(File file) throws IOException, ClassNotFoundException {
        List<PartsImage[][]> partsImageses = new ArrayList<>();
        if (file.exists()) {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            Object aux = objectInputStream.readObject();
            //obtiene las matrices del archivo
            partsImageses = (List<PartsImage[][]>) aux;
            objectInputStream.close();
        } // if(myFile.exists())
        return partsImageses;
    } // recover
}
