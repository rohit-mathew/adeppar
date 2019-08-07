package mergefiles;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Application {

    class StringWithIndex {
        String str;
        int index;

        public StringWithIndex(String str, int index) {
            this.str = str;
            this.index = index;
        }
    }

    class StringComparator implements Comparator<StringWithIndex> {
        public int compare(StringWithIndex c1, StringWithIndex c2)
        {
            return c1.str.compareTo(c2.str);
        }
    }

    BufferedWriter outputWriter;
    List<Scanner> scanners;
    PriorityQueue<StringWithIndex> queue = new PriorityQueue<>();

    public Application(String inputPath, String outputPath) {

        try {
            outputWriter = new BufferedWriter(new FileWriter(outputPath, true));
        } catch (IOException ioe) {
            throw new InputMismatchException("Invalid output path");
        }

        try {
            List<File> filesInFolder = Files.walk(Paths.get(inputPath))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            scanners = new ArrayList<>();

            for (File file : filesInFolder) {
                scanners.add(new Scanner(new FileInputStream(file.getPath()), "UTF-8"));
            }
        } catch (IOException ioe) {
            throw new InputMismatchException("Invalid input path");
        }
        queue = new PriorityQueue<>(scanners.size(), new StringComparator());
    }

//    private boolean verifyFiles() {
//        for (File file : filesInFolder) {
//
//        }
//
//        return false;
//    }

    private void writeToFile(String line) throws IOException {

            outputWriter.write(line);
            outputWriter.newLine();
            outputWriter.flush();
    }

    private StringWithIndex getStringFromFile(int index) {

        Scanner scanner = scanners.get(index);

        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            if (currentLine.isEmpty()) {
                continue;
            } else return new StringWithIndex(currentLine, index);
        }
        return null;
    }

    private void populatePriorityQueue() {

        for (int i = 0; i < scanners.size(); i++) {
            queue.add(getStringFromFile(i));
        }
    }

    private void process() throws IOException {

        populatePriorityQueue();
        String previousLine = null;

        while (!queue.isEmpty()) {
            StringWithIndex current = queue.poll();
            if (previousLine != null && current.str.compareTo(previousLine) < -1) {
                System.out.println("Error: Looks like the input is not sorted");
                System.out.println("Erroring string: " + current.str);
                System.out.println("Previous string: " + previousLine);
                System.exit(1);
            }
            if(!current.str.equals(previousLine)) writeToFile(current.str);
            previousLine = current.str;
            StringWithIndex next = getStringFromFile(current.index);
            if (next != null) queue.add(next);
        }
        outputWriter.close();
    }

    public static void main(String[] args) {

        String inputpath = args[0];
        String outputPath = args[1];
        Application app = new Application(inputpath, outputPath);
        try {
            app.process();
        } catch (IOException ioe) {
            System.out.println("IOException: Please check input and output paths");
            System.exit(1);
        }
    }
}
