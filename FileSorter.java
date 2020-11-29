///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.5.2

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * {@link FileSorter} sorts files according to their extension (for example .java) 
 * into subfolders which are named after their extension (for example JAVA).
 * 
 * @author Marc Baumgartner marc@mabaka.ch
 */
@Command(name = "FileSorter", mixinStandardHelpOptions = true, version = "FileSorter 0.1", description = "Command line utility to sort a bunch of files by there extension into sub folders.")
class FileSorter implements Callable<Integer> {

    @Option(names = { "-i", "--input" }, required = true, description = "Path to the unsorted files")
    private File inputDirectory;

    @Option(names = { "-o",
            "--output" }, required = true, description = "Path where the sorted file should be copied to.")
    private File outputDirectory;

    @Option(names = { "-r", "--recursive" }, description = "Recursively process all subdirectories. Default = true")
    private boolean recursive;

    @Option(names = { "-v", "--verbose" }, description = "Enable verbose output. Default = false")
    private boolean verbose;

    public static void main(String... args) {
        int exitCode = new CommandLine(new FileSorter()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception { // your business logic goes here...
        if (checkDirectories()) {
            processDirectory(inputDirectory);
        }
        return 0;
    }

    private void processDirectory(File currentDirectory) {
        List<File> filesAndFolders = Arrays.asList(currentDirectory.listFiles());
        for (File file : filesAndFolders) {
            if (file.isDirectory() && recursive) {
                processDirectory(file);
            } else if (file.isFile()) {
                processFile(file);
            }
        }
    }

    private void processFile(File file) {
        try {
            int indexOfLastDot = file.getName().lastIndexOf(".");
            String fileExtension = "UNKNOWN";
            if (indexOfLastDot > 0 && indexOfLastDot < file.getName().length() - 1) {
                fileExtension = file.getName().substring(indexOfLastDot + 1);
            }
            File outputSubDir = new File(
                    outputDirectory.getAbsolutePath() + File.separatorChar + fileExtension.toUpperCase());
            if (!outputSubDir.exists()) {
                Files.createDirectories(outputSubDir.toPath());
            }
            if (!outputSubDir.isDirectory()) {
                System.err.println("Error: Output path " + outputSubDir + " is not a directory. ");
                System.exit(-2);
            }

            File outputFile = new File(outputSubDir.getAbsolutePath() + File.separatorChar + file.getName());
            if (outputFile.exists()) {
                System.out.println("Warning: " + outputFile.getAbsolutePath() + " exists -> Generate new name!");
                outputFile = new File(outputSubDir.getAbsolutePath() + File.separatorChar + UUID.randomUUID() + "." + fileExtension);
            }
            
            if (verbose) {
                System.out.println("Copy " + file + " to " + outputFile);
            }
            
            Files.copy(file.toPath(), outputFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-3);
        }
    }

    private boolean checkDirectories() {
        if (!inputDirectory.isDirectory()) {
            System.err.println("Error: " + inputDirectory + " is not a directory");
            return false;
        }
        if (!outputDirectory.exists()){
            if (!outputDirectory.mkdirs()) {
                System.err.println("Error: Could not create output directory " + outputDirectory);
                return false;
            }
        }
        if (!outputDirectory.isDirectory()){
            System.err.println("Error: " + outputDirectory + " is not a directory");
            return false;
        }
        return true;
    }
}
