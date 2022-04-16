package console;

import org.kohsuke.args4j.CmdLineException;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        Console console = new Console();
        try {
            console.commandReader(args);
        } catch (CmdLineException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
