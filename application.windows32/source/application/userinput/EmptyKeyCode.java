package application.userinput;

public class EmptyKeyCode extends KeyCode {

	public EmptyKeyCode() {
		super(-1);
	}
	
	public boolean equals(KeyCode other) {
		return true;
	}

	protected boolean isAlwaysEqual() {
		return true;
	}
}
