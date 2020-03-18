package org.frdmrt.mtfr;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    private BlockingQueue<String> inputQueue;
    private BlockingQueue<String> resultsQueue;

    public Consumer(BlockingQueue<String> i, BlockingQueue<String> r) {
        this.inputQueue = i;
        this.resultsQueue = r;
    }

    public String processLine(String line) {
        StringBuilder result = new StringBuilder();

        final String[] fieldNames = "field1,field2,field3".split(",");
        //System.out.println(Thread.currentThread().getName() + ": " + line);
        String[] lineFields = line.split(",");
        result.append("{");
        for (int i=0; i<fieldNames.length; i++) {
            if (i>0){
                result.append(", ");
            }
            result.append(fieldNames[i] + " : " + lineFields[i]);
        }
        result.append(" }");
        return result.toString();
    }

    @Override
    public void run() {
        while(true) {
            String line = inputQueue.poll();
            if (line == null && !RunnableController.isProducerAlive()){
                return;
            }
            if (line != null) {
                resultsQueue.add(processLine(line));
            }
        }
    }
}
