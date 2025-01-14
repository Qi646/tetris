package com.ics.tetris;

public class Queue {
    private int[] queue;
    private int front, rear, size;

    public Queue(int initialCapacity) {
        queue = new int[initialCapacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    public void enqueue(int value) {
        rear = (rear + 1) % queue.length;
        queue[rear] = value;
        size++;
    }

    public int dequeue() {
        if (size == 0) {
            return -1;
        }
        int value = queue[front];
        front = (front + 1) % queue.length;
        size--;
        return value;
    }

    public int peek() {
        if (size == 0) {
            return -1;
        }
        return queue[front];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}

