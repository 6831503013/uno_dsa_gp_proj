package utils;

import exceptions.EmptyNameException;
import exceptions.InvalidNameException;
import java.util.Scanner;

public class InputHandler {

    private static Scanner scanner = new Scanner(System.in);

    public static String getValidateName(Scanner sc) throws EmptyNameException, InvalidNameException {
        System.out.print("Enter your name: ");
        String name = sc.nextLine().trim();

        // Check for empty name
        if (name == null || name.trim().isEmpty()) {
            throw new EmptyNameException("Name cannot be empty. Please enter a valid name.");
        }

        // Check for invalid characters (only letters and spaces allowed)
        if (!name.matches("[a-zA-Z ]+")) {
            throw new InvalidNameException(
                    "Name contains invalid characters. Please enter a valid name with words(Aa-Zz).");
        }

        return name;
    }

    public static int getInt(String message) {
        System.out.print(message);
        int input = -1;
        boolean isValid = false;

        while (!isValid) {
            System.out.print(message);
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                isValid = true;
            } else {
                System.out.println("Invalid input. Please enter a number.");
            }
            scanner.nextLine();
        }
        return input;
    }

    public static String getString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
}
