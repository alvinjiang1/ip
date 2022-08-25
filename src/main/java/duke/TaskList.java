package duke;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TaskList {

    private ArrayList<Task> tasks;

    public TaskList(ArrayList<String> taskList) {
        if (taskList.isEmpty()) tasks = new ArrayList<>();
        ArrayList<Task> result = new ArrayList<>();
        for (String str : taskList) {
            char type = str.charAt(1);
            if (type == 'T') {
                String description = str.substring(7);
                result.add(new Todo(description));
            } else if (type == 'D') {
                String temp = str.substring(7);
                String description = temp.split(" \\(by: ")[0];
                String temp2 = temp.split(" \\(by: ")[1];
                String by = temp2.substring(0, temp2.length() - 1);
                LocalDate date = LocalDate.parse(by, DateTimeFormatter.ofPattern("MMM dd yyyy"));
                result.add(new Deadline(description, date.toString()));
            } else {
                String temp = str.substring(7);
                String description = temp.split(" \\(at: ")[0];
                String temp2 = temp.split(" \\(at: ")[1];
                String at = temp2.substring(0, temp2.length() - 1);
                LocalDate date = LocalDate.parse(at, DateTimeFormatter.ofPattern("MMM dd yyyy"));
                result.add(new Event(description, date.toString()));
            }
        }
        this.tasks = result;
    }

    public String displayList() {
        String result = "";
        for (int i = 0; i < this.tasks.size(); i++) {
            if (this.tasks.get(i) == null) break;
            result += i + 1 + ". " + this.tasks.get(i) + "\n";
        }
        return result;
    }

    public String getList() {
        String result = "";
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i) == null) break;
            result += tasks.get(i).toString() + "\n" ;
        }
        return result;
    }

    public void markTask(int i) {
        if (i >= 100) return;
        if (this.tasks.get(i) == null) return;
        this.tasks.get(i).doTask();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + this.tasks.get(i));
    }
    public void unmarkTask(int i) {
        if (i >= 100) return;
        if (this.tasks.get(i) == null) return;
        this.tasks.get(i).undoTask();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + this.tasks.get(i));
    }

    public void deleteTask(int i) {
        if (i >= 100) return;
        if (this.tasks.get(i) == null) return;
        Task t = this.tasks.get(i);
        this.tasks.remove(i);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + this.tasks.size() + " tasks in the list.");
    }

    public void addTask(Task t) {
        this.tasks = t.printAndStoreTask(this.tasks);
    }

    public void findTask(String keyword) {
        String result = "";
        int counter = 1;
        for (int i = 0; i < this.tasks.size(); i++) {
            if (this.tasks.get(i).match(keyword)) {
                result += counter + ". " + this.tasks.get(i) + "\n";
                counter++;
            }
        }
        if (result.isEmpty()) {
            System.out.println("Sorry! None of the tasks match " + "\"" + keyword + "\"");
        } else {
            System.out.println("Here are the matching tasks in your list:\n" + result);
        }
    }

}
