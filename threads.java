public class CreatingThreads {
    public static class MyThread extends Thread {
        @Override
        public void run() {
            //do whatever
            System.out.println
        }
    }

    public static void main(String[] args) {
        Thread myThread = new Thread();

        myThread.start();

    }
}