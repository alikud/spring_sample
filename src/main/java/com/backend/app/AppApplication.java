package com.backend.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SpringBootApplication
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

}

class Coffee {
    private final String id;
    private String name;

    public Coffee(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Coffee(String name) {
        this(UUID.randomUUID().toString(), name);
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}

@RestController
class RestApiController {
    private List<Coffee> coffeeList = new ArrayList<com.backend.app.Coffee>();

    public RestApiController(){
        coffeeList.addAll(List.of(new Coffee("coffe A"),
                new Coffee("coffe  B")));
    }

    @RequestMapping(value = "/coffees", method = RequestMethod.GET)
    Iterable<Coffee> getCoffees(){
        return coffeeList;
    }

    @GetMapping("/coffees/{id}")
    Optional<Coffee> getCoffeeById(@PathVariable String id){
        for (Coffee c : coffeeList) {
            if (c.getId().equals(id)){
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    @PostMapping("/coffees")
    Coffee postCoffee(@RequestBody Coffee coffee){
        coffeeList.add(coffee);
        return coffee;
    }

    @PutMapping("/coffees/{id}")
    Coffee putCoffee(@PathVariable String id, @RequestBody Coffee coffee){
        int coffeeIndex = -1;
        for (Coffee c: coffeeList) {
            if (c.getId().equals(id)) {
                coffeeIndex = coffeeList.indexOf(c);
                coffeeList.set(coffeeIndex, coffee);
            }
        }
        return (coffeeIndex == -1) ? postCoffee(coffee) : coffee;
    }

    @DeleteMapping("/coffees/{id}")
    @ResponseBody
    Map<String, String> deleteCoffee(@PathVariable String id){
        coffeeList.removeIf(c -> c.getId().equals(id));
        return Collections.singletonMap("response", id);
    }
}
