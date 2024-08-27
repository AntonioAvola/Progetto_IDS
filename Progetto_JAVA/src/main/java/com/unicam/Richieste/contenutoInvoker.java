package com.unicam.Richieste;

public class contenutoInvoker {
    private ICommand Command;

    public void setCommand(ICommand command) {
        this.Command = command;
    }

    public void executeCommand() {
        Command.Execute();
    }
}
