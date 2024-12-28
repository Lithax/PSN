#include <iostream>
#include <cstdlib>  // For system()

int main(int argc, char *argv[]) {
    if (argc < 2) {
        std::cerr << "Error: No file specified. Usage: psn_display <filename>" << std::endl;
        return 1;
    }

    // Construct the command to run in the new cmd window
    std::string command = "cmd /c \"psn --common -display \"" + std::string(argv[1]) + "\"\"";

    // Output the command for debugging purposes
    std::cout << "Running command: " << command << std::endl;

    // Run the command in a new terminal window and wait for completion
    int result = system(command.c_str());

    if (result != 0) {
        std::cerr << "Error: Command failed with exit code " << result << std::endl;
        return 1;
    }

    return 0;
}
