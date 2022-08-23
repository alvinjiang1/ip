import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;


public class Duke {
    private static ArrayList<Task> taskList = new ArrayList<>();
    final static String PATH = "./data";
    final static String FILENAME = "./data/duke.txt";
    final static String LOGO =
            "   __ __    ____       ___               __\n" +
                    "  / // /__ / / /__    / _ \\___ ____  ___/ /__ _\n" +
                    " / _  / -_) / / _ \\  / ___/ _ `/ _ \\/ _  / _ `/\n" +
                    "/_//_/\\__/_/_/\\___/ /_/   \\_,_/_//_/\\_,_/\\_,_/\n";

    private static void markTask(int i) {
        if (i >= 100) return;
        if (taskList.get(i) == null) return;
        taskList.get(i).doTask();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + taskList.get(i));
    }

    private static void unmarkTask(int i) {
        if (i >= 100) return;
        if (taskList.get(i) == null) return;
        taskList.get(i).undoTask();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + taskList.get(i));
    }

    private static void deleteTask(int i) {
        if (i >= 100) return;
        if (taskList.get(i) == null) return;
        Task t = taskList.get(i);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + t);
        taskList.remove(i);
        System.out.println("Now you have " + taskList.size() + " tasks in the list.");
    }

    private static void displayList() {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i) == null) break;
            System.out.println(i + 1 + ". " + taskList.get(i));
        }
    }

    private static String getList() {
        String result = "";
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i) == null) break;
            result += taskList.get(i).toString() + "\n" ;
        }
        return result;
    }

    private static void writeFile(String str) {
        try {
            FileWriter fw = new FileWriter(FILENAME);
            fw.write(str);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readFile(File file) {
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                char type = data.charAt(1);
                if (type == 'T') {
                    String description = data.substring(7);
                    taskList.add(new Todo(description));
                } else if (type == 'D') {
                    String temp = data.substring(7);
                    String description = temp.split(" \\(by: ")[0];
                    String temp2 = temp.split(" \\(by: ")[1];
                    String by = temp2.substring(0, temp2.length() - 1);
                    LocalDate date = LocalDate.parse(by, DateTimeFormatter.ofPattern("MMM dd yyyy"));
                    taskList.add(new Deadline(description, date.toString()));
                } else {
                    String temp = data.substring(7);
                    String description = temp.split(" \\(at: ")[0];
                    String temp2 = temp.split(" \\(at: ")[1];
                    String at = temp2.substring(0, temp2.length() - 1);
                    LocalDate date = LocalDate.parse(at, DateTimeFormatter.ofPattern("MMM dd yyyy"));
                    taskList.add(new Event(description, date.toString()));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void createFile() {

        File directory = new File(PATH);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File file = new File(FILENAME);
        try {
            file.createNewFile();
            readFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void beginBot() throws DukeException {
            System.out.println("Hello from\n" + LOGO);
            Scanner sc = new Scanner(System.in);
            System.out.println("How can I help you today? :)");
            String command = sc.nextLine();
        try {
            while (!command.equals("bye")) {
                if (command.equals("list")) {
                    displayList();
                    command = sc.nextLine();
                    continue;
                } else if (command.contains("unmark")) {
                    String s = command.substring(7);
                    int i = Integer.parseInt(s);
                    unmarkTask(i - 1);
                    writeFile(getList());
                    command = sc.nextLine();
                    continue;
                } else if (command.contains("mark")) {
                    String s = command.substring(5);
                    int i = Integer.parseInt(s);
                    markTask(i - 1);
                    writeFile(getList());
                    command = sc.nextLine();
                    continue;
                } else if (command.contains("delete")) {
                    String s = command.substring(7);
                    int i = Integer.parseInt(s);
                    deleteTask(i - 1);
                    writeFile(getList());
                    command = sc.nextLine();
                    continue;
                } else if (command.contains("deadline")) {
                    if (command.length() == 8) throw new DukeException("OOPS!!! I'm sorry but description of a deadline cannot be empty");
                    String s = command.substring(9);
                    String[] result = s.split(" /by ");
                    Deadline d = new Deadline(result[0], result[1]);
                    taskList = d.printAndStoreTask(taskList);
                    writeFile(getList());
                    command = sc.nextLine();
                    continue;
                } else if (command.contains("todo")) {
                    if (command.length() == 4) throw new DukeException("OOPS!!! I'm sorry but description of a todo cannot be empty");
                    String s = command.substring(5);
                    if (s == " ") throw new DukeException("OOPS!!! I'm sorry but description of a todo cannot be empty");
                    Todo t = new Todo(s);
                    taskList = t.printAndStoreTask(taskList);
                    writeFile(getList());
                    command = sc.nextLine();
                    continue;
                } else if (command.contains("event")) {
                    if (command.length() == 5) throw new DukeException("OOPS!!! I'm sorry but description or time period of an event cannot be empty");
                    String s = command.substring(6);
                    String[] result = s.split(" /at ");
                    Event e = new Event(result[0], result[1]);
                    e.printAndStoreTask(taskList);
                    writeFile(getList());
                    command = sc.nextLine();
                    continue;
                }
                throw new DukeException("OOPS!!! I'm sorry but I don't know what that means :-(");
            }
            sc.close();
            System.out.println("Bye. Hope to see you again soon!");
        } catch (DukeException e) {
            System.out.println(e.getMessage());
        } finally {
            sc.close();
        }

    }

    public static void main(String[] args) {
        try {
            createFile();
            beginBot();
        } catch (DukeException e) {
            e.printStackTrace();
        }
    }

}
