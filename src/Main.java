import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int count = 0;
        while (true) {
            System.out.println("Введите путь к файлу:");
            String path = new Scanner(System.in).nextLine();
            if (path.equals("exit")) {
                return;
            }
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (!fileExists) {
                System.out.println("Это не путь к файлу");
                continue;
            } else if (isDirectory) {
                System.out.println("Это путь к папке, а не файлу");
                continue;
            }
            System.out.println("Путь указан верно");
            count++;
            System.out.println("Это файл номер " + count);
            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);
                String line;
                int lineCount = 0;
                int yandexCount = 0;
                int googleCount = 0;
                while ((line = reader.readLine()) != null) {
                    lineCount++;
                    int length = line.length();
                    if (length > 1024) {
                        throw new TooLongLineException("Длина строки больше 1024");
                    }
                    if (line.contains("(") && line.contains(")") && line.indexOf('(') < line.indexOf(')')) {
                        String firstBrackets = line.substring(line.indexOf('('), line.indexOf(')'));
                        String[] parts = firstBrackets.split(";");
                        if (parts.length >= 2) {
                            String fragment = parts[1].trim();
                            if (fragment.contains("/")) {
                                String program = fragment.substring(0, fragment.indexOf('/'));
                                switch (program) {
                                    case "YandexBot" -> yandexCount++;
                                    case "Googlebot" -> googleCount++;
                                }
                            }
                        }
                    }
                }
                System.out.println("Общее количество строк в файле: " + lineCount);
                System.out.println("Доля запросов от YandexBot: " + String.format("%.3f", (double) yandexCount * 100 / lineCount) + "%");
                System.out.println("Доля запросов от Googlebot: " + String.format("%.3f", (double) googleCount * 100 / lineCount) + "%");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}