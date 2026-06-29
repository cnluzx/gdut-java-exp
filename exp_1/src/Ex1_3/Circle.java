package Ex1_3;

public class Circle extends obj{

    private double r;//r半径

    public Circle(double r){
        this.r = r ;

    }
    @Override
    public double get_perimeters(){
        return 2*Math.PI*r;
    }//周长

    @Override
    public double get_area(){
        return Math.PI*r*r;
    }//面积


}
