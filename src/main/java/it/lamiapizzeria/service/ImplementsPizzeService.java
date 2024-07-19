package it.lamiapizzeria.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import it.lamiapizzeria.model.ModelofmenuDB;
import it.lamiapizzeria.repository.MyRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ImplementsPizzeService implements PizzeService {

    @Autowired
    private MyRepository myRepository;

    @Override
    public Optional<ModelofmenuDB> findById(Integer id) {

        return myRepository.findById(id);
    }

    @Override
    public ModelofmenuDB save(ModelofmenuDB pizze) {
        return myRepository.save(pizze);

    }

    @Override
    public ModelofmenuDB update(Integer id, ModelofmenuDB pizze) {
        Optional<ModelofmenuDB> pizzaCercata = myRepository.findById(id);
        if (pizzaCercata.isEmpty()) {
            throw new IllegalArgumentException("non trovato");
        }
        ModelofmenuDB pizza = pizzaCercata.get();
        pizza.setName(pizze.getName());
        pizza.setDescrizione(pizze.getDescrizione());
        pizza.setPrezzo(pizze.getPrezzo());

        return myRepository.save(pizze);
    }

    @Override
    public void deleteById(Integer id) {
        myRepository.deleteById(id);
    }
}
