package com.erikmafo.javachess.search;

class HashFunction {
    private final int size;

    public HashFunction(int size) {
        this.size = size;
    }

    private long mod(long a, int b) {
        long c = a % b;
        return (c < 0) ? c + b : c;
    }

    /**
     * Maps the specified key to a non-negative integer
     *
     * @param key any long value
     * @return a non-negative integer
     */
    public int map(long key) {
        return (int) (mod(key, size));
    }

    /**
     * Gets the maximum value that can be produced by this <code>HashFunction</code>
     *
     * @return a non-negative integer
     */
    public int getSize() {
        return size;
    }
}