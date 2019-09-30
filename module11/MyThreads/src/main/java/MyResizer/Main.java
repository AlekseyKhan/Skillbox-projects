package MyResizer;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Main {
    private static int newWidth = 300;

    public static void main(String[] args) {
        String srcFolder = "data/src";
        String dstFolder = "data/dst";

        File srcDir = new File(srcFolder);
        File[] files = srcDir.listFiles();

        int countOfCores = Runtime.getRuntime().availableProcessors();
        System.out.println("Процессоров в системе:  " + countOfCores);

        int part = files.length / countOfCores;
        int position = 0;
        int size;
        Set<ImageResizer> resizers = new HashSet<>();

        long start = System.currentTimeMillis();
        for (int i = 0; i < countOfCores; i++) {

            if (i == countOfCores - 1) {
                size = files.length - position;
            } else {
                size = part;
            }
            File[] partOfFiles = new File[size];
            System.arraycopy(files, position, partOfFiles, 0, size);
            position += part;

            resizers.add(new ImageResizer(partOfFiles, newWidth, dstFolder, start));
        }

        resizers.forEach(Thread::start);
    }
}
