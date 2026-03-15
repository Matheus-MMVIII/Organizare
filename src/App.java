
public class App {
    public static void main(String[] args) {
        Users user1 = new Users(1, "John Doe", "john.doe@example.com", "123-456-7890", 5, 15);
        System.out.println("User: " + user1.getName());
        System.out.println("Email: " + user1.getEmail());
        System.out.println("Cell Phone: " + user1.getCellPhone());
        System.out.println("Date of Birth: " + user1.getDateOfBirth()[0] + "/" + user1.getDateOfBirth()[1]);
        System.out.println("Is Birthday Today? " + user1.isBirthdayToday());

        Clothing shirt = new Clothing(1, "T-Shirt", 19.99, 50, "M", "Red");
        System.out.println("\nProduct: " + shirt.getName());
        System.out.println("ID: " + shirt.getId());
        System.out.println("Price: $" + shirt.getPrice());
        System.out.println("Stock: " + shirt.getStock());
        System.out.println("Size: " + shirt.getSize());
        System.out.println("Color: " + shirt.getColor());
    }
}
