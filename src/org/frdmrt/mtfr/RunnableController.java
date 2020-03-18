package org.frdmrt.mtfr;

import java.nio.file.Paths;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class RunnableController {
    //private static final String inFileName = "C:\\Users\\micro\\Downloads\\Organization.csv";
    private static final String[] inFileList = {"C:\\Users\\micro\\Downloads\\Organization.csv", "C:\\Users\\micro\\Downloads\\Anarchy.csv", "C:\\Users\\micro\\Downloads\\Ragtag.csv"};
    private static final int NUMBER_OF_CONSUMERS = 3;
    private static final int QUEUE_SIZE = 100;
    private static BlockingQueue<String> lineQueue;
    private static BlockingQueue<String> resultsQueue;
    private static Collection<Thread> producerThreadCollection;
    private static Collection<Thread> allThreadCollection;

    private static void createAndStartProducers() {
        for (int i=0; i<inFileList.length; i++) {
            System.out.println(("Starting producer-"+i+", filename="+inFileList[i]));
            Producer producer = new Producer(Paths.get(inFileList[i]), lineQueue);
            Thread producerThread = new Thread(producer, "producer-" + i);
            producerThreadCollection.add(producerThread);
            producerThread.start();
        }
        allThreadCollection.addAll(producerThreadCollection);
    }

    private static void createAndStartConsumers() {
        for (int i=0; i<NUMBER_OF_CONSUMERS; i++) {
            Thread consumerThread = new Thread(new Consumer(lineQueue, resultsQueue), "consumer-" + i);
            allThreadCollection.add(consumerThread);
            consumerThread.start();
        }
    }

    public static boolean isProducerAlive() {
        for (Thread t : producerThreadCollection) {
            if (t.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        producerThreadCollection = new ArrayList<Thread>();
        allThreadCollection = new ArrayList<Thread>();
        lineQueue = new LinkedBlockingDeque<String>(QUEUE_SIZE);
        resultsQueue = new LinkedBlockingQueue<String>();

        System.out.println("Starting producer and " + NUMBER_OF_CONSUMERS + " consumers . . .");
        ZonedDateTime startTime = ZonedDateTime.now();
        createAndStartProducers();
        createAndStartConsumers();

        // Wait for all threads to finish:
        for (Thread t : allThreadCollection) {
            try {
                t.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ZonedDateTime endTime = ZonedDateTime.now();
        Duration runTime = Duration.between(startTime, endTime);
        System.out.println("All threads finished, run time = " + runTime + ", resultsQueue size = " + resultsQueue.size());
        System.out.println(resultsQueue.toString());
        //System.out.println(resultsQueue.peek().toString());
        //System.out.println(String.join(System.getProperty("line.separator"), resultsQueue.toString().split(",")));
    }
}
