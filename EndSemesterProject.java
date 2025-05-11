import java.util.*;
import java.io.*; //file reading/writing
class EndSemesterProject{ ////////MAIN CLASS
    public static void main(String args[]){
       Scanner input=new Scanner(System.in);
       ArrayList<User>UserList=new ArrayList<>();
       ArrayList<Product>ProductList=new ArrayList<>();
       //logic for reading from file and populating arrayLists
       Management m1=new Management();
       m1.Manage(input,UserList);
       
    }
}
class Management{//class responsible for showing intial choice menu

    String productfile = "Products.txt"; //will be in the same directory as the .java file
    //String userfile = "Users.txt";
    String userfile = "C:\\Users\\hp\\Desktop\\Users.txt";
    String transactionfile = "Transactions.txt";
    String purchasehistoryfile = "Purchases.txt";
    String coursefile = "Courses.txt";
    BufferedWriter userwriter = null;
    BufferedReader userreader = null;
    
    public int generateHunarhubID(ArrayList<User> userlist){ //id generated randomly, ensures every id is unique
        Random rng = new Random();
        int id = 0;
        boolean isUnique = false;
        do{
            id = 100 + rng.nextInt(900); //3 digit num between 100-999
            isUnique = true;
            for(User m:userlist){
                if(id == m.getID())
                    isUnique = false;
            }
        } while(!isUnique); //keeps generating id until a unique one is created
        return id;
    }
    void readUserFile(ArrayList<User> UserList, String userfile, BufferedReader userreader){
        String line = null;
        try{
            File file = new File(userfile);
            if (!file.exists()) {
                System.out.println("No user data found, continuing...");
                return; // If no file exists, just return
            }
            while( (line=userreader.readLine()) != null){
                String[] data = line.split(",");
                int isMaker = Integer.parseInt(data[0]);
                int id = Integer.parseInt(data[1]);
                String username = data[2];
                String password = data[3];
                int accountid = Integer.parseInt(data[4]);
                String accountpassword = data[5];
                double balance = Double.parseDouble(data[6]);
                
                User u;
                if(isMaker == 1)
                    u = new Maker(isMaker,username, password, id, accountid, accountpassword, balance); 
                else
                    u = new Buyer(isMaker,username, password, id, accountid, accountpassword, balance); 
                
                UserList.add(u);
            }
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    void Manage(Scanner input,ArrayList<User>UserList){
        try{
            File file = new File(userfile);
            if (!file.exists()) {
                System.out.println("file doesnt exist, creating a new one...");
                file.createNewFile();  // Create the file if it doesn't exist
            }
            userreader = new BufferedReader(new FileReader(userfile));
        }
        catch(IOException e){
            System.out.println(e);
        }
        readUserFile(UserList, userfile, userreader);//reads users stored in the file and writes them to the arraylist
        
        //PROOf that it does read past users and stores them in the arraylist BEFORE starting main menu
        System.out.println("Users read from file:");
        for(User u:UserList){
            System.out.println(u);
        }
        
        System.out.println("Welcome to HunarHub");
        boolean continueLoop1=true;
        boolean continueLoop2=false;
        int choice=0;
        do{//loop for choices
         System.out.println("Choose one of the following");
         System.out.println("1.To login");
         System.out.println("2.To sign up");
         System.out.println("3.Exit");
         do{
             try{//for valid input
                 choice=input.nextInt();
                 if(choice!=1&&choice!=2&&choice!=3){//throws customized exception
                     continueLoop2=true;
                     throw new WrongInput("Please choose bwtween 1 and 3");
                 }
                 else 
                 continueLoop2=false;
             }
             catch(WrongInput e){
                 input.nextLine();
                 System.out.println(e);
             }
             catch(InputMismatchException e){//catches exception if input was not integer
                 input.nextLine();
                 System.out.println("Error: Please enter a valid integer between 1 and 3");
                 continueLoop2=true;
             }
         }while(continueLoop2);
         
         switch(choice){
             case 1:{
                Login login=new Login();
                login.LoginMenu(UserList,input);
             }break;
             case 2:{
                Signup signup=new Signup();
                signup.SignUpMenu(input,UserList, userfile, userwriter, userreader);
             }break;
             case 3:
                continueLoop1=false;
                break;
         }
 
        }while(continueLoop1);
        input.close();
    }
}
class Login{//for login
   void LoginMenu(ArrayList<User>UserList,Scanner input){
       System.out.println("Enter password");
       boolean passwordExists=false;
       User A=null;//empty user refernce
       input.nextLine();
       do{
        try{
            String password=input.nextLine();
            for(User a:UserList){
                if(password.equals(a.password)){
                   A=a;//points to the user in question
                   passwordExists=true;
                   break;
                }
            }
            if(passwordExists==false)
              throw new WrongInput("Entered password is wrong");
        }
        catch(WrongInput e){
            System.out.println(e);
        }
       }while(!passwordExists);
       Menu menu=new Menu();
       if(A instanceof Maker)
          menu.MakerMenu(A);
       else
         menu.BuyerMenu(A,input);
       return;
    }
}

class Signup {
    
    void SignUpMenu(Scanner input, ArrayList<User> UserList, String userfile, BufferedWriter userwriter, BufferedReader userreader) {
        try{
            File file = new File(userfile);
            if (!file.exists()) {
                System.out.println("file doesnt exist, creating a new one...");
                file.createNewFile();  // Create the file if it doesn't exist
            }
            userwriter = new BufferedWriter(new FileWriter(userfile,true)); //true to append it
        }
        catch(IOException e){
            System.out.println(e);
        }
        
        boolean continueLoop = true;
        boolean UserNameExists = false;
        String Username = null;
        String password = null;
        String accountpassword = null;
        int type = 0;
        boolean isCorrect = false;

        // Account type selection
        System.out.println("\nChoose your account type: \n 1) Maker  2) Buyer");
        do {
            try {
                System.out.print("Enter your choice: ");
                type = input.nextInt();
                input.nextLine(); // Flush leftover newline

                if (type < 1 || type > 2) {
                    throw new WrongInput("Please choose between options 1 and 2");
                }
                isCorrect = true;
            } catch (WrongInput e) {
                System.out.println(e);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter numbers only.");
                input.nextLine(); // Flush wrong input
            }
        } while (!isCorrect);

        // Username input and uniqueness check
        System.out.println("Enter a username:");
        do {
            try {
                Username = input.nextLine();
                for (User a : UserList) {
                    if (Username.equals(a.Username)) {
                        UserNameExists = true;
                        break;
                    }
                }
                if (UserNameExists)
                    throw new WrongInput("This username already exists. Please modify it to be unique.");
                continueLoop = false;
            } catch (WrongInput e) {
                System.out.println(e);
                UserNameExists = false; // Reset for next try
            }
        } while (continueLoop);

        // Primary password
        continueLoop = true;
        boolean hasSpecialCharacter = false;
        System.out.println("Enter a strong password (min 8 characters with at least one special character):");
        do {
            try {
                password = input.nextLine();
                hasSpecialCharacter = false;

                if (password.length() < 8) {
                    throw new WrongInput("Password must be at least 8 characters long.");
                }

                for (int i = 0; i < password.length(); i++) {
                    if (!Character.isLetterOrDigit(password.charAt(i))) {
                        hasSpecialCharacter = true;
                        break;
                    }
                }

                if (!hasSpecialCharacter) {
                    throw new WrongInput("Password must contain at least one special character.");
                }

                continueLoop = false;
            } catch (WrongInput e) {
                System.out.println(e);
            }
        } while (continueLoop);

        // Account password
        continueLoop = true;
        hasSpecialCharacter = false;
        System.out.println("Enter your account password (min 8 characters with at least one special character:");
        do {
            try {
                accountpassword = input.nextLine();
                hasSpecialCharacter = false;

                if (accountpassword.length() < 8) {
                    throw new WrongInput("Password must be at least 8 characters long.");
                }

                for (int i = 0; i < accountpassword.length(); i++) {
                    if (!Character.isLetterOrDigit(accountpassword.charAt(i))) {
                        hasSpecialCharacter = true;
                        continueLoop = false;
                        break;
                    }
                }

                if (!hasSpecialCharacter) {
                    throw new WrongInput("Password must contain at least one special character.");
                }

                continueLoop = false;
            } catch (WrongInput e) {
                System.out.println(e);
            }
        } while (continueLoop);

        Management m = new Management();
        int ID = m.generateHunarhubID(UserList);
        User u = null;
        if (type == 1)
            u = new Maker(1,Username, password, ID,UserList, accountpassword);
        else
            u = new Buyer(0,Username, password, ID,UserList, accountpassword);

        UserList.add(u);
        try{
            userwriter.write(u.toString());
            userwriter.newLine();
            userwriter.flush();
            userwriter.close();
            System.out.println("Account created successfully. You may now log in.");            
        }
        catch(IOException e){
            System.out.println(e);
        }
    }
}
////////Still needs work

class BankAccount {
    private int accountID;
    private String bankpassword;
    private double balance;
    public BankAccount(ArrayList<User> userlist, String bankpassword) {
        this.accountID = this.generateBankID(userlist);
        this.bankpassword = bankpassword;
        this.balance = 0.0; // default starting balance
    }
    public BankAccount(int accountID, String bankpassword, Double balance) {
        this.accountID = accountID;
        this.bankpassword = bankpassword;
        this.balance = balance; // balance from the file
    }
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: PKR" + amount);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }
    public void withdraw(double amount) {
        if (amount > balance)
            System.out.println("Error: Insufficient balance. Withdrawal denied.");
        else {
            balance -= amount;
            System.out.println("Withdrawn: PKR" + amount);
        }
    }
    public double getBalance() {
        return balance;
    }
    public int getAccountID() {
        return accountID;
    }
    public String getAccountPassword(){
        return this.bankpassword;
    }
    public int generateBankID(ArrayList<User> userlist){ //id generated randomly, ensures every id is unique
        Random rng = new Random();
        int id = 0;
        boolean isUnique = false;
        do{
            id = 100 + rng.nextInt(900);
            if(userlist.isEmpty()){
                System.out.println("Your Bank ID is "+ id);
                return id;
            }
            for(User m:userlist){
                if(id != m.account.getAccountID())
                    isUnique = true;
            }
        } while(!isUnique); //keeps generating id until a unique one is created
        
        System.out.println("Your Bank ID is "+ id);
        return id;
    }

    public String toString() {
        return this.accountID +"," + this.balance;
    }
}
class Menu{
  void MakerMenu(User A){
    
  }
  void BuyerMenu(User B, Scanner input){
    Management management = new Management();
    boolean isCorrect = false;
    boolean menuisOpen = false;
    int choice = 0;
    do{
        System.out.print("Select an option: \n 1) Browse Products  2) Open WishList  3) View Cart 4) Buy Products"
            +" \n 5) Rate Maker 6) View Past Purchases 7) Manage Bank Account 8) Log out");
        do{
            System.out.print("\n Enter your choice:");
            try{
                choice = input.nextInt();
                input.nextLine();
                
                if(choice<1 || choice>8)
                    throw new WrongInput("Please choose between options 1 and 8");
                else
                    isCorrect = true;
            }
            catch(WrongInput e){
                System.out.println(e);
            }
        }
        while(!isCorrect);
        switch(choice){
            case 1:{ //BROWSE PRODUCTS
                
            }break;
            
            case 2:{ //OPEN WISHLISH
                
            }break;
            
            case 3:{ //VIEW CART
                
            }break;
            
            case 4:{ //Buy Products
                
            }break;
            case 5:{ //Rate Creators (open transaction history and choose from there)
                System.out.println("Select the creator you want to rate:");
                
            }break;
            
            case 6:{ //past purchases, transaction file
                
            }break;
            
            case 7:{ //view bank details(id, current balance
                boolean passwordCorrect=false;
                boolean idCorrect = false;
                int accountid;
                String password;
                
                do{
                    System.out.println("Enter your Bank ID:");
                    try{
                        accountid = input.nextInt();
                        input.nextLine();//compensates new line skips

                        if(accountid != B.account.getAccountID())
                            throw new WrongInput("Invalid Bank Account ID");
                        else
                            idCorrect = true;
                    }
                    catch(WrongInput e){
                        System.out.println(e);
                    }
                }while(!idCorrect);
                
                do{
                    System.out.println("Enter your Bank Account Password:"); //output id when account is created and tell them to remember thier id
                    try{
                        password = input.nextLine();

                        if(!password.equals(B.account.getAccountPassword()))
                            throw new WrongInput("Invalid Bank Account Password");
                        else
                            passwordCorrect = true;
                    }
                    catch(WrongInput e){
                        System.out.println(e);
                    }
                }while(!passwordCorrect);
                
                boolean exitMenu = false;
                boolean isoptioncorrect = false;
                int option=0;
                double amount=0;
                do{
                    System.out.println("Choose an option: 1) Deposit Balance  2) Withdraw Balance  3) Exit to Menu");
                    do{
                        try{
                            option = input.nextInt();
                            input.nextLine();
                            if(option<1 || option>3)
                                throw new WrongInput("Option must be between 1 and 3");
                            else
                                isoptioncorrect = true;
                        }
                        catch(WrongInput e){
                            System.out.print(e);
                        }                            
                    }while(!isoptioncorrect);

                    switch(option){
                        case 1:{ //deposit
                            System.out.print("Enter amount to Deposit: PKR");
                            try{
                                amount = input.nextDouble();
                                input.nextLine();

                                if(amount<1)
                                    throw new WrongInput("Amount deposited cannot be negative");
                            }
                            catch(WrongInput e){
                                System.out.print(e);
                            }
                            B.account.deposit(amount);
                        }break;
                        case 2:{ //withdraw
                            System.out.print("Enter amount to Withdraw: PKR ");
                            try{
                                amount = input.nextDouble();
                                input.nextLine();

                                if(amount<1)
                                    throw new WrongInput("Amount withdrawn cannot be negative");
                                if(amount > B.account.getBalance())
                                    throw new WrongInput("Amount Withdrawn cannot exceed deposited amount");
                            }
                            catch(WrongInput e){
                                System.out.print(e);
                            }
                            B.account.withdraw(amount);

                        }break;
                        case 3:{ //exit to menu
                           exitMenu = true; 
                        }break;
                    }
                }
                while(!exitMenu);
                  
            }break;
            
            case 8:{ //log out as Buyer
                System.out.println("Logging out...");
                menuisOpen = true;
            }break;
        }
    }while(!menuisOpen);
  }
}
class WrongInput extends Exception{
    String message;
    WrongInput( String message){
      this.message=message;
    }
    public String toString(){
       return "Error: "+message;
    }
}
abstract class User{
    protected int isMaker;
    protected int ID;
    protected String Username;
    protected String password;
    protected BankAccount account;
    
    User(int isMaker, String Username, String password, int ID,ArrayList<User> userlist, String bankpassword){
        this.isMaker = isMaker;
        this.account = new BankAccount(userlist,bankpassword);
        this.Username = Username;
        this.password = password;
        this.ID = ID;
    }
    User(int isMaker, String Username, String password, int ID,int accountID, String bankpassword, Double balance){ //to create form the file
        this.isMaker = isMaker;
        this.account = new BankAccount(accountID, bankpassword, balance);
        this.Username = Username;
        this.password = password;
        this.ID = ID;
    }
    
//    //this.accountID = this.generateBankID(userlist);
//        this.bankpassword = bankpassword;
//        this.balance = 0.0; // default starting balance
   public int getID(){
        return ID;
    }
}
class Maker extends User{
    //maker constructor MAY have different attributes added but this is the common stuff
    Maker(int isMaker,String Username, String password, int ID,ArrayList<User> userlist,String bankpassword){
        super(isMaker, Username, password, ID,userlist,bankpassword);
    }
    Maker(int isMaker,String Username, String password, int ID,int accountID, String bankpassword, Double balance){
        super(isMaker, Username,  password, ID, accountID, bankpassword, balance);
    }
    
    @Override //again the toString will mostprobably include more strributes
    public String toString(){
        return super.isMaker+","+super.ID + "," + super.Username + "," + super.password+ "," 
        + super.account.getAccountID()+ "," +super.account.getAccountPassword()+","+super.account.getBalance();
    }
    
    //add product
    //remove product
    //rating
    //review list
    //sales tracking
    //performance viewer
}
class Courses{
  //location,description category and stuff(As specified in doc shared earlier)
}
class Buyer extends User{
    Cart cart = new Cart();
    WishList wishlist = new WishList();
    
    Buyer(int isMaker,String Username, String password, int ID,ArrayList<User> userlist,String bankpassword){
        super(isMaker, Username, password, ID,userlist,bankpassword);
    }
    Buyer(int isMaker,String Username, String password, int ID,int accountID, String bankpassword, Double balance){
        super(isMaker, Username,  password, ID, accountID, bankpassword, balance);
    }
    @Override
    public String toString(){
        return super.isMaker+","+super.ID + "," + super.Username + "," + super.password+ "," 
        + super.account.getAccountID()+ "," +super.account.getAccountPassword()+","+super.account.getBalance();
    }

    public void RateMaker(){
    }
    //cart //wishlist //notifications
    //bank account //print history
    //will get to these once products, maker have been defined more
}
class Product{
  //should contain maker id category(As specified in doc shared earlier)
}
interface ProductManager{
  public abstract void addProduct(Product p);
  public abstract void removeProduct(Product p);
}
class Bank{
  //must have something that can actually store transactions maybe make a transactions class and then arraylist of all transaction
}
class Cart implements ProductManager{
  //maybe has hashmaps for an elaborate order thing going on
  ArrayList <Product> cartitems = new ArrayList<>();
  @Override
  public void addProduct(Product p){
    
  }
  @Override
  public void removeProduct(Product p){
    
  }
}
class WishList implements ProductManager{
  //should also have method to add to cart
  ArrayList <Product> wishitems = new ArrayList<>();
  @Override
  public void addProduct(Product p){
    wishitems.add(p);
  }
  @Override
  public void removeProduct(Product p){
    
  }
}
class review{

}
