package org.frdmrt.mtfr;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    private Path fileToRead;
    private BlockingQueue<String> pQueue;

    public Producer(Path filePath, BlockingQueue<String> q) {
        this.fileToRead = filePath;
        this.pQueue = q;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = Files.newBufferedReader(fileToRead);
            String line = null;
            while ((line = reader.readLine()) != null) {
                try {
                    //System.out.println(Thread.currentThread().getName() + " added line \"" + line + "\" into queue, queue size = " + pQueue.size());
                    pQueue.put(line);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
