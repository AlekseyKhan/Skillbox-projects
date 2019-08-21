import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String pathFrom = "data/from";
        String pathTo = "data/to";

        copyFolder(pathFrom, pathTo);
    }

    private static void copyFolder(String from, String to) throws IOException {
        File source = new File(from);

        if (!source.exists()) {
//            throw new RuntimeException("Сбой. Нет источника копирования.");
            System.out.println("Не найдена папка для копирования");
            return;
        }

        String destFolderName = to + File.separator + source.getName();
        File dest = new File(to);

        if (!dest.exists()) {
//            throw new RuntimeException("Сбой. Папка назначения отсутствует.");
            System.out.println("Не найдена папка назначения");
            return;
        }

        List<File> allFiles = getAllFiles(source);
        String commonPath = source.getCanonicalPath();
//        System.out.println(commonPath);
        for (File file : allFiles) {

            String filePath = file.getAbsolutePath();
            String destPath = destFolderName + filePath.substring(commonPath.length());
            //System.out.println(destPath);

            File destFile = new File(destPath);
            File sourceFile = new File(filePath);

            if (file.isFile()) {
                copyFileUsingStream(sourceFile, destFile);
            } else if (!destFile.exists()) {
                destFile.mkdirs();
            }
        }
        System.out.println("Копирование завершено");
    }

    private static List<File> getAllFiles(File directory) throws IOException {

        if (!directory.exists()) {
            System.out.println(String.format("Папка '%s' не найдена", directory.getName()));
            return null;
        }

        List<File> resultList = new ArrayList<>();
        File[] fList = directory.listFiles();

        resultList.add(directory);
        for (File file : fList) {
            resultList.add(file);
            if (file.isDirectory()) {
                resultList.addAll(getAllFiles(file));
            }
        }

        return resultList;
    }

    private static void copyFileUsingStream(File src, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;

        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int piece;
            while ((piece = is.read(buffer)) > 0) {
                os.write(buffer, 0, piece);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    private static void copyFileUsingFiles(File src, File dst) throws IOException {
        Files.copy(src.toPath(), dst.toPath());
    }


}
