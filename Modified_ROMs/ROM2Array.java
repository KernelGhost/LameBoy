// Name: ROM2Array.java
// Role: A simple Java program to represent a hexadecimal data string in a format compatible with declaring a C++ byte array.
// Usage:
//   1. Run the program directly using: `java /path/to/ROM2Array.java /path/to/input/file.ch8` (No need to compile separately in Java 11+).
//   2. Copy and paste the output into your C++ project.
//
// Note:
// This program was implemented in Java for historical reasons.
// There are more efficient ways to achieve this operation, especially using scripting languages better suited for byte manipulation (e.g. Bash, Python, etc.).
// If you have the time and interest, feel free to submit a pull request featuring an implementation in a more suitable language.

// Import relevant classes.
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class Main {
	  public static void main(String[] args) {
        // Check if a file path argument was provided.
        if (args.length < 1) {
            System.err.println("ERROR: No input file specified.");
            System.err.println("Usage: java /path/to/ROM2Array.java <file-path>");
            System.exit(1);
        }

        // Get the file path from the first command line argument.
        String filePath = args[0];

        // Initialise the fileBytes variable.
        byte[] fileBytes = {};

        // Read the file content.
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            fileBytes = fis.readAllBytes();
        } catch (IOException e) {
            System.err.println("ERROR: Could not read file '" + args[0] + "'.");
            System.err.println(e.getMessage());
            System.exit(2);
        }

        // Extract the file name for the array name.
        File file = new File(filePath);
        String fileName = file.getName();
        String arrayName = fileName.substring(0, fileName.lastIndexOf("."));
        arrayName = arrayName.replace(" ", "_");

        // Store the beginning of the C++ byte array declaration using the user-specified array name.
        String strOutput = "const byte " + arrayName + "[] PROGMEM = {\n    ";

        // Loop through the character array to begin building the body of the C++ byte array declaration.
        for (int intCtr = 0; intCtr < fileBytes.length; intCtr++) {
            strOutput += ("(byte) 0x" + String.format("%02X", fileBytes[intCtr]));

            // Add a comma and space for clarity and visual separation between bytes.
            if (intCtr + 1 < fileBytes.length) {
                strOutput += ", ";
            }

            // Print a new line (with indentation) after every 8 bytes for improved readability.
            if (((intCtr + 1) % 8 == 0) && (intCtr + 1 < fileBytes.length)) {
                strOutput += "\n    ";
            }
        }

        // Close the C++ byte array declaration.
        strOutput += "\n};";

        // Output the final C++ byte array declaration string to the console.
        System.out.println(strOutput);
	  }
}
