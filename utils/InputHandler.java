package utils;

import java.util.Scanner;

public class InputHandler {

    private static Scanner scanner = new Scanner(System.in);

    public static int getInt(String message) {
        System.out.print(message);
        // TODO: Validate input
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
