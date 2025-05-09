import java.util.*;
class EndSemesterProject{
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
    void Manage(Scanner input,ArrayList<User>UserList){
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
             case 1:
                Login login=new Login();
                login.LoginMenu(UserList,input);
             case 2:
                Signup signup=new Signup();
                signup.SignUpMenu(input,UserList);
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
         menu.BuyerMenu(A);
       return;
    }
}
class Signup{//for signup
    void SignUpMenu(Scanner input,ArrayList<User>UserList){
      boolean continueLoop=true;
      boolean UserNameExists=false;
      String Username=null;
      String password=null;
      System.out.println("Enter a User name");
      input.nextLine();
      do{
        try{
            Username =input.nextLine();
            for(User a:UserList){
               if(Username.equals(a.Username)){
                 UserNameExists=true;
                 break;
               }
            } 
            if(UserNameExists==true)
              throw new WrongInput("This user name already exists, Please add some characters to it to make it unique");
            continueLoop=false;
        }
        catch(WrongInput e){
            System.out.println(e);
            UserNameExists=false;//resetting
        }
      }while(continueLoop);
      
      continueLoop=true;//resetting
      boolean hasSpecialCharacter=false;
      System.out.println("Enter a Strong password, it must have atleast 8 characters with atleast one special character");
      do{
        try{
          password=input.nextLine();
          if(password.length()<8)
            throw new WrongInput("Password must atleast be 8 characters long, enter again");
          for(int i=0;i<password.length();i++){
            if((int)password.charAt(i)<=47&&(int)password.charAt(i)>=33||(int)password.charAt(i)<=64&&(int)password.charAt(i)>=58||(int)password.charAt(i)<=96&&(int)password.charAt(i)>=91||(int)password.charAt(i)<=126&&(int)password.charAt(i)>=123){
                hasSpecialCharacter=true;
                break;
            }
           }
           if(hasSpecialCharacter==false)
             throw new WrongInput("Password must contain atleast one special character, enter again");
           continueLoop=false;
        }
        catch(WrongInput e){
            System.out.println( e);
        }
      }while(continueLoop);
      //needs bank account creation
      //lastly must add to array list and then to file
    }
}
////////Still needs work
class Menu{
  void MakerMenu(User A){
    
  }
  void BuyerMenu(User B){
   
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
    int ID;
    String Username;
    String password;
     //bank account
     //constructor
   
   
}
class Maker extends User{
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
    //cart
    //wishlist
    //notifications
    //bank account
    //print history
}
class Product{
  //should contain maker id category(As specified in doc shared earlier)
}
interface ProductManager{
  //add product()
  //remove product()
}
class Bank{
  //must have something that can actually store transactions maybe make a transactions class and then arraylist of all transaction
}
class Cart implements ProductManager{
  //maybe has hashmaps for an elaborate order thing going on
}
class WishList implements ProductManager{
  //should also have method to add to cart
}
class review{

}
