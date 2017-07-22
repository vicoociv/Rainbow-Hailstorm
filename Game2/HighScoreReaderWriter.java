import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class HighScoreReaderWriter {

    private FileWriter fileWriter;
    private BufferedReader fileReader;
    String fileName;

    // use linked list here because can have duplicates. Also order matters
    private LinkedList<int[]> highScores = new LinkedList<int[]>();
    private LinkedList<String> highScoresText = new LinkedList<String>();


    public HighScoreReaderWriter(File file) {
        fileName = file.getName();
        try {
            fileWriter = new FileWriter(fileName, true);
            fileReader = new BufferedReader(new FileReader(fileName));
            updateScoreLists();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //updates the score lists
    public void updateScoreLists() {
        //populates list from file
        try {
            highScores.clear();
            highScoresText.clear();
            
            while (fileReader.ready()) {
                String line = fileReader.readLine();
                String[] tempTextArray = line.split(" ");

                int tempScore = Integer.parseInt(tempTextArray[0]);
                int tempIncorrect = Integer.parseInt(tempTextArray[1]);
                int tempTraps = Integer.parseInt(tempTextArray[2]);
                int[] tempArray = {tempScore, tempIncorrect, tempTraps};
                
                highScores.add(tempArray);
                highScoresText.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //updates the file itself
    private void updateScore(int index, int score, String text, int incorrect, int traps) {
        int[] tempArray = {score, incorrect, traps};
        highScores.add(index, tempArray);
        highScoresText.add(index, text);

        //keeps only the top ten
        if (highScores.size() > 10) {
            highScores.removeLast();
            highScoresText.removeLast();
        }
        
        // overrides the existing file whenever a score is added
        try {
            for (int i = 0; i < highScoresText.size(); i++) {
                fileWriter.write(highScoresText.get(i) + "\n");
            }
            closeEverything();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //closes the readers and writers
    public void closeEverything() {
        try {
            fileWriter.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //clears the file
    public void resetFileReaderWriter() {
        try {
            fileWriter = new FileWriter(fileName, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // sees if the provided score is in the top ten
    private boolean checkTopTen(int score) {
        boolean result = false;
        if (highScores.size() < 10) {
            result = true;
        } else {
            for (int i = 0; i < highScores.size(); i++) {
                if (score > highScores.get(i)[0]) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    // checks index the new high score is to be inserted into
    private int checkIndex(int score) {
        int result = highScores.size();

        for (int i = 0; i < highScores.size(); i++) {
            if (score > highScores.get(i)[0]) {
                return i;
            }
        }
        return result;
    }

    // adds the info the user wants to store
    public void addInfo(int score, String text, int incorrect, int traps) {
        if (checkTopTen(score)) {
            updateScore(checkIndex(score), score, text, incorrect, traps);
        }
    }

    //gets the high scores
    public LinkedList<String> getHighScoreInfo() {
        LinkedList<String> resultList = new LinkedList<String>();
        for (int i = 0; i < highScores.size(); i++) {
            int[] tempScores = highScores.get(i);
            String date = highScoresText.get(i).split(" ")[3];
            String lineOutput = "Time: " + date + "   Score: " + tempScores[0] + "   Incorrect: " + tempScores[1] + "   Traps Hit: " + tempScores[2];
            resultList.add(lineOutput);
        }
        return resultList;
    }
    
    //testing use
    public LinkedList<int[]> getScores() {
        return highScores;
    }
}
