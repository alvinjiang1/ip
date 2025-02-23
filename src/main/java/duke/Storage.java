package duke;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Storage handles the reading and writing of the TaskList to an output file.
 *
 * @author Alvin Jiang Min Jun
 * @version v0.2
 */
public class Storage {

    String filePath;
    final static String PATH = "./data";

    /**
     * Creates an instance of a Storage object.
     *
     * @param filePath The file path to which the storage can read and write files.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Create the output file if it does not exist,
     * and if it does, read the file and generate an ArraryList that
     * corresponds to its current contents.
     *
     * @return ArrayList of type String The output, whereby each line of the file
     * corresponds to entry in the ArrayList.
     */
    public ArrayList<String> load() {
        File directory = new File(PATH);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File file = new File(this.filePath);
        assert file != null : "file does not exist";
        try {
            file.createNewFile();
            ArrayList<String> result = new ArrayList<>();
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                result.add(data);
            }
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Write an input String to the file.
     *
     * @param str The input String to be added to the file.
     */
    public void writeFile(String str) {
        try {
            FileWriter fw = new FileWriter(this.filePath);
            fw.write(str);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
