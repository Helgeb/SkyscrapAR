package application.userinput;

public class KeyCode {
	private int keyCode;

	public KeyCode(int keyCode) {
		this.keyCode = keyCode;
	}
	
	public boolean equals(KeyCode other) {
		return this.keyCode == other.keyCode || other.isAlwaysEqual();
	}

	protected boolean isAlwaysEqual() {
		return false;
	}
}