package aed;
//implements Comparable<Traslado> 
public class Traslado {
    
    private int id;
    private int origen;
    private int destino;
    private int gananciaNeta;
    private int timestamp;
    private int posHeapAntiguo; 
    private int posHeapRedituable;

    public Traslado(int id, int origen, int destino, int gananciaNeta, int timestamp){
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.gananciaNeta = gananciaNeta;
        this.timestamp = timestamp;
    }
    public int getId(){
        return id;
    }

    public int getOrigen(){
        return origen;
    }

    public int getDestino(){
        return destino;
    }
    public int getGananciaNeta() {
        return gananciaNeta;
    }

    public int getTimestamp() {
        return timestamp;
    }


    public int getPosRedituable(){
        return posHeapRedituable;
    }

    public int getPosAntiguo(){
        return posHeapAntiguo;
    }

    public void setPosRedituable(int posicionActualizada){
        this.posHeapRedituable = posicionActualizada;
    }

    public void setPosHeapAntiguo(int posicionActualizada){
        this.posHeapAntiguo = posicionActualizada;
    }

    
    @Override
    public String toString() {
        return String.valueOf(this.id);
    }

    // //compareTo, armo para minheap y maxheap especificamente, en comparatorTraslados
    // @Override
    // public int compareTo(Traslado other) {
    //     if (this.gananciaNeta != other.gananciaNeta) {
    //         return Integer.compare(other.gananciaNeta, this.gananciaNeta);
    //     } else {
    //         return Integer.compare(this.id, other.id);
    //     }
    // }
}
