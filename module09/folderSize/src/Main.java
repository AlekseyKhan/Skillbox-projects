import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final long basic = 1024;
    private static final long Kilo = basic;
    private static final long Mega = Kilo * basic;
    private static final long Giga = Mega * basic;

    public static void main(String[] args) throws IOException {
        String path = "data";

        System.out.println(showResult(calculateSize(path), path));
    }

    private static List<File> getAllFiles(String directoryName) throws IOException {
        File directory = new File(directoryName);
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

        for (File file : allFiles) {
            //System.out.println(file + " размер " +Files.size(Paths.get(file.getPath())));
            size += Files.size(Paths.get(file.getPath()));
        }

        return size;
    }

    private static String showResult(long size, String path) {
        float result;
        if (size > Giga) {
            result = (float) size / Giga;
            return String.format("Папка '%s' занимает %.2f ГБайт(%d байт)", Paths.get(path).toAbsolutePath(), result, size);
        } else if (size > Mega) {
            result = (float) size / Mega;
            return String.format("Папка '%s' занимает %.2f МБайт(%d байт)", Paths.get(path).toAbsolutePath(), result, size);
        } else if (size > Kilo) {
            result = (float) size / Kilo;
            return String.format("Папка '%s' занимает %.2f КБайт(%d байт)", Paths.get(path).toAbsolutePath(), result, size);
        } else {
            return String.format("Папка '%s' занимает %d байт", Paths.get(path).toAbsolutePath(), size);
        }

    }
}
