package it.lamiapizzeria.service;

import java.util.Optional;

import it.lamiapizzeria.model.ModelofmenuDB;

import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;

@Service
public interface PizzeService {

    public Optional<ModelofmenuDB> findById(Integer id);

    public ModelofmenuDB save(ModelofmenuDB pizze);
    public ModelofmenuDB update (Integer id, ModelofmenuDB pizze);
    public void deleteById(Integer id);


    
}
