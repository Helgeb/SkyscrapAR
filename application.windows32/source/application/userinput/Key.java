package application.userinput;

public class Key {
	private char key;

	public Key(char key) {
		this.key = key;
	}
	
	public boolean equals(Key other) {
		return this.key == other.key;
	}
}