package nimonscooked.action;

import nimonscooked.entity.Chef;

public interface Command {
    void execute(Chef chef);
}