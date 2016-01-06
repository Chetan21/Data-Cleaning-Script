
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class BigData {

    public static BigData listFilesUtil = new BigData();
    public static final int YEAR_LOWER_BOUND = 15;
    public static final int YEAR_UPPER_BOUND = 19;
    public static final int LATTITUDE_LOWER_BOUND = 28;
    public static final int LATTITUDE_UPPER_BOUND = 34;
    public static final int LONGITUDE_LOWER_BOUND = 34;
    public static final int LONGITUDE_UPPER_BOUND = 41;
    public static final int TEMPERATURE_LOWER_BOUND = 87;
    public static final int TEMPERATURE_UPPER_BOUND = 92;
    public static final int MIN_YEAR = 1900;
    public static final int MAX_YEAR = 2015;
    public static final int MIN_LAT = -90;
    public static final int MAX_LAT = 90;
    public static final int MIN_LNG = -180;
    public static final int MAX_LNG = 180;
    public static final double MIN_TMP = -93.2;
    public static final double MAX_TMP = 61.8;
    private static final String COMMA_DELIMITER = ";";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "year;lattitude;longitude;temperature";
    private static final String INPUT_PARENT_DIRECTORY = "F:/filesbd";
    private static final String OUTPUT_DIRECTORY = "F:/filesbd_output/";

    public void listFilesAndFolders(String directoryName) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            System.out.println("--" + file.getName());
            listFilesUtil.listFiles(directoryName.concat("/").concat(file.getName()));
        }
    }

    public void listFiles(String directoryName) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList) {

            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
    }

    public void listFolders(String directoryName) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isDirectory()) {

                System.out.println(file.getName());
            }
        }
    }

    public void listFilesAndFilesSubDirectories(String directoryName) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                System.out.println("--" + file.getAbsolutePath() + "--" + file.getName());
                generateDataCSVFile(file.getAbsolutePath(), file.getName());
            } else if (file.isDirectory()) {
                listFilesAndFilesSubDirectories(file.getAbsolutePath());
            }
        }
    }

    public void generateDataCSVFile(String filePath, String fileName) {
        FileWriter fileWriter = null;
        String readRow = "";
        String yearString = "";
        String latString = "";
        String lngString = "";
        String tmpString = "";

        int year = 0;
        double lattitude, longitude, temperature;

        try {
            fileWriter = new FileWriter(OUTPUT_DIRECTORY.concat(fileName).concat(".csv"));
            fileWriter.append(FILE_HEADER.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);

            FileInputStream file = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(file));

            while ((readRow = br.readLine()) != null) {
                try {
                    yearString = readRow.substring(YEAR_LOWER_BOUND, YEAR_UPPER_BOUND);
                    year = Integer.parseInt(yearString);
                    if (year < MIN_YEAR || year > MAX_YEAR) {
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }

                latString = readRow.substring(LATTITUDE_LOWER_BOUND, LATTITUDE_UPPER_BOUND);
                latString = latString.substring(0, 3) + "." + latString.substring(3, latString.length());
                try {
                    lattitude = Double.parseDouble(latString);
                    if (lattitude < MIN_LAT || lattitude > MAX_LAT) {
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }

                lngString = readRow.substring(LONGITUDE_LOWER_BOUND, LONGITUDE_UPPER_BOUND);
                lngString = lngString.substring(0, 4) + "." + lngString.substring(4, lngString.length());
                try {
                    longitude = Double.parseDouble(lngString);
                    if (longitude < MIN_LNG || longitude > MAX_LNG) {
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }

                tmpString = readRow.substring(TEMPERATURE_LOWER_BOUND, TEMPERATURE_UPPER_BOUND);
                tmpString = tmpString.substring(0, 4) + "." + tmpString.substring(4, tmpString.length());
                try {
                    temperature = Double.parseDouble(tmpString);
                    if (temperature < MIN_TMP || temperature > MAX_TMP || readRow.charAt(92) == '3' || readRow.charAt(92) == '7') {
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }
                fileWriter.append(String.valueOf(year));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(latString);
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(lngString);
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(tmpString);
                fileWriter.append(NEW_LINE_SEPARATOR);

            }


            System.out.println("CSV file was created successfully !!!");


        } catch (IOException ex) {
            System.out.println("IO Exception during generateDataCSVFile : " + ex.getMessage());
            ex.printStackTrace();

        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }



    }

    public static void main(String[] args) {

        final String directoryWindows = INPUT_PARENT_DIRECTORY;
        listFilesUtil.listFilesAndFilesSubDirectories(directoryWindows);
    }
}
