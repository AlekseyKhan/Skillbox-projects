import java.util.HashMap;
import java.util.regex.Pattern;

public class CustomerStorage {
    private static Pattern PHONENUMBER = Pattern.compile("^(\\+7)(\\d{10})$");
    private static Pattern NAME = Pattern.compile("[A-Z]{1}[a-z]+\\s[A-Z]{1}[a-z]*$");
    private static Pattern EMAIL = Pattern.compile(
            "^((\\w|[-+])+(\\.[w-]+)*@[\\w-]+((\\.[\\d\\p{Alpha}]+)*(\\.\\p{Alpha}{2,})*)*)$"
    );

    private HashMap<String, Customer> storage;

    public CustomerStorage() {
        storage = new HashMap<>();
    }

    public void addCustomer(String data) throws ArrayIndexOutOfBoundsException {
        String[] components = data.split("\\s+");
        String name = components[0] + " " + components[1];
        String email = components[2];
        String phoneNumber = components[3];
        if (!NAME.matcher(name).matches()) {
            throw new IllegalArgumentException("Неверный формат имени");
        }
        if (!PHONENUMBER.matcher(phoneNumber).matches()) {
            throw new IllegalArgumentException("Неверный формат тел. номера");
        }
        if (!EMAIL.matcher(email).matches()) {
            throw new IllegalArgumentException("Неверный формат эл. адреса");
        }
        storage.put(name, new Customer(name, phoneNumber, email));

    }

    public void listCustomers() {
        storage.values().forEach(System.out::println);
    }

    public void removeCustomer(String name) {
        storage.remove(name);
    }

    public int getCount() {
        return storage.size();
    }
}