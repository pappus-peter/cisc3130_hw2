package assignment;
import java.io.*; 
import java.util.*; 

public class hwAssignment2_ver2 {

	public static void main(String[] args) throws IOException {
		Scanner master = new Scanner (new File("master.txt")).useDelimiter("\\s*,\\s*");
		Scanner transaction = new Scanner (new File("transaction.txt")); 
		PrintWriter output = new PrintWriter (new File ("output_hw2.txt")); 
		
		char type; 
		int idRunning; 
		int id = printMaster(master, output);
		double balanceDue = printBalancePre(master, output); 
		
		
		while(transaction.hasNext()) {
			type = transaction.next().charAt(0);
			idRunning = transaction.nextInt();
			
			if(id != idRunning) {
				printBalanceDue(balanceDue, output);
				id = printMaster(master, output);
				balanceDue = printBalancePre(master, output); 
			} 
			
			if(type == 'O') {
				balanceDue += printOrder(transaction, output);
			}else if(type == 'P'){
				balanceDue -= printPayment(transaction, output); 
				
			}
		}
		output.close();
	}
	
	static int printMaster(Scanner input, PrintWriter output) {
		int id = input.nextInt();  
		String name = input.next();
		
		output.printf("%-20s\t%04d\n\n",name, id); 
		return id; 
	}
	
	static double printBalancePre(Scanner input, PrintWriter output) {
		double balancePre = input.nextDouble(); 
		output.printf("Transaction Number\tPrevious Balance\t$%10.2f\n", balancePre);
		
		return balancePre; 
	}
	
	
	static double printOrder(Scanner input, PrintWriter output) {
		Scanner line = new Scanner(input.nextLine());	
		
		int num = line.nextInt();
		String item = line.next();
		int quantity = line.nextInt();
		item += "*" + quantity; 
		double price = line.nextDouble();
		int discount = 0; 		
		
		if(line.hasNext()) {
			discount = line.nextInt(); 
		}
		double amount = price*quantity*(100-discount)*0.01; 
		
		output.printf("%04d\t\t\t%-15s\t\t$%10.2f\n", num, item, amount);
		return amount; 
	}
	
	static double printPayment(Scanner input, PrintWriter output) {
		Scanner line = new Scanner(input.nextLine());
		int num = line.nextInt();
		double amount = line.nextDouble(); 
		int discount = 0; 
		
		if(line.hasNext()) {
			discount = line.nextInt(); 
		}
		amount*=(100-discount)*0.01; 
		output.printf("%04d\t\t\t%-15s\t\t$%10.2f\n", num, "payment", amount); 
		return amount; 
	}
	
	static void printBalanceDue(double balanceDue, PrintWriter output) {
		output.printf("\t\t\tBalance Due\t\t$%10.2f\n\n-----\n", balanceDue);
	}
	
}
