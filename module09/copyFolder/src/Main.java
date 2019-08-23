import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String pathFrom = "data/from";
        String pathTo = "data/to";

        try {
            copyFolder(pathFrom, pathTo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void copyFolder(String from, String to) throws IOException {
        File source = new File(from);
        if (!source.exists()) {
            throw new RuntimeException(String.format("Сбой. Путь %s не существует", source));
        }

        File dest = new File(to);
        if (!dest.exists()) {
            throw new RuntimeException(String.format("Сбой. Путь %s не существует", dest));
        }

        Path destFolder = Paths.get(to + File.separator + source.getName());
        Path commonPath = Paths.get(source.getCanonicalPath());
        for (File file : new ArrayList<>(getAllFiles(source))) {

            Path filePath = Paths.get(file.getAbsolutePath());
            Path relativePath = commonPath.relativize(filePath);
            Path destPath = destFolder.resolve(relativePath);

            File destFile = new File(destPath.toString());
            File sourceFile = new File(filePath.toString());

            if (!file.exists()) {
                System.out.println(String.format("Не удалось скопировать %s", file));
                continue;
            }

            if (file.isFile()) {
                copyFileUsingTransfer(sourceFile, destFile);
            } else if (!destFile.exists()) {
                destFile.mkdirs();
            }
        }
        System.out.println("Копирование завершено");
    }

    private static List<File> getAllFiles(File directory) throws IOException {
        if (directory == null) {
            System.out.println("Ошибка. directory ссылается на null");
            throw new RuntimeException("");
        }
        if (!directory.exists()) {
            System.out.println(String.format("Папка '%s' не найдена", directory.getName()));
            return Collections.emptyList();
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

    private static void copyFileUsingTransfer(File src, File dst) throws IOException {
        try (InputStream is = new FileInputStream(src);
            OutputStream os = new FileOutputStream(dst))
        {
            is.transferTo(os);
        } catch (Exception e) {
            System.out.println(String.format(
                    "Входные данные:\nsrc=%s\ndst=%s",
                    src,
                    dst
            ));
            e.printStackTrace();
        }
    }

}
