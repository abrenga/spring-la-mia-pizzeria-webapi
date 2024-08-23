package it.lamiapizzeria.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import it.lamiapizzeria.storage.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

@Service
public class FileSystemStorageService implements IStorageService {

    private final Path rootLocation;


    @Autowired
    public FileSystemStorageService(StorageProperties properties) {

        if(properties.getLocation().trim().length() == 0){
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }


    @Override
   public void init(){
        
    }

    @Override
	public void store(MultipartFile file){

    }

    @Override
	public Stream<Path> loadAll(){

    }

    @Override
	public Path load(String filename){
        return Path;//solo per ricordarmi

    }

	@Override
    public Resource loadAsResource(String filename){
        return Resource;

    }

    @Override
	public void deleteAll(){

    }
    
}
