package app.PingPongModel;


public enum GameOutcomeEnum {
    WIN(1),DRAW(0.5),LOSS(0);


    private final double factor;

    GameOutcomeEnum(double factor) {
        this.factor = factor;
    }

    public double getFactor(){
        return this.factor;
    }
}
