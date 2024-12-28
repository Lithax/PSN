package psn;

import java.io.File;
import java.io.IOException;

public class Psn {
    public static void main(String[] args) {
        if(args == null || args.length < 2) {
            System.err.println("Error: Not enough Arguments, use --help for help");
            System.exit(0);
        }
        int idx = 0;
        for(String arg : args) {
            if(arg.equals("--common")) {
                if(args.length >= 3 && args[idx+1].equals("-in")) {
                    File[] files = Wildcard.getMatchingFiles(args[idx+2]);
                    for(File file : files) {
                        try {
                            System.out.println("File "+file.getName()+" is being processed.");
                            byte[] psn = Converter.convertCompatibleToPSN(file);
                            File f = new File(file.getName().substring(0, file.getName().lastIndexOf('.'))+".psn");
                            f.createNewFile();
                            new FileProcessor(f).writeb(psn);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if(args.length >= 4 && args[idx+1].equals("-out")) {
                    File[] files = Wildcard.getMatchingFiles(args[idx+3]);
                    for(File file : files) {
                        try {
                            System.out.println("File "+file.getName()+" is being processed.");
                            byte[] psn = new FileProcessor(file).readb();
                            Reader.unwrapPSNFile(psn, false, file.getName().substring(0, file.getName().lastIndexOf('.'))+"."+args[2].toLowerCase(), args[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if(args[idx+1].equals("-display")) {
                    File[] files = Wildcard.getMatchingFiles(args[idx+2]);
                    for(File file : files) {
                        try {
                            System.out.println("File "+file.getName()+" is being displayed.");
                            byte[] psn = new FileProcessor(file).readb();
                            Reader.unwrapPSNFile(psn, true, null, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            idx++;
        }
    }
}