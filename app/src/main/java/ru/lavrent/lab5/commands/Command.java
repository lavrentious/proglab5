package ru.lavrent.lab5.commands;

import ru.lavrent.lab5.interfaces.Executable;

public abstract class Command implements Executable {
  private String name;
  private String description;

  /**
   * initialize the command
   * 
   * @param name        the command's alias
   * @param description text that is displayed in help
   */
  public Command(String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * execute command with no arguments
   */
  public void execute() {
    execute(new String[] {});
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return "Command(name=%s, description=%s)".formatted(name, description);
  }
}
