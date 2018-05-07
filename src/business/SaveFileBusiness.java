/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import domain.MosaicObject;
import data.SaveFileData;
import domain.PartsImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author hansel
 */
public class SaveFileBusiness {
    SaveFileData fileData;

    public SaveFileBusiness() {
        this.fileData=new SaveFileData();
    }
    
    public void saveProject(PartsImage[][] matrizImage, PartsImage[][] matrizMosaic, String path) throws IOException, ClassNotFoundException{
        this.fileData.saveProject(matrizImage, matrizMosaic, path);
    }
    
    public List<PartsImage[][]> recover(File file) throws IOException, ClassNotFoundException{
        return this.fileData.recover(file);
    }
}
