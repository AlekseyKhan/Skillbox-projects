import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final long BASIC = 1024;
    private static final long KILO = BASIC;
    private static final long MEGA = KILO * BASIC;
    private static final long GIGA = MEGA * BASIC;

    public static void main(String[] args) throws IOException {
        String path = "data";

        System.out.println(showResult(calculateSize(path), path));
    }

    private static List<File> getAllFiles(String directoryName) throws IOException {
        File directory = new File(directoryName);

        if (!directory.exists()) {
            System.out.println(String.format("Папка '%s' не найдена", directoryName));
            return null;
        }

        List<File> resultList = new ArrayList<>();
        File[] fList = directory.listFiles();

        for (File file : fList) {
            if (file.isFile()) {
                resultList.add(file);
            } else if (file.isDirectory()) {
                resultList.addAll(getAllFiles(file.getPath()));
            }
        }

        return resultList;
    }

    private static long calculateSize(String path) throws IOException {
        long size = 0;

        List<File> allFiles = getAllFiles(path);

        if(allFiles == null) {
            System.out.println("Данные отсутствуют");
            return -1;
        }

        for (File file : allFiles) {
            //System.out.println(file + " размер " +Files.size(Paths.get(file.getPath())));
            size += Files.size(Paths.get(file.getPath()));
        }

        return size;
    }

    private static String showResult(long size, String path) {
        if (size < 0) {
            return "В работе произошел сбой";
        }

        float result;
        StringBuilder output = new StringBuilder("Папка '%s' занимает");
        if (size > GIGA) {
            result = (float) size / GIGA;
            output.append(" %.2f ГБайт(%d байт)");
        } else if (size > MEGA) {
            result = (float) size / MEGA;
            output.append(" %.2f МБайт(%d байт)");
        } else if (size > KILO) {
            result = (float) size / KILO;
            output.append(" %.2f КБайт(%d байт)");
        } else {
            output.append(" %d байт");
            return String.format(output.toString(), Paths.get(path).toAbsolutePath(), size);
        }

        return String.format(output.toString(), Paths.get(path).toAbsolutePath(), result, size);

    }
}
