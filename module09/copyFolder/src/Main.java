import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String pathFrom = "C:/java";
        String pathTo = "data/to";

        copyFolder(pathFrom, pathTo);
    }

    private static void copyFolder(String from, String to) throws IOException {
        File source = new File(from);
        if (!source.exists()) {
            throw new RuntimeException("Сбой. Нет источника копирования.");
        }

        File dest = new File(to);
        if (!dest.exists()) {
            throw new RuntimeException("Сбой. Папка назначения отсутствует.");
        }

        List<File> allFiles = getAllFiles(source);
        Path destFolder = Paths.get(to + File.separator + source.getName());
        Path commonPath = Paths.get(source.getCanonicalPath());
        for (File file : allFiles) {

            Path filePath = Paths.get(file.getAbsolutePath());
            Path relativePath = commonPath.relativize(filePath);
            Path destPath = destFolder.resolve(relativePath);

            File destFile = new File(destPath.toString());
            File sourceFile = new File(filePath.toString());

            if (file.isFile()) {
                copyFileUsingTransfer(sourceFile, destFile);
            } else if (!destFile.exists()) {
                destFile.mkdirs();
            }
        }
        System.out.println("Копирование завершено");
    }

    private static List<File> getAllFiles(File directory) throws IOException {

        if (!directory.exists() || directory == null) {
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
        try (InputStream is = new FileInputStream(src);
             OutputStream os = new FileOutputStream(dest)
        ) {
            byte[] buffer = new byte[1024];
            int piece;
            while ((piece = is.read(buffer)) > 0) {
                os.write(buffer, 0, piece);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copyFileUsingFiles(File src, File dst) throws IOException {
        Files.copy(src.toPath(), dst.toPath());
    }

    private static void copyFileUsingTransfer(File src, File dst) throws IOException {
        try (InputStream is = new FileInputStream(src);
            OutputStream os = new FileOutputStream(dst))
        {
            is.transferTo(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
