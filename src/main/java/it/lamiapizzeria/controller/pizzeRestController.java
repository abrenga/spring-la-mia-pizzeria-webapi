package it.lamiapizzeria.controller;

import java.util.Optional;

import ch.qos.logback.core.model.Model;
import it.lamiapizzeria.responses.Payload;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.lamiapizzeria.model.ModelofmenuDB;
import it.lamiapizzeria.service.PizzeService;


@RestController
@CrossOrigin
@RequestMapping("/api/pizze")
public class pizzeRestController {

    @Autowired
    private PizzeService pizzeService;


    @GetMapping("{id}")
    public ResponseEntity<Payload<ModelofmenuDB>> getMethodName(@PathVariable("id") Integer idPizze) {
        Optional<ModelofmenuDB> pizza = pizzeService.findById(idPizze);
        if (pizza.isPresent()) {
            return ResponseEntity.ok(new Payload<ModelofmenuDB>(pizza.get(), null, HttpStatus.OK));


        } else {
            return new ResponseEntity(new Payload<>(null,"non trovato " + idPizze, HttpStatus.NOT_FOUND).getStatus());
        }

    }


    @PostMapping
    public ResponseEntity<Payload<ModelofmenuDB>> postMethodName(@Valid @RequestBody ModelofmenuDB entity) {


        try {
            ModelofmenuDB pizza = pizzeService.save(entity);
            return ResponseEntity.ok((new Payload<ModelofmenuDB>(pizza, null, HttpStatus.OK)));
        } catch (Exception e) {
            return new ResponseEntity(new Payload<>(null,"non salvato ", HttpStatus.NOT_FOUND).getStatus());

        }
    }


    @PutMapping({"id"})
    public ResponseEntity updatePizza(@PathVariable("id") Integer idPizza, @Valid @RequestBody ModelofmenuDB entity) {
        Optional<ModelofmenuDB> pizza = pizzeService.findById(idPizza);
        try {

            return ResponseEntity.ok( new Payload<>(pizzeService.update(idPizza, entity),"pizza Modificata",HttpStatus.OK ));

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Payload<>(null,"Errore nel salvataggio del libro", HttpStatus.NOT_FOUND).getStatus());
        } catch (Exception e) {
            return new ResponseEntity("non salvato", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping({"id"})
    public ResponseEntity deletePizza(@PathVariable("id") Integer idPizza) {
        try {
            pizzeService.deleteById(idPizza);
            return ResponseEntity.ok(new Payload<>(null,"pizza Eliminata",HttpStatus.OK));
        } catch (Exception e) {
            return new ResponseEntity(new Payload<>(null,"non salvato", HttpStatus.INTERNAL_SERVER_ERROR).getStatus());
        }
    }
}














