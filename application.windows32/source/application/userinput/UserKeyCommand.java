package application.userinput;

public class UserKeyCommand {
	private Key key;
	private KeyCode keyCode;

	public UserKeyCommand(char key) {
		this.key = new Key(key);
		this.keyCode = new EmptyKeyCode();
	}

	public UserKeyCommand(char key, int keyCode) {
		this.key = new Key(key);
		this.keyCode = new KeyCode(keyCode);
	}
	
	public boolean equals(UserKeyCommand other) {
		return this.key.equals(other.key) && this.keyCode.equals(other.keyCode);
	}
	
}