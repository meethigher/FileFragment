package top.meethigher;

import top.meethigher.utils.FileFragment;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 启动类
 *
 * @author chenchuancheng
 * @since 2024/06/20 18:02
 */
public class App {

    private static final FileFragment fileFragment = new FileFragment();

    private static final Map<String, String> commandMap = new LinkedHashMap<>();

    static {
        commandMap.put("h", "Get all command descriptions");
        commandMap.put("s", "Split a file into part files");
        commandMap.put("m", "Merge part files into a single file");
        commandMap.put("q", "Quit");
    }


    public static void main(String[] args) {
        System.out.println("Welcom to FileFragment (https://github.com/meethigher/FileFragment)");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.print("Command (h for help): ");
            String command = scanner.next();
            if ("q".equals(command)) {
                System.out.println("FileFragment (https://github.com/meethigher/FileFragment) is happy to serve you. ");
                break;
            }
            switch (command) {
                case "h":
                    System.out.println("The currently supported command list is as follows");
                    for (String key : commandMap.keySet()) {
                        System.out.println(key + ": " + commandMap.get(key));
                    }
                    break;
                case "s":
                    System.out.print("Enter file path: ");
                    String filePath = scanner.next();
                    System.out.print("Enter the size of the part file (MB): ");
                    int partSize = scanner.nextInt();
                    fileFragment.splitFile(filePath, partSize * 1024 * 1024L);
                    break;
                case "m":
                    System.out.print("Enter directory path: ");
                    String directoryPath = scanner.next();
                    fileFragment.mergeFiles(directoryPath);
                    break;
                default:
                    System.out.println("Unsupported command (h for help)");
            }
        }
    }
}
