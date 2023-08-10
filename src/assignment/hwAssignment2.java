package assignment;
import java.io.*; 
import java.util.*; 

/**
 * will process files of customers and transactions
 * and print them in organized format
 * This is the first version that used class
 * and created arrays for customers and transactions
 */
public class hwAssignment2 {
	public static void main(String[] args) throws IOException {
		Scanner master = new Scanner (new File("master.txt")).useDelimiter("\\s*,\\s*");
		Scanner transaction = new Scanner (new File("transaction.txt")); 
		PrintWriter output = new PrintWriter (new File ("output_hw2.txt")); 
		
		char type; 
		ArrayList<Customer> customers = new ArrayList<Customer>(); 
		ArrayList<Transaction> transactions = new ArrayList<Transaction>(); 
		while(master.hasNext()) {
			customers.add(Customer.read(master.nextLine()));
		}
		while(transaction.hasNext()) {
			type = transaction.next().charAt(0);	
			if(type =='O') {
				transactions.add(Order.read(transaction.nextLine()));
			}else if(type == 'P') {
				transactions.add(Payment.read(transaction.nextLine()));
			}
		}
		

		double balanceAdd = 0.0; 				
		int cusI= customers.get(0).getId()-1; 
		int transI = transactions.get(0).getId()-1; 
		output.print(customers.get(cusI).toString());
		
		for(int i=0; i<transactions.size(); i++) {	
			transI = transactions.get(i).getId()-1;
			if(cusI != transI) {
				balanceAdd = resetBalance(customers.get(cusI).getBalancePre(), balanceAdd, output);
				output.print(customers.get(cusI).toString());
				cusI = transI; 
			}
			output.print(transactions.get(i).toString());
			balanceAdd += transactions.get(i).getAmount();
		}
		balanceAdd = resetBalance(customers.get(cusI).getBalancePre(), balanceAdd, output);
		
		/**
		 * this version used a nested for loop
		 * which would process the transaction array multiple times redundantly
		 * even it appears to be simpler
		 */
//		for(int i=0; i<customers.size(); i++) {
//		output.print(customers.get(i).toString());
//		for(int j=0; j<transactions.size(); j++) {
//			if(customers.get(i).getId() == transactions.get(j).getId()) {
//				output.print(transactions.get(j).toString());
//				balanceAdd += transactions.get(j).getAmount(); 
//			}
//		}
//		balanceAdd = resetBalance(customers.get(i).getBalancePre(), balanceAdd, output);
//	}
		
		master.close();
		transaction.close();
		output.close();
		
	}
	
	/**
	 * will calculate and print the balance due amount for the previous customer 
	 * and return 0 to reset the balance due amount in main 
	 * @param balancePre
	 * @param balanceAdd
	 * @param output
	 * @return
	 */
	public static double resetBalance(double balancePre, double balanceAdd, PrintWriter output) {
		double balanceDue = balancePre + balanceAdd; 
		output.printf("\t\t\tBalance Due\t\t$%10.2f\n\n-----\n", balanceDue);
		return 0.0;
	}
		

	/**
	 * a class for customer
	 * it stores their id, company name and previous balance
	 */
	static class Customer{
		private int id; 
		private String name; 
		private double balance; 
		
		/**
		 * constructor with all required fields
		 * @param id
		 * @param name
		 * @param balance
		 */
		public Customer(int id, String name, double balance) {
			this.id = id; 
			this.name = name; 
			this.balance = balance; 
		}
		
		/**
		 * static read method that process a line of String data
		 * @param line
		 * @return
		 */
		public static Customer read(String line) {
			Scanner input = new Scanner(line).useDelimiter("\\s*,\\s*");
			int id = input.nextInt();  
			String name = input.next();
			double balancePre = input.nextDouble(); 
			input.close();
			
			return new Customer(id, name, balancePre); 
		}
		/**
		 * send back a string with the cusotmer's name, id and balance
		 */
		public String toString() {
			return String.format("%-20s\t%04d\n\nTransaction Number\tPrevious Balance\t$%10.2f\n", this.name, this.id, this.balance);
		}
		/**
		 * getters for all the private fields
		 * @return
		 */
		public int getId() {
			return this.id; 
		}
		public String getName() {
			return this.name;
		}
		public double getBalancePre() {
			return this.balance; 
		}
		
		
	}
	
	/**
	 * abstract class of transaction that stores 
	 * the customer id and transaction number
	 *
	 */
	static abstract class Transaction{
		private int id; 
		private int num; 
		
		/**
		 * constructor
		 * @param id
		 * @param num
		 */
		public Transaction(int id, int num) {
			this.id = id; 
			this.num = num; 
		}
		
		/**
		 * send back a string with transaction number
		 */
		public String toString() {
			return String.format("%04d\t\t\t", num);
		}
		/**
		 * getters for all private fields
		 * @return
		 */
		public int getId() {
			return this.id; 
		}
		public int getNum() {
			return this.num; 
		}
		public abstract double getAmount(); 
	}
	
	/**
	 * Order that a company received from their customers
	 */
	static class Order extends Transaction{
		private String item; 
		private double price; 
		private int discount = 0; 
		
		/**
		 * constructor with all required fields
		 * @param id
		 * @param num
		 * @param item
		 * @param price
		 * @param discount
		 */
		public Order(int id, int num, String item, double price, int discount) {
			super(id, num); 
			this.item = item; 
			this.price = price; 
			this.discount = discount; 
		}
		
		/**
		 * 
		 */
		public double getAmount() {
			return this.price*(100-this.discount)*0.01;
		}
		/**
		 * 
		 * @param line
		 * @return
		 */
		public static Order read(String line) {
			Scanner input = new Scanner(line);
			int id = input.nextInt(); 
			int num = input.nextInt();
			String item = input.next(); 
			int quantity = input.nextInt();
			item += "*" + quantity; 
			double price = input.nextDouble();
			price *= quantity; 
			int discount = 0; 
			
			if(input.hasNext()) {
				discount = input.nextInt(); 
			}
			input.close();
			
			return new Order(id, num, item, price, discount); 
		}
		/**
		 * 
		 */
		public String toString() {
			return String.format("%04d\t\t\t%-15s\t\t$%10.2f\n", super.getNum(), this.item, this.price*(100-this.discount)*0.01);
		}
	}
	
	/**
	 * Payment that a customer paid
	 */
	static class Payment extends Transaction{
		private double amount; 
		private int discount; 
		
		/**
		 * constructor with all required fields 
		 * @param id
		 * @param num
		 * @param amount
		 * @param discount
		 */
		public Payment(int id, int num, double amount, int discount) {
			super(id, num); 
			this.amount = amount; 
			this.discount = discount; 
		/**
		 * getter for a discounted price
		 */
		}
		public double getAmount() {
			return this.amount*(100-this.discount)*-0.01; 
		}
		/**
		 * calculate and send back a string with discounted amount
		 */
		public String toString() {
			return String.format("%04d\t\t\t%-15s\t\t$%10.2f\n", super.getNum(), "payment", this.amount*(100-this.discount)*0.01);
		}
		
		/**
		 * static method that process all requried fields from a line of String
		 * @param line
		 * @return
		 */
		public static Payment read(String line) {
			Scanner input = new Scanner(line);
			int id = input.nextInt();
			int num = input.nextInt();
			double amount = input.nextDouble(); 
			int discount = 0; 
			
			if(input.hasNext()) {
				discount = input.nextInt(); 
			} 
			input.close();
			return new Payment(id, num, amount, discount); 
		}
	}
}
