package com.yuliia.airlines_api.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


@Service
public class FileStorageService {

    @Value("${image.directory}")
    private String imageDirectory;

    @Value("${image.url}")
    private String imageUrl;

    public String imgUpload(MultipartFile file){
        if(file.isEmpty()){
            throw new RuntimeException("The img do not uploaded");
        }

        try {
            byte[] bytes = file.getBytes();
            String imageExtension = file.getOriginalFilename()
                    .substring(file.getOriginalFilename().lastIndexOf(".")+1);

            String imgNewName = UUID.randomUUID() + "." + imageExtension;
            String imgNewDirectory = imageDirectory + imgNewName;

            Path imgNewPath = Path.of(imgNewDirectory);
            Files.createDirectories(imgNewPath.getParent());
            Files.write(imgNewPath, bytes);

            String imgUrl = imageUrl + imgNewName;
            return imgUrl;

        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void deleteImg(String imgUrl){

        try {
            String imgName = imgUrl.substring(imgUrl.lastIndexOf("/")+1);
            Path imgPath = Path.of(imageDirectory + imgName);
            Files.delete(imgPath);

        } catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}
// TODO añadir la lógica de update img