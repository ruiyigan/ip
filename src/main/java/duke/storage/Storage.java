package duke.storage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import duke.exception.DukeException;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.ToDo;
import duke.tasklist.TaskList;



/**
 *  duke.storage.Storage to handle loading and saving of duke.tasklist.TaskList
 *  (before interacting with user and after interacting with user)
 */
public class Storage {
    private final String filePath;
    /**
     *  Constructor to create data storage object
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves data from duke.tasklist.TaskList and store into file specified by this.filePath
     *
     * @params list to be saved
     */
    public void saveData(TaskList taskList) throws DukeException {
        try {
            FileWriter fileWriter = new FileWriter(this.filePath);
            for (int i = 0; i < taskList.getSize(); i++) {
                Task task = taskList.getTask(i);
                StringBuilder taskString = new StringBuilder();
                taskString.append(task.getEventType());
                taskString.append(" | " + task.getStatusIcon() + " | ");
                taskString.append(task.getStorageDetails() + "\n");
                fileWriter.write(taskString.toString());
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new DukeException(e.getMessage());
        }
    }

    /**
     *  Load data from file path and stores it in an ArrayList for duke.main.Duke
     *
     * @return ArrayList containing tasks from file based on file path
     */

    public ArrayList<Task> loadData() throws DukeException {
        ArrayList<Task> taskList = new ArrayList<>();
        File file = new File(this.filePath);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdir();
                file.createNewFile();
            }
            Scanner fileReader = new Scanner(file);
            Task task;
            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                String[] dataArray = data.split("\\| ");
                String eventType = dataArray[0].trim();
                String isDone = dataArray[1].trim();
                if (eventType.equals("T")) {
                    task = new ToDo(dataArray[2].trim());
                } else if (eventType.equals("D")) {
                    LocalDate deadline = LocalDate.parse(dataArray[3].trim());
                    task = new Deadline(dataArray[2].trim(), deadline);
                } else {
                    String[] eventDetails = dataArray[3].split("- ");
                    LocalDate startDate = LocalDate.parse(eventDetails[0].trim());
                    LocalDate endDate = LocalDate.parse(eventDetails[1].trim());
                    task = new Event(dataArray[2].trim(), startDate, endDate);
                }
                if (isDone.equals("X")) {
                    task.markAsDone();
                }
                taskList.add(task);
            }
        } catch (IOException e) {
            throw new DukeException(e.getMessage());
        }
        return taskList;
    }
}
