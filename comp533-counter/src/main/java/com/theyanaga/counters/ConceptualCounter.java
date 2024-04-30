package com.theyanaga.counters;

public class ConceptualCounter implements Counter {
	private int value;
	private ProducerConsumerTurn producerConsumerTurn = ProducerConsumerTurn.PRODUCER_TURN;

	public synchronized int getValue() {
		// wait in condition queue if the the last operation invoked on this object was getValue()
		waitForConsumerTurn();

		// actions taken after user enters release command
		System.out.println("CONSUMED:" + value);

		producerConsumerTurn = ProducerConsumerTurn.PRODUCER_TURN;
		notifyAll(); // move all threads in condition queue to urgent queue

		return value;
	}

	public synchronized void increment() {
		// wait in condition queue if the last operation invoked on this object was increment
		waitForProducerTurn();

		// actions taken after user enters release command
		value++;
		System.out.println("PRODUCED:" + value);

		producerConsumerTurn = ProducerConsumerTurn.CONSUMER_TURN;
		notifyAll(); // move all threads in condition queue to urgent queue
	}

	public void waitForProducerTurn() {
		while (!(producerConsumerTurn == ProducerConsumerTurn.PRODUCER_TURN)) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void waitForConsumerTurn() {
		while (!(producerConsumerTurn == ProducerConsumerTurn.CONSUMER_TURN)) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
