public class Threads {
    public static class MyThread extends Thread {
        @Override
        public void run() {
            //do whatever
        }
    }

    public static void main(String[] args) {
        Thread myThread = new Thread();

        myThread.start();

        System.out.println("Test");

    }
}