package top.meethigher;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class App {

    private static final FileFragment fileFragment = new FileFragment();

    private static final Map<String, String> commandMap = new LinkedHashMap<>();

    static {
        commandMap.put("h", "Get all command descriptions");
        commandMap.put("s", "Split a file into part files");
        commandMap.put("m", "Merge part files into a single file");
        commandMap.put("q", "Quit");
    }

    // 解析文件大小的方法
    private static long parseSize(String sizeInput) {
        sizeInput = sizeInput.trim().toUpperCase();
        if (sizeInput.endsWith("KB")) {
            return Long.parseLong(sizeInput.substring(0, sizeInput.length() - 2)) * 1024L;
        } else if (sizeInput.endsWith("MB")) {
            return Long.parseLong(sizeInput.substring(0, sizeInput.length() - 2)) * 1024L * 1024L;
        } else if (sizeInput.endsWith("GB")) {
            return Long.parseLong(sizeInput.substring(0, sizeInput.length() - 2)) * 1024L * 1024L * 1024L;
        } else if (sizeInput.endsWith("B")) {
            return Long.parseLong(sizeInput.substring(0, sizeInput.length() - 1));
        } else {
            throw new NumberFormatException();
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to FileFragment (https://github.com/meethigher/FileFragment)");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.print("Command (h for help): ");
            String command = scanner.nextLine().trim().toLowerCase();
            if ("q".equals(command)) {
                System.out.println("Thank you for using FileFragment (https://github.com/meethigher/FileFragment). Have a great day!");
                break;
            }
            switch (command) {
                case "h":
                    System.out.println("The currently supported command list is as follows:");
                    for (Map.Entry<String, String> entry : commandMap.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }
                    break;
                case "s":
                    System.out.print("Please enter the path of the file you want to split (e.g., C:/example.txt): ");
                    String filePath = scanner.nextLine().trim();
                    System.out.print("Please enter the size of each part file with unit (e.g., 1B, 1KB, 1MB, 1GB): ");
                    String sizeInput = scanner.nextLine().trim();
                    long partSize;
                    try {
                        partSize = parseSize(sizeInput);
                        if (partSize <= 0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException e) {
                        log.error("Invalid input. Please enter a positive number with unit (B, KB, MB, GB).");
                        break;
                    }
                    fileFragment.splitFile(filePath, partSize);
                    break;
                case "m":
                    System.out.println("The original file will be deleted after a successful merge.");
                    System.out.print("Please enter the directory path containing the part files (e.g., C:/parts): ");
                    String directoryPath = scanner.nextLine().trim();
                    fileFragment.mergeFiles(directoryPath);
                    break;
                default:
                    log.warn("Unsupported command. Please enter a valid command (h for help).");
            }
        }
    }
}
