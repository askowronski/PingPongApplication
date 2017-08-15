package app.ViewModel;


public enum GameOutcomeEnum {
    WIN(1.0),DRAW(0.5),LOSS(0);


    private final double factor;

    GameOutcomeEnum(double factor) {
        this.factor = factor;
    }

    public double getFactor(){
        return this.factor;
    }
}
