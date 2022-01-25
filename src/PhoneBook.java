/*
 * Program: Apliakcja typu „klient-serwer” realizującą funkcję prostej książki telefonicznej
 *    Plik: PhoneBook.java
 *          - definicja klasy PhoneBook, zawierająca prototyp obiektu książki telefonicznej - mapę kontaktów
 *            implementuje metody wykonujące akcje na mapie
 *
 *    Autor: Mikołaj Przenzak
 *    indeks: 259066
 *    Data: 25.01.2022 r.
 */


import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class PhoneBook {

    ConcurrentHashMap<String, String> book = new ConcurrentHashMap<>();

    String LOAD(String fileName) throws IOException {
        String serverLog = null;
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                book.put(data[0], data[1]);
            }
            csvReader.close();
            serverLog = "OK";
        } catch (IOException e) {
            serverLog = "EROOR blad odczytu z podanej bazy danych";
        }
        return serverLog;
    }

    String SAVE(String fileName) throws IOException {
        String serverLog = null;

        String separator = System.getProperty("line.separator");

        try (Writer writer = new FileWriter(fileName)) {
            for (ConcurrentHashMap.Entry<String, String> entry : book.entrySet()) {
                writer.append(entry.getKey())
                        .append(',')
                        .append(entry.getValue())
                        .append(separator);
                serverLog = "OK";
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.out.println("EROOR błąd zapisu do podanej bazy danych");
            serverLog = "EROOR błąd zapisu do podanej bazy danych";
        }

        return serverLog;
    }

    String GET(String name) {
        String number = book.get(name);
        return "OK " + number;
    }

    String PUT(String name, String number) {
        book.put(name, number);
        return "OK";
    }

    String REPLACE(String name, String number) {
        book.replace(name, number);
        return "OK";
    }

    String DELETE(String name) {
        book.remove(name);
        return "OK";
    }

    String LIST() {
        StringBuilder namesList = new StringBuilder("");
        for (ConcurrentHashMap.Entry<String, String> entry : book.entrySet()) {
            namesList.append(entry.getKey()).append(" ");
        }
        return "OK " + namesList.toString();
    }

}