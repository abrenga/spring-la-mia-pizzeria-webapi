package it.lamiapizzeria.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements IStorageService {

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
