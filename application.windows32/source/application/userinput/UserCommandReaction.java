package application.userinput;

public abstract class UserCommandReaction {
	private UserCommand commandToReactOn;
	
	public UserCommandReaction(UserCommand commandToReactOn) {
		this.commandToReactOn = commandToReactOn;
	}
	
	public void reactOnMatchingCommand(UserCommand command) {
		if (shoudReactOnCommand(command))
			reactOnCommand(command);
	}
	
	protected abstract void reactOnCommand(UserCommand command);
	
	private boolean shoudReactOnCommand(UserCommand command) {
		return command.equals(commandToReactOn);
	}
}
