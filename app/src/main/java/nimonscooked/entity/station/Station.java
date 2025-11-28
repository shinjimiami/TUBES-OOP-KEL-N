package nimonscooked.entity.station;

public abstract class Station {
    private boolean isOccupied;

    public Station() {
        this.isOccupied = false;

    }

    public void interactWithChef(){

    }
    
    public abstract void interactionType();

    public boolean getIsOccupied(){
        return isOccupied;
    }

    public void setIsOccupied(boolean status){
        this.isOccupied = status;
    }
}
