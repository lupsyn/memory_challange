package com.memory;

/**
 * Your task:
 * Identify and annotate the shortcomings in the current implementation as if you were doing a
 * code review, using comments in the CSVReaderWriter.java file.
 *
 * General comments :
 * 
 * 1- Naming conventions : I really don't like the C# style of the code, i know in android there
 *    is not a very clear naming convention, but this is too far away from the google style convetions.
 *
 * 2- This class come without tests, TDD is always a good mode 'how to write code'
 *
 * 3- This class violates the Single Responsibility Principle (Read and Write in same class)
 *    and also the Don't Repeat Yourself principle (for example the read method has in common lot of code)
 *    I would suggest to break the responsibility of reading and writing to separate classes which could also be derived
 *    from some interface.
 *
 * 4- The class is doing I/O in a synchronous fashion, which is a terrible idea.
 *    Using multithreading in this case can improve the performances and the most dangerous thing I/O methods are not thread safe.
 * */

import java.io.*;

public class CSVReaderWriter {
    //Naming convention, why c# ?
    private BufferedReader _bufferedReader = null;
    private BufferedWriter _bufferedWriter = null;

    // It's any particular reason why we are associate to an ENUM to an int here ?
    // We have only to mode (read and write) so we don't need to do it !
    // Also the mode should be capital, so the name convention is not good also here
    public enum Mode {
        Read (1), Write(2);

        private int _mode;
        Mode(int mode) {
            this._mode = mode;
        }
        public int getMode() {
            return _mode;
        }
    }

    public void open(String fileName, Mode mode) throws Exception {
        // We should check parameters. For example, whether file name is valid.
        if (mode == Mode.Read) {
            _bufferedReader = new BufferedReader(new FileReader(fileName));
        } else if (mode == Mode.Write) {
            FileWriter fileWriter = new FileWriter(fileName);
            _bufferedWriter = new BufferedWriter(fileWriter);
        } else {
            //Why general exception ? Better throw always the correct one
            throw new Exception("Unknown file mode for " + fileName);
        }
    }

    //Why this method is not thread safe at all ?
    public void write(String... columns) throws IOException {
        String outPut = "";
        // There is no point in inventing such a thing. You can just use string.Join.
        // The less new code, the less space for mistakes.
        for (int i = 0; i < columns.length; i++)
        {
            outPut += columns[i];
            if ((columns.length - 1) != i)
            {
                //Any reason why this char could not be static or passed into the constructor ?
                outPut += "\t";
            }
        }

        writeLine(outPut);
    }
    //Why this method is not thread safe at all ?
    public boolean read(String[] columns) throws IOException {
        // If you were to use these constants and had two places where you need them
        // then you should make them global class static final constant
        final int FIRST_COLUMN = 0;
        final int SECOND_COLUMN = 1;

        String line;
        String[] splitLine;

        //Any reason why this char could not be declared as global static variable or passed into the constructor ?
        String separator = "\t";

        line = readLine();
        splitLine = line.split(separator);

        if (splitLine.length == 0)
        {
            columns[0] = null;
            columns[1] = null;

            return false;
        }
        else
        {
            columns[0] = splitLine[FIRST_COLUMN];
            columns[1] = splitLine[SECOND_COLUMN];

            return true;
        }
    }
    //Why this methos is not thread safe at all ?
    public boolean read(String column1, String column2) throws IOException {
        // If you were to use these constants and had two places where you need them
        // then you should make them global class static final constant
        final int FIRST_COLUMN = 0;
        final int SECOND_COLUMN = 1;
        //This method can be extracted and used in the first read
        String line;
        String[] splitLine;

        //Any reason why this char could not be declared as global static variable or passed into the constructor ?
        String separator = "\t";

        line = readLine();

        if (line == null)
        {
            column1 = null;
            column2 = null;

            return false;
        }

        splitLine = line.split(separator);

        if (splitLine.length == 0)
        {
            column1 = null;
            column2 = null;

            return false;
        }
        // No check, that column length is at least size of 2
        else
        {
            column1 = splitLine[FIRST_COLUMN];
            column2 = splitLine[SECOND_COLUMN];

            return true;
        }
    }

    //Why this method is not thread safe at all ?
    private void writeLine(String line) throws IOException {
        _bufferedWriter.write(line);
    }
    //Why this method is not thread safe at all ?
    private String readLine() throws IOException {
        return _bufferedReader.readLine();
    }

    public void close() throws IOException {
        if (_bufferedWriter != null)
        {
            _bufferedWriter.close();
        }

        if (_bufferedWriter != null)
        {
            _bufferedWriter.close();
        }
    }
}
