package assignment; 
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class hwAssignment2_generator{
		public static void main(String[] args) throws IOException {
			generatorTransaction(generatorMaster()); 
		}
		
		public static int generatorMaster() throws IOException{
			PrintWriter master = new PrintWriter(new File("master.txt"));
			int size = (int)(Math.random()*(15-7+1))+7;
			String[] first = {"Karen", "Dorothy", "Bella", "Oliver", "Brian", "Julian", "Austin"}; 
			// String[] last = {"Bell", "Black", "Henderson", "Wallace", "Walsh", "Hamilton", "Miller"};
			String[] last = {"B", "A", "H", "W", "L", "E", "M"};
			String[] title = {"LLC.", "Inc.", "Co.", "Corp.", "Ltd."};
			
			for(int i=0; i<size; i++) {
				String name = first[(int)(Math.random()*(first.length-1+1))] + " " + last[(int)(Math.random()*(last.length-1+1))] + "'s " + title[(int)(Math.random()*(title.length-1+1))]; 
				double balance = Math.random()*(99999-10+1)+10; 	
				// master.printf("%04d\t\t%20s\t\t%08.2f\n", i+1, name, balance); 
				master.printf("%04d,%s,%08.2f,\n", i+1, name, balance); 
			}
			master.close();
			return size; 
		}
		
		public static void generatorTransaction(int size) throws IOException{
			PrintWriter transaction = new PrintWriter(new File("transaction.txt"));
			int numTransaction = 1; 
			
			for(int i=0; i<size; i++) {
				int numRecord = (int)(Math.random()*(8-3+1))+3;
				for(int j=0; j<numRecord; j++) {
					char op = checkerOP();
					int discount = checkerDiscount(op); 
					transaction.printf("%1c\t\t%04d\t\t%04d\t\t", op, i+1, numTransaction);
					
					if(op == 'O') {
						String[] items = {"apple", "orange", "raspberry", "banana", "avocado"};
						double[] prices = {5.29, 2.55, 12.49, 4.99, 17.75}; 
						int numItem = (int)(Math.random()*(100))+1; 
						int item = (int)(Math.random()*(items.length-1+1)); 
						
						transaction.printf("%20s\t\t%2d\t\t%5.2f", items[item], numItem, prices[item]); 
					} else {
						double payment = Math.random()*(9999)+1.0; 
						transaction.printf("%10.2f", payment); 
					}
					if(discount>0) {
						transaction.printf("\t\t%2d", discount); 
					}
					transaction.println(); 
					numTransaction++; 
				}
				
				
			}
			transaction.close();
			
		}
		public static char checkerOP() {
			char op = 'O';
			if((int)(Math.random()*(1+1)) == 1) {
				op = 'P';
			}
			return op;
		}
		public static int checkerDiscount (char op) {
			if(Math.random()>0.6) {
				if(op == 'O') {
					return (int)(Math.random()*(6+1))*5;
				}else{
					return (int)(Math.random()*(10+1))*5;
				}
			}
			return 0;
		}
	}