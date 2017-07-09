package app;


public enum GameOutcome {
    WIN(1),DRAW(0),LOSS(-1);


    private final int factor;

    GameOutcome(int factor) {
        this.factor = factor;
    }

    public int getFactor(){
        return this.factor;
    }
}
