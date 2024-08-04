import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Lab13_FileListMaker {
    private static ArrayList<String> list = new ArrayList<>();
    private static boolean needsToBeSaved = false;
    private static String currentFileName = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char choice;

        do {
            displayMenu();
            choice = scanner.next().charAt(0);
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 'A':
                    addItem(scanner);
                    break;
                case 'D':
                    deleteItem(scanner);
                    break;
                case 'I':
                    insertItem(scanner);
                    break;
                case 'V':
                    viewList();
                    break;
                case 'M':
                    moveItem(scanner);
                    break;
                case 'O':
                    openList(scanner);
                    break;
                case 'S':
                    saveList(scanner);
                    break;
                case 'C':
                    clearList(scanner);
                    break;
                case 'Q':
                    quitProgram(scanner);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 'Q');
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("A - Add an item to the list");
        System.out.println("D - Delete an item from the list");
        System.out.println("I - Insert an item into the list");
        System.out.println("V - View the list");
        System.out.println("M - Move an item in the list");
        System.out.println("O - Open a list file from disk");
        System.out.println("S - Save the current list file to disk");
        System.out.println("C - Clear the list");
        System.out.println("Q - Quit the program");
        System.out.print("Enter your choice: ");
    }

    private static void addItem(Scanner scanner) {
        System.out.print("Enter item to add: ");
        String item = scanner.nextLine();
        if (!item.isEmpty()) {
            list.add(item);
            needsToBeSaved = true;
        }
    }

    private static void deleteItem(Scanner scanner) {
        if (list.isEmpty()) {
            System.out.println("The list is empty.");
            return;
        }
        viewList();
        System.out.print("Enter index of item to delete: ");
        int index = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        if (index > 0 && index <= list.size()) {
            list.remove(index - 1);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void insertItem(Scanner scanner) {
        if (list.isEmpty()) {
            System.out.println("The list is empty.");
            return;
        }
        viewList();
        System.out.print("Enter position to insert: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline
        System.out.print("Enter item to insert: ");
        String item = scanner.nextLine();
        if (index >= 0 && index <= list.size()) {
            list.add(index, item);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid position.");
        }
    }

    private static void viewList() {
        if (list.isEmpty()) {
            System.out.println("The list is empty.");
            return;
        }
        System.out.println("List:");
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ". " + list.get(i));
        }
    }

    private static void moveItem(Scanner scanner) {
        if (list.isEmpty()) {
            System.out.println("The list is empty.");
            return;
        }
        viewList();
        System.out.print("Enter index of item to move: ");
        int fromIndex = scanner.nextInt() - 1;
        System.out.print("Enter new position: ");
        int toIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline character

        if (fromIndex >= 0 && fromIndex < list.size() && toIndex >= 0 && toIndex <= list.size()) {
            String item = list.remove(fromIndex);
            list.add(toIndex, item);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid positions.");
        }
    }

    private static void openList(Scanner scanner) {
        if (needsToBeSaved) {
            System.out.print("Current list has unsaved changes. Save before opening a new list (Y/N)? ");
            char saveChoice = scanner.next().toUpperCase().charAt(0);
            scanner.nextLine(); // Consume newline character
            if (saveChoice == 'Y') {
                saveList(scanner);
            }
        }

        System.out.print("Enter filename to open: ");
        String filename = scanner.nextLine() + ".txt";
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            list.clear();
            while (fileScanner.hasNextLine()) {
                list.add(fileScanner.nextLine());
            }
            currentFileName = filename;
            needsToBeSaved = false;
            System.out.println("List loaded from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    private static void saveList(Scanner scanner) {
        if (list.isEmpty()) {
            System.out.println("The list is empty. Nothing to save.");
            return;
        }
        if (!needsToBeSaved) {
            System.out.println("No changes to save.");
            return;
        }
        if (currentFileName == null) {
            System.out.print("Enter basename for the list file: ");
            String basename = scanner.nextLine();
            currentFileName = basename + ".txt";
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(new File(currentFileName)))) {
            for (String item : list) {
                writer.println(item);
            }
            System.out.println("List saved successfully.");
            needsToBeSaved = false;
        } catch (IOException e) {
            System.out.println("Error saving list: " + e.getMessage());
        }
    }

    private static void clearList(Scanner scanner) {
        System.out.print("Are you sure you want to clear the list (Y/N)? ");
        char choice = scanner.next().toUpperCase().charAt(0);
        scanner.nextLine(); // Consume newline character
        if (choice == 'Y') {
            list.clear();
            needsToBeSaved = true;
            System.out.println("List cleared.");
        }
    }

    private static void quitProgram(Scanner scanner) {
        if (needsToBeSaved) {
            System.out.print("Current list has unsaved changes. Save before quitting (Y/N)? ");
            char choice = scanner.next().toUpperCase().charAt(0);
            scanner.nextLine(); // Consume newline character
            if (choice == 'Y') {
                saveList(scanner);
            }
        }
        System.out.println("Exiting program.");
    }
}