//zawiera kolekcję typu
//ConcurrentMap w której przechowywane są pary tekstów <imię , numer telefonu>. Klasa
//PhoneBook powinna posiadać metody wykonujące poszczególne operacja na kolekcji np.:
//String LOAD(String nazwa_pliku);
//String SAVE(String nazwa_pliku);
//String GET(String imię):
//String PUT( String imię, String numer);
//String REPLACE(String imie, String numer);
//String DELETE(String imie);
//String LIST();

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PhoneBook {

    Map<String, Integer> book = new ConcurrentHashMap<>();

    String LOAD(String fileName) {
        return fileName;
    }

    String SAVE(String fileName){
        return fileName;
    }

    String GET(String name){
        return name;
    }

    String PUT(String name, String number){
        return number;
    }

    String REPLACE(String name, String number){
        return number;
    }

    String DELETE(String name){
        return name;
    }

    String LIST(){
        return null;
    }

}