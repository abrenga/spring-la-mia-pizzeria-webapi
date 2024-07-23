package it.lamiapizzeria.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.lamiapizzeria.model.Ingredients;
import it.lamiapizzeria.repository.IngredientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.lamiapizzeria.model.ModelOfSpecialPrice;
import it.lamiapizzeria.model.ModelofmenuDB;
import it.lamiapizzeria.model.PizzaDiAmministrazione;
import it.lamiapizzeria.repository.MyRepository;
import it.lamiapizzeria.repository.SpecialPriceRepo;
import jakarta.validation.Valid;

@Controller
public class MyControllerPizzeria {

    /* Per il db */
    @Autowired
    private MyRepository repository;

    @Autowired
    private SpecialPriceRepo specialPriceRepo;

    @Autowired
    private IngredientsRepository IngredientsRepository;

    @GetMapping("index")
    public String populateMenu(@RequestParam(name = "name", required = false) String name, Model model) {

        List<ModelofmenuDB> menu = new ArrayList<>();

        if (name == null || name.isBlank()) {
            menu = repository.findAll();
        } else {
            menu = repository.findByName(name);
        }

        model.addAttribute("pizze", menu);
        return "index";
    }

    @GetMapping("index/{id}")
    public String paginaSingola(@PathVariable(name = "id") Integer id, Model model) {
        ModelofmenuDB pizza = repository.getReferenceById(id);

        model.addAttribute("dettaglioPizza", pizza);

        List<ModelOfSpecialPrice> prices = specialPriceRepo.findAllByPizze(pizza);
        LocalDateTime todatDate = LocalDateTime.now();
        List<ModelOfSpecialPrice> validOffers = new ArrayList<>();
        for (int i = 0; i < prices.size(); i++) {
            ModelOfSpecialPrice price = prices.get(i);
            if (price.getEndOfSpecialPrice().isAfter(todatDate) && price.getSpecialPriceDate().isBefore(todatDate)) {
                validOffers.add(price);
            }

        }
        model.addAttribute("specialPrices", validOffers);

        return "dettaglioPizza";

    }

    @GetMapping("/index/form")
    public String create(Model model) {
        model.addAttribute("menu", new ModelofmenuDB());
        model.addAttribute("update", false);

        return "form";
    }

    @PostMapping("/index/form")
    public String FormDb(@Valid @ModelAttribute("menu") ModelofmenuDB menu, BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "redirect:/index/form";
        }

        repository.save(menu);
        return "redirect:/index/administration";
    }

    @GetMapping("index/administration")
    public String administrationEP(Model model) {
        /*pizze*/
        List<ModelofmenuDB> pizze = repository.findAll();
        model.addAttribute("pizze", pizze);
        /*Ingredients*/

 /*special price*/
        List<PizzaDiAmministrazione> pizzaDiAmministrazione = new ArrayList<PizzaDiAmministrazione>();
        for (int i = 0; i < pizze.size(); i++) {
            ModelofmenuDB pizza = pizze.get(i);
            List<ModelOfSpecialPrice> spPrice = specialPriceRepo.findAllByPizze(pizza);
            List<Ingredients> ingredients = IngredientsRepository.findAllByMenu(pizza);
            pizzaDiAmministrazione.add(new PizzaDiAmministrazione(pizza, spPrice, ingredients));
        }

        model.addAttribute("pizzaAmministratore", pizzaDiAmministrazione);

        return "administration";
    }

    @GetMapping("/form/{id}")
    public String administration(@PathVariable(name = "id") Integer id, Model model) {
        model.addAttribute("menu", repository.getReferenceById(id));
        model.addAttribute("update", true);

        return "form";
    }

    @PostMapping("/form/{id}")
    public String update(@Valid @ModelAttribute("updated") ModelofmenuDB updated, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/index/formUpdate/{updated.id}";
        }
        repository.save(updated);
        return "redirect:/index/administration";
    }

    @PostMapping("{resource}/delete/{id}")
    public String delete(@PathVariable("resource") String resource, @PathVariable("id") Integer id) {
        switch (resource) {
            case "pizza":
                repository.deleteById(id);
                break;
            case "specialOffer":
                specialPriceRepo.deleteById(id);
                break;
            case "ingredient":
            deleteIngredient(id);
        }

        return "redirect:/index/administration";
    }

    private void deleteIngredient(Integer idIngredient) {
        Optional<Ingredients>  i = ingredientsRepository.findById(idIngredient);
        if( !i.isPresent()) {
            return;
        }
  
         Ingredients ingredient = i.get();
          List<ModelofmenuDB>  pizze = repository.findByIngredients(ingredient);
          for (int l=0; l<pizze.size(); l++){
              ModelofmenuDB pizza = pizze.get(l);
              pizza.getIngredients().remove(ingredient);
              repository.save(pizza);
          }
          ingredientsRepository.delete(ingredient);
          }

    @GetMapping("/formSpecialPrice/{id}")
    public String specialPriceAdmin(@PathVariable(name = "id") Integer id, Model model) {
        model.addAttribute("specialPricelist", specialPriceRepo.getReferenceById(id));
        model.addAttribute("update", true);

        return "formSpecialPrice";
    }

    @PostMapping("/formSpecialPrice/{id}")
    public String specialPriceAdminUpdate(@Valid @ModelAttribute("updated") ModelOfSpecialPrice updated, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/index/formSpecialPrice/{updated.id}";
        }
        specialPriceRepo.save(updated);
        return "redirect:/index/administration";
    }

    @GetMapping("/index/formSpecialPrice")
    public String createSpecialPrice(@PathVariable("id") Integer id, Model model) {
        ModelOfSpecialPrice specialPrice = new ModelOfSpecialPrice();
        ModelofmenuDB pizza = repository.getReferenceById(id);
        specialPrice.setPizze(pizza);
        model.addAttribute("specialPricelist", specialPrice);
        model.addAttribute("updated", false);

        return "formSpecialPrice";
    }

    @PostMapping("/index/formSpecialPrice")
    public String postSpecialPrice(@Valid @ModelAttribute("specialPricelist") ModelOfSpecialPrice specialPrice, BindingResult bindingResult, Model model) {
        model.addAttribute("specialPricelist", specialPrice);
        if (bindingResult.hasErrors()) {
            return "redirect:/index/formSpecialPrice/{specialPricelist.id}";
        }
        specialPriceRepo.save(specialPrice);
        return "redirect:/index/administration";
    }

    @GetMapping
    public String index(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "redirect:/index/administration";
    }

    @GetMapping("/index/formIngredients/{id}/add")
    public String addIngredients(@PathVariable(name = "id") Integer id, Model model) {
        model.addAttribute("pizzaId", id);
        model.addAttribute("ingredients", IngredientsRepository.findAll());
        model.addAttribute("up", false);
        model.addAttribute("ingredientNew", new Ingredients());

        return "formIngredients";
    }

    @PostMapping("/index/formIngredients/{id}/add" )   
    public String addIngredient(@PathVariable(name="id")Integer id, @ModelAttribute("ingredients") Ingredients ingredient, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "redirect:/index/formIngredients";
        }
            ModelofmenuDB pizza = repository.getReferenceById(id);
    Ingredients ingredients = IngredientsRepository.findById(ingredient.getId()).get();
        pizza.getIngredienst().add(ingredient);
        repository.save(pizza);
        return "redirect:/index/administration";
    }

    @GetMapping("index/formIngredients/{id}/update") 
    public String updateIgredients(@PathVa riable("id") Integer id, Model model){
        model.addAttribute("idIngredient",IngredientsRepository.getReferenceById(id));
        model.addAttribute("ingredient", new Ingredients());
        model.addAttribute("up", true);
        return "redirect:/index/administration";

    }

    @PostMapping("index/formIngredients/update")  
    public String updateIgredients(@ModelAttribute("ingredient") Ingredients ingredient,BindingResult bindingResult ){ 
        
            bindingResult.hasErrors()) {
            return "redirect:/index/administration";
        
     IngredientsRepository.save(ingredient);
        
    return "redirect:/index/administration";

    

}