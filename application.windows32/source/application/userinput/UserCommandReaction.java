package application.userinput;

public abstract class UserCommandReaction {
	private UserCommand commandToReactOn;
	public UserCommandReaction(UserCommand commandToReactOn) {
		this.commandToReactOn = commandToReactOn;
	}
	public abstract void reactOnCommand(UserCommand command);
	protected boolean shoudReactOnCommand(UserCommand command) {
		return command.equals(commandToReactOn);
	}
}
