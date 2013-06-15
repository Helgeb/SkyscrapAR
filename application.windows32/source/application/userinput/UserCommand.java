package application.userinput;

public class UserCommand {
	
	UserKeyCommand keyCommand;
	int mouseX;
	int mouseY;
	
	public UserCommand(char key) {
		this.keyCommand = new UserKeyCommand(key);
	}
	
	public UserCommand(char key, int keyCode) {
		this.keyCommand = new UserKeyCommand(key, keyCode);
	}

	public UserCommand(int mouseX, int mouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}
	
	public UserCommand(char key, int keyCode, int mouseX, int mouseY) {
		this.keyCommand = new UserKeyCommand(key, keyCode);
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}
	
	public boolean equals(UserCommand other) {
		return ( this.keyCommand.equals(other.keyCommand) ) || (
			   this.mouseX == other.mouseX && this.mouseY == other.mouseY );
	}
}
