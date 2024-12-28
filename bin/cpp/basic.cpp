#include <iostream>
#include <windows.h>
#include <string>
#include <sstream>
#include <filesystem>

namespace fs = std::filesystem;

int main(int argc, char* argv[]) {
    // Get the current executable's directory
    char exePath[MAX_PATH];
    if (GetModuleFileName(NULL, exePath, MAX_PATH) == 0) {
        std::cerr << "Failed to get executable path" << std::endl;
        return 1;
    }

    // Convert to a std::string and get the directory part
    std::string exeDir = fs::path(exePath).parent_path().string();

    // Define the relative paths from the executable directory
    std::string jrePath = "java";  // Path to java.exe in the JRE folder
    std::string jarPath = exeDir + "\\psn.jar";            // Path to your JAR file

    // Start the command with the path to java.exe and the -jar option
    std::stringstream commandStream;
    commandStream << "\"" << jrePath << "\" -jar \"" << jarPath << "\"";

    // Add all the arguments passed to the C++ program (skip the first one which is the executable path)
    for (int i = 1; i < argc; ++i) {
        commandStream << " \"" << argv[i] << "\"";
    }

    // Convert the command to a C-string
    std::string command = commandStream.str();
    const char* commandCStr = command.c_str();

    // Execute the command using CreateProcess (Windows API)
    STARTUPINFO si = { sizeof(STARTUPINFO) };
    PROCESS_INFORMATION pi;

    if (CreateProcess(NULL, (LPSTR)commandCStr, NULL, NULL, FALSE, 0, NULL, NULL, &si, &pi)) {
        // Wait for the process to finish
        WaitForSingleObject(pi.hProcess, INFINITE);

        // Clean up process handles
        CloseHandle(pi.hProcess);
        CloseHandle(pi.hThread);
    } else {
        std::cerr << "Failed to execute command. Error: " << GetLastError() << std::endl;
    }

    return 0;
}
