package com.hiden.biz.wechat.mp;

public class TestNonAtomicLongAssignment {
    private static final long HI = 4294967296L;
    private static final long LO = 1L;
    private static final long TEST_NUMBER = 4294967297L;
    private static long assignee = 0L;

    public TestNonAtomicLongAssignment() {
    }

    public static void main(String[] args) {
        Thread writer = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    TestNonAtomicLongAssignment.assignee = 4294967297L;
                }
            }
        });
        writer.setDaemon(true);
        Thread reader = new Thread(new Runnable() {
            public void run() {
                long i = 0L;

                long test;
                do {
                    ++i;
                    test = TestNonAtomicLongAssignment.assignee;
                } while (test == 4294967297L);

                System.out.print(i + " times:" + TestNonAtomicLongAssignment.toBin(test));
            }
        });
        writer.start();
        reader.start();
    }

    public static String toBin(long n) {
        StringBuilder sb = new StringBuilder(Long.toBinaryString(n));

        for (int padding = 64 - sb.length(); padding > 0; --padding) {
            sb.insert(0, '0');
        }

        return sb.toString();
    }
}
