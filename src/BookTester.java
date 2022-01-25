/*
 * Program: Apliakcja typu „klient-serwer” realizuj?c? funkcj? prostej ksi??ki telefonicznej
 *    Plik: BookTester.java
 *          - program uruchamiajacy serwer i dwóch klientówie
 *
 *    Autor: Miko?aj Przenzak
 *    indeks: 259066
 */

class BookTester {
	
	public static void main(String [] args){
		new PhoneBookServer();
		
		try{
			Thread.sleep(1000);
		} catch(Exception e){}
			
	  	new PhoneBookClient("Zosia", "localhost");
	}
	
}


