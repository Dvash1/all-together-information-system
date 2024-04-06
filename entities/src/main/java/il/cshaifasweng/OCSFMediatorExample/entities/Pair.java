package il.cshaifasweng.OCSFMediatorExample.entities;

// Helper class for SimpleServer, so the thread hashmap will be more efficient.
// There isn't a built-in pair or library class in Java.
public class Pair<A, B> {
	private A first;
	private B second;

	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	public A getFirst() {
		return first;
	}

	public B getSecond() {
		return second;
	}

	public void setFirst(A first) {
		this.first = first;
	}

	public void setSecond(B second) {
		this.second = second;
	}
}
