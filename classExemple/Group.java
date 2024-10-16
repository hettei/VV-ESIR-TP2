public class Group {

    private int weight;
    private String name;
    private Color color;

    public Group(String name, Color color, int weight) {
        this.name = name;
        this.color = color;
        this.weight = weight;
    }

    public int compareTo(Group other) {
        return weight - other.weight;
    }

    public void draw() {
        Screen.rectangle(color, name);
    }

    public String getName() { 
        return this.name; 
    }
    
    public void doIt(){
        if(true){
            draw();
        }else{
            draw();
        }
        if(false){
            draw();
        }else{
            draw();
        }
    }

}