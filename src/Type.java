public enum Type {
    /**
     * Created by Dr Andreas Shepley for COSC120 on 03/07/2023
     */
    SELECT, BURGER,SALAD;

    public String toString(){
        return switch (this) {
            case SELECT -> "Select a Meal";
            case BURGER -> "Burger";
            case SALAD -> "Salad";
        };
    }
}












